package com.example.emos.wx.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.emos.wx.db.dao.*;
import com.example.emos.wx.db.pojo.MessageEntity;
import com.example.emos.wx.db.pojo.TbUser;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.UserService;
import com.example.emos.wx.task.ActiveCodeTask;
import com.example.emos.wx.task.MessageTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@Scope("prototype")
public class UserServiceImpl implements UserService {
    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Autowired
    private TbUserDao userDao;

    @Autowired
    private TbDeptDao deptDao;

    @Autowired
    private TbCheckinDao checkinDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MessageRefDao messageRefDao;

    @Autowired
    private TbFaceModelDao faceModelDao;

    @Autowired
    private MessageTask messageTask;

    @Autowired
    private ActiveCodeTask activeCodeTask;

    @Autowired
    private RedisTemplate redisTemplate;

    private String getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap map = new HashMap();
        map.put("appid", appId);
        map.put("secret", appSecret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String response = HttpUtil.post(url, map);
        JSONObject json = JSONUtil.parseObj(response);
        String openId = json.getStr("openid");
        if (openId == null || openId.length() == 0) {
            throw new RuntimeException("????????????????????????");
        }

        return openId;
    }

    @Override
    public int registerUser(String registerCode, String code, String nickname, String photo) {
        if (registerCode.equals("000000")) {
            boolean bool = userDao.haveRootUser();
            if (!bool) {
                String openId = getOpenId(code);
                HashMap param = new HashMap();
                param.put("deptName","?????????");
                param.put("name","???????????????");
                param.put("username","admin");
                param.put("password","abc123456");
                param.put("openId", openId);
                param.put("nickname", nickname);
                param.put("photo", photo);
                param.put("role", "[0]");
                param.put("status", 1);
                param.put("createTime", new Date());
                param.put("root", true);
                userDao.insert(param);
                int id = userDao.searchIdByOpenId(openId);

                MessageEntity entity = new MessageEntity();
                entity.setSenderId(0);
                entity.setSenderName("????????????");
                entity.setUuid(IdUtil.simpleUUID());
                entity.setMsg("???????????????????????????????????????????????????????????????????????????");
                entity.setSendTime(new Date());
                messageTask.sendAsync(id + "", entity);
                return id;
            } else {
                throw new EmosException("?????????????????????????????????");
            }
        } else if (!redisTemplate.hasKey(registerCode)) {
            //???????????????????????????
            throw new EmosException("????????????????????????");
        } else {
            int userId = Integer.parseInt(redisTemplate.opsForValue().get(registerCode).toString());
            //????????????????????????ROOT??????
            TbUser entity = new TbUser();
            String openId = getOpenId(code);
            HashMap param = new HashMap();
            param.put("openId", openId);
            param.put("nickname", nickname);
            param.put("photo", photo);
            param.put("userId", userId);
            int row = userDao.activeUserAccount(param);
            if (row != 1) {
                throw new EmosException("??????????????????");
            }
            redisTemplate.delete(registerCode);
            return userId;
        }
    }

    @Override
    public Set<String> searchUserPermissions(int userId) {
        Set<String> permissions = userDao.searchUserPermissions(userId);
        return permissions;
    }

    @Override
    public Integer login(String code) {
        String openId = getOpenId(code);
        Integer id = userDao.searchIdByOpenId(openId);
        if (id == null) {
            throw new EmosException("???????????????");
        }

        return id;
    }

    @Override
    public TbUser searchById(int userId) {
        TbUser user = userDao.searchById(userId);

        return user;
    }

    @Override
    public String searchUserHiredate(int userId) {
        String hiredate = userDao.searchUserHiredate(userId);
        return hiredate;
    }

    @Override
    public HashMap searchUserSummary(int userId) {
        HashMap map = userDao.searchUserSummary(userId);
        return map;
    }

    @Override
    public ArrayList<HashMap> searchUserGroupByDept(String keyword) {
        ArrayList<HashMap> list_1 = deptDao.searchDeptMembers(keyword);
        ArrayList<HashMap> list_2 = userDao.searchUserGroupByDept(keyword);
        for (HashMap map_1 : list_1) {
            long deptId = (Long) map_1.get("id");
            ArrayList members = new ArrayList();
            for (HashMap map_2 : list_2) {
                long id = (Long) map_2.get("deptId");
                if (deptId == id) {
                    members.add(map_2);
                }
            }
            map_1.put("members", members);
        }
        return list_1;
    }

    @Override
    public ArrayList<HashMap> searchMembers(List param) {
        ArrayList<HashMap> list = userDao.searchMembers(param);
        return list;
    }

    @Override
    public List<HashMap> selectUserPhotoAndName(List param) {
        List<HashMap> list = userDao.selectUserPhotoAndName(param);
        return list;
    }

    @Override
    public String searchMemberEmail(int id) {
        String email = userDao.searchMemberEmail(id);
        return email;
    }

    @Override
    public void insertUser(HashMap param) {
        //????????????
        int row = userDao.insert(param);
        if (row == 1) {
            String email = (String) param.get("email");
            //??????Email?????????????????????????????????
            int userId = userDao.searchUserIdByEmail(email);
            //???????????????????????????????????????
            activeCodeTask.sendActiveCodeAsync(userId, email);
        } else {
            throw new EmosException("????????????????????????");
        }
    }

    @Override
    public HashMap searchUserInfo(int userId) {
        HashMap map = userDao.searchUserInfo(userId);
        return map;
    }

    @Override
    public int updateUserInfo(HashMap param) {
        //??????????????????
        int rows = userDao.updateUserInfo(param);
        //?????????????????????????????????
        if (rows == 1) {
            Integer userId = (Integer) param.get("userId");
            String msg = "???????????????????????????????????????";
            MessageEntity entity = new MessageEntity();
            entity.setSenderId(0);  //??????????????????
            entity.setSenderPhoto("../../static/system.jpg");
            entity.setSenderName("????????????");
            entity.setMsg(msg);
            entity.setSendTime(new Date());
            messageTask.sendAsync(userId.toString(), entity);
        }
        return rows;
    }

    @Override
    public void deleteUserById(int id) {
        int row = userDao.deleteUserById(id); //??????????????????
        if (row != 1) {
            throw new EmosException("??????????????????");
        }
        checkinDao.deleteUserCheckin(id);
        messageDao.deleteUserMessage(id);
        messageRefDao.deleteUserMessageRef(id);
        faceModelDao.deleteFaceModel(id);
        messageTask.deleteQueue(id + "");
    }

    @Override
    public JSONObject searchUserContactList() {
        ArrayList<HashMap> list = userDao.searchUserContactList();
        String letter = null;
        JSONObject json = new JSONObject(true);
        JSONArray array = null;
        for (HashMap<String, String> map : list) {
            String name = map.get("name");
            String firstLetter = PinyinUtil.getPinyin(name).charAt(0) + "";
            firstLetter = firstLetter.toUpperCase();
            if (letter == null || !letter.equals(firstLetter)) {
                letter = firstLetter;
                array = new JSONArray();
                json.set(letter, array);
            }
            array.put(map);
        }
        return json;
    }

}
