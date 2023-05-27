package com.example.sharingplatform.entity;

import javax.persistence.*;

@Table(name="picture",indexes = {@Index(columnList = "workID")})
@Entity
public class picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pictureID;
    private long workID;
    private String savePath;

    public long getPictureID() {
        return pictureID;
    }

    public void setPictureID(long pictureID) {
        this.pictureID = pictureID;
    }

    public long getWorkID() {
        return workID;
    }

    public void setWorkID(long workID) {
        this.workID = workID;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
}
