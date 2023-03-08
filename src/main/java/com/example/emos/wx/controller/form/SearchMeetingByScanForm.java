package com.example.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("查询")
public class SearchMeetingByScanForm {

    @NotNull(message = "meetingRoomId不能为空")
    @Min(value = 1, message = "meetingRoomId")
    private Integer meetingRoomId;
}
