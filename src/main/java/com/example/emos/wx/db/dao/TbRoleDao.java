package com.example.emos.wx.db.dao;

import com.example.emos.wx.db.pojo.TbRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Entity com.example.emos.wx.db.pojo.TbRole
 */
@Mapper
public interface TbRoleDao {
    public List<TbRole> searchAllRole();
    public long searchRoleUsersCount(int id);
    public ArrayList<HashMap> searchRoleOwnPermission(int id);
    public int insertRole(TbRole role);
    public int updateRolePermissions(TbRole role);
    public ArrayList<HashMap> searchAllPermission();
    public int deleteRoleById(int id);
}