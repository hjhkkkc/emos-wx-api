package com.example.emos.wx.controller;

import com.example.emos.wx.common.util.R;
import com.example.emos.wx.controller.form.SearchMeetingRoomByScanForm;
import com.example.emos.wx.service.MeetingRoomService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("meeting_room")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @PostMapping("/searchMeetingRoomByScan")
    @RequiresAuthentication
    @ApiOperation("根据扫描二维码或者NFC标签获取会议室信息")
    public R searchMeetingRoomByScan(@Valid @RequestBody SearchMeetingRoomByScanForm form) {
        HashMap map = meetingRoomService.searchMeetingRoomByScan(form.getMeetingRoomId());
        return R.ok().put("result",map);
    }
}
