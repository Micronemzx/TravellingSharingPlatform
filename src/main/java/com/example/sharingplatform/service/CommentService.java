package com.example.sharingplatform.service;

import com.example.sharingplatform.entity.comment;

import java.util.List;

public interface CommentService {
    void add(long workID,long userID,String commentContent);

    List<comment> getCommentDetail(long workID);
    comment getCommentByID(long commentID);

    void deleteComment(long workID, comment res);
}
