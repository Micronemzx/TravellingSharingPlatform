package com.example.sharingplatform.service;

public interface NotificationService {
    void sendNotification(long userID,String msg,int type);
}
