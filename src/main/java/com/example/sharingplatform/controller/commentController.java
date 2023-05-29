package com.example.sharingplatform.controller;

import com.example.sharingplatform.entity.comment;
import com.example.sharingplatform.entity.commentResult;
import com.example.sharingplatform.service.CommentService;
import com.example.sharingplatform.service.UserService;
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
    CommentService commentservice;

    @PostMapping("/add")
    public Result commentAddController(@RequestPart("workID") long workID,
                                       @RequestPart("userID") long userID,
                                       @RequestPart("commentContent") String commentContent,
                                       HttpServletRequest request){
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        commentservice.add(workID,userID,commentContent);
        return Result.success(200,"成功");
    }

    @GetMapping("/get")
    public Result<commentResult> commentGetController(@RequestParam long workID, HttpServletRequest request) {
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        List<comment> res = commentservice.getCommentDetail(workID);
        commentResult commentRes = new commentResult();
        commentRes.setResultNumber(res.size());
        commentRes.setWorkResult(res);
        return Result.success(commentRes,200,"成功");
    }

    @DeleteMapping("/delete")
    public Result deleteCommentController(@RequestPart("workID") long workID,
                                          @RequestPart("userID") long userID,
                                          @RequestPart("commentID") long commentID,
                                          HttpServletRequest request)
    {
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        comment res = commentservice.getCommentByID(commentID);
        if (res == null) return Result.error(404,"评论不存在");
        if (res.getUserID()!=userID) Result.error(403,"错误");
        commentservice.deleteComment(workID,res);
        return Result.success(200,"成功");
    }
}
