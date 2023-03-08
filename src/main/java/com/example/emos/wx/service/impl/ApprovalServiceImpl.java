package com.example.emos.wx.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.emos.wx.common.util.R;
import com.example.emos.wx.config.shiro.JwtUtil;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Value("${workflow.url}")
    private String workflow;

    @Value("${emos.code}")
    private String code;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ArrayList searchUserTaskListByPage(HashMap param) {
        param.put("code",code);
        String url = workflow + "/workflow/searchUserTaskListByPage";
        HttpResponse resp = HttpRequest.post(url).header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(param)).execute();
        if (resp.getStatus() == 200) {
            JSONObject json = JSONUtil.parseObj(resp.body());
            ArrayList result=json.get("result",ArrayList.class);
            return  result;
        }
        else{
            throw new EmosException("查询工作流审批任务失败");
        }
    }

    @Override
    public void approveTask(HashMap param) {
        param.put("code",code);
        String url = workflow + "/workflow/approvalTask";
        HttpResponse resp = HttpRequest.post(url).header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(param)).execute();
        if (resp.getStatus() != 200) {
            throw new EmosException("查询工作流审批任务失败");
        }
    }
}
