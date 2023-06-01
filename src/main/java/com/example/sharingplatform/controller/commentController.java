package com.example.sharingplatform.controller;

import com.example.sharingplatform.entity.comment;
import com.example.sharingplatform.entity.commentResult;
import com.example.sharingplatform.entity.user;
import com.example.sharingplatform.entity.work;
import com.example.sharingplatform.service.CommentService;
import com.example.sharingplatform.service.NotificationService;
import com.example.sharingplatform.service.UserService;
import com.example.sharingplatform.service.WorkService;
import com.example.sharingplatform.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class commentController {
    @Resource
    UserService userservice;
    @Resource
    WorkService workservice;
    @Resource
    CommentService commentservice;
    @Resource
    NotificationService notificationservice;

    @PostMapping("/add")    //添加评论
    public Result commentAddController(@RequestBody comment newComment,HttpServletRequest request){
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        long workID = newComment.getWorkID();
        long userID = newComment.getUserID();
        String commentContent = newComment.getCommentContent();
        commentservice.add(workID,userID,commentContent);
        work res = workservice.getWorkByID(workID);
        user writer = userservice.getUserByID(userID);
        notificationservice.sendNotification(res.getUserID(),"用户 "+writer.getUserName()+" 评论了您的动态（ "+res.getTitle()+" ）: \n"+commentContent,2);
        return Result.success(200,"成功");
    }

    @GetMapping("/get")     //获取动态下的所有评论
    public Result<commentResult> commentGetController(@RequestParam long workID, HttpServletRequest request) {
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(null,401,"NeedLogin");
        List<comment> res = commentservice.getCommentDetail(workID);
        commentResult commentRes = new commentResult();
        commentRes.setResultNumber(res.size());
        commentRes.setWorkResult(res);
        return Result.success(commentRes,200,"成功");
    }

    @PostMapping("/delete")   //删除评论
    public Result deleteCommentController(@RequestBody comment comment,HttpServletRequest request)
    {
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        long commentID = comment.getCommentID();
        long userID = comment.getUserID();
        long workID = comment.getWorkID();
        comment res = commentservice.getCommentByID(commentID);
        if (res == null) return Result.error(404,"评论不存在");
        if (res.getUserID()!=userID) Result.error(403,"错误");
        commentservice.deleteComment(workID,res);
        return Result.success(200,"成功");
    }
}
