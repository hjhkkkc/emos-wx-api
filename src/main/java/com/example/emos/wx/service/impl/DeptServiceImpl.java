package com.example.emos.wx.service.impl;

import com.example.emos.wx.db.dao.TbDeptDao;
import com.example.emos.wx.db.dao.TbUserDao;
import com.example.emos.wx.db.pojo.TbDept;
import com.example.emos.wx.exception.EmosException;
import com.example.emos.wx.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private TbDeptDao deptDao;

    @Autowired
    private TbUserDao userDao;

    @Override
    public List<TbDept> searchAllDept() {
        List<TbDept> list = deptDao.searchAllDept();
        return list;
    }

    @Override
    public int insertDept(String deptName) {
        int row = deptDao.insertDept(deptName);
        if (row != 1) {
            throw new EmosException("部门添加失败");
        }
        return row;
    }

    @Override
    public void deleteDeptById(int id) {
        //查询部门是否有数据
        long count = userDao.searchUserCountInDept(id);
        if (count > 0) {
            throw new EmosException("部门中有员工，无法删除部门");
        } else {
            int row = deptDao.deleteDeptById(id);
            if (row != 1) {
                throw new EmosException("部门删除失败");
            }
        }
    }

    @Override
    public void updateDeptById(TbDept entity){
        int row=deptDao.updateDeptById(entity);
        if (row != 1) {
            throw new EmosException("部门删除失败");
        }
    }
}
