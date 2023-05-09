package com.example.sharingplatform.entity;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.util.Date;

@Table(name="notification")
@Entity
public class notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long notificationID;
    private String notificationContent;
    private Date notificationTime;
    private int notificationType;
    private long notificationReceiver;

    public long getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(long notificationID) {
        this.notificationID = notificationID;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public Date getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public long getNotificationReceiver() {
        return notificationReceiver;
    }

    public void setNotificationReceiver(long notificationReceiver) {
        this.notificationReceiver = notificationReceiver;
    }
}
