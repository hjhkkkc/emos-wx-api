package com.example.emos.wx.db.pojo;

import lombok.Data;

@Data
public class TbMeetingRoom {
    private Integer id;
    private String name;
    private Short max;
    private String desc;
    private Byte status;
}
