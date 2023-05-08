package com.example.sharingplatform.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name="work")
@Entity
public class work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long workID;
    private String title;
    private String content;
    //private String[] picture;
    private String userName;
    private long userID;
    private Date createTime;
    private String place;
    private long likeNumber;
    private long commentNumber;
    private int authority;
    private long hotPoint;
    public long getWorkID() {
        return workID;
    }

    public void setWorkID(long workID) {
        this.workID = workID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public long getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(long likeNumber) {
        this.likeNumber = likeNumber;
    }

    public long getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(long commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public long getHotPoint() {
        return hotPoint;
    }

    public void setHotPoint(long hotPoint) {
        this.hotPoint = hotPoint;
    }
}
