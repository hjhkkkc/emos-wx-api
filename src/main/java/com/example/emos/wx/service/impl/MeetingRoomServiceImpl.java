package com.example.emos.wx.service.impl;

import com.example.emos.wx.db.dao.TbMeetingRoomDao;
import com.example.emos.wx.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {

    @Autowired
    private TbMeetingRoomDao meetingRoomDao;

    @Override
    public HashMap searchMeetingRoomByScan(int meetingRoomId) {
        HashMap map = meetingRoomDao.searchMeetingRoomByScan(meetingRoomId);
        return map;
    }
}
