package com.example.sharingplatform.entity;

import javax.persistence.*;

@Table(name="complaint",indexes = {@Index(columnList = "workID")})
@Entity
public class complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long workID;
    private String title;
    private long userID;
    private int status;

    public long getWorkID() {
        return workID;
    }

    public void setWorkID(long workID) {
        this.workID = workID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
