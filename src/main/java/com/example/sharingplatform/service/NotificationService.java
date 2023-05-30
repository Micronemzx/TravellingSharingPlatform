package com.example.sharingplatform.service;

import com.example.sharingplatform.entity.notification;

import java.util.List;

public interface NotificationService {
    void sendNotification(long userID,String msg,int type);

    List<notification> getNotification(long userId);
}
