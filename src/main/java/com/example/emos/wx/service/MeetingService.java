package com.example.emos.wx.service;

import com.example.emos.wx.db.pojo.TbMeeting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface MeetingService {
    public void insertMeeting(TbMeeting entity);

    public ArrayList<HashMap> searchMyMeetingListByPage(HashMap param);

    public HashMap searchMeetingById(int id);

    public void updateMeetingInfo(HashMap param);

    public void deleteMeetingById(int id);

    public Long searchRoomIdByUUID(String uuid);

    public List<String> searchUserMeetingInMonth(HashMap param);

    public boolean searchCanCheckinMeeting(HashMap param);

    public int updateMeetingPresent(HashMap param);

    public HashMap searchMeetingByScan(HashMap param);

}
