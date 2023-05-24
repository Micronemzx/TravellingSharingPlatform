package com.example.sharingplatform.controller;

import com.example.sharingplatform.entity.user;
import com.example.sharingplatform.entity.work;
import com.example.sharingplatform.service.UserService;
import com.example.sharingplatform.service.WorkService;
import com.example.sharingplatform.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/work")
public class workController {
    @Resource
    private WorkService workservice;
    @Resource
    private UserService userservice;
    @GetMapping("/testGet") //测试GET
    public Result testGet(@RequestParam String a) { return Result.success(500,a); }

    @PostMapping("/testPost")   //测试POST
    public Result testPost(@RequestBody String a) { return Result.success(500,a); }

    @PostMapping("/like")
    public Result likeWorkController(@RequestPart("userID") long userID, @RequestPart("workID") long workID, HttpServletRequest request)
    {
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        user userRes = userservice.getUserByID(userID);
        work workRes = workservice.getWorkByID(workID);
        if (userRes==null||workRes==null) { return Result.error(500,"不存在该用户或该动态"); }
        workservice.like(workRes,userRes);
        return Result.success(200,"成功");
    }
}
