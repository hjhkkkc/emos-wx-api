package com.example.emos.wx.controller;

import cn.hutool.json.JSONUtil;
import com.example.emos.wx.common.util.R;
import com.example.emos.wx.config.shiro.JwtUtil;
import com.example.emos.wx.controller.form.ApproveTaskForm;
import com.example.emos.wx.controller.form.SearchUserTaskListByPageForm;
import com.example.emos.wx.service.ApprovalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/approval")
@Api("审批模块Web接口")
public class ApprovalController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ApprovalService approvalService;

    @PostMapping("/searchUserTaskListByPage")
    @ApiOperation("查询审批任务分页列表")
    @RequiresPermissions(value = {"WORKFLOW:APPROVAL"})
    public R searchUserTaskListByPage(@Valid @RequestBody SearchUserTaskListByPageForm form, @RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token);
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("userId",userId);
        ArrayList list = approvalService.searchUserTaskListByPage(param);
        return R.ok().put("result", list);
    }

    @PostMapping("/approveTask")
    @ApiOperation("审批任务")
    @RequiresPermissions(value = {"WORKFLOW:APPROVAL"})
    public R approveTask(@Valid @RequestBody ApproveTaskForm form){
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        approvalService.approveTask(param);
        return R.ok();
    }
}
