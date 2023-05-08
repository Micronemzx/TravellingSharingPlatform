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

}
