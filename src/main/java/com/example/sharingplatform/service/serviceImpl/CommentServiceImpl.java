package com.example.sharingplatform.service.serviceImpl;

import com.example.sharingplatform.entity.comment;
import com.example.sharingplatform.entity.place;
import com.example.sharingplatform.entity.work;
import com.example.sharingplatform.repository.commentRepository;
import com.example.sharingplatform.repository.placeRepository;
import com.example.sharingplatform.repository.workRepository;
import com.example.sharingplatform.service.CommentService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    commentRepository commentRep;
    @Resource
    workRepository workRep;
    @Resource
    placeRepository placeRep;

    @Override
    public void add(long workID, long userID, String commentContent) {
        comment entity = new comment();
        entity.setWorkID(workID);
        entity.setUserID(userID);
        entity.setCommentContent(commentContent);
        Date now = new Date();
        entity.setCommentTime(now);
        commentRep.save(entity);
        work res = workRep.findByWorkID(workID);
        res.setCommentNumber(res.getCommentNumber()+1);
        res.setHotPoint(res.getHotPoint()+1);
        workRep.save(res);
        addPlaceHot(res, placeRep);
    }

    public static void addPlaceHot(work res, placeRepository placeRep) {
        place place = placeRep.findByName(res.getPlace());
        if (place==null) { place = new place(); place.setName(res.getPlace()); place.setHotPoint(0); }
        place.setHotPoint(place.getHotPoint()+1);
        placeRep.save(place);
    }

    @Override
    public List<comment> getCommentDetail(long workID)
    {
        List<comment> res = commentRep.findByWorkIDOrderByCommentTimeDesc(workID);
        return res;
    }

    @Override
    public comment getCommentByID(long commentID) {
        return commentRep.findByCommentID(commentID);
    }

    @Override
    public void deleteComment(long workID, comment res) {
        work result = workRep.findByWorkID(workID);
        commentRep.delete(res);
        result.setCommentNumber(result.getCommentNumber()-1);
        workRep.save(result);
    }


}
