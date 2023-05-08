package com.example.sharingplatform.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "comment")
@Entity
public class comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentID;
    private long userId;
    private long workID;
    private String commentContent;
    private Date commentTime;

    public long getCommentID() {
        return commentID;
    }

    public void setCommentID(long commentID) {
        this.commentID = commentID;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getWorkID() {
        return workID;
    }

    public void setWorkID(long workID) {
        this.workID = workID;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }
}
