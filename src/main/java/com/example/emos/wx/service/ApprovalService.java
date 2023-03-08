package com.example.emos.wx.service;

import java.util.ArrayList;
import java.util.HashMap;

public interface ApprovalService {

    public ArrayList searchUserTaskListByPage(HashMap param);

    public void approveTask(HashMap param);
}
