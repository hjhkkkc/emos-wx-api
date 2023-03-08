package com.example.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("根据扫描二维码或者NFC标签获取会议室信息表单")
public class SearchMeetingRoomByScanForm {
    @NotNull(message = "meetingRoomId不能为空")
    @Min(value = 1, message = "MeetingRoomId不能小于1")
    private Integer meetingRoomId;
}
