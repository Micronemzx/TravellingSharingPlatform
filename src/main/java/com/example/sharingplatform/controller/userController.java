package com.example.sharingplatform.controller;

import com.example.sharingplatform.entity.*;
import com.example.sharingplatform.service.WorkService;
import com.example.sharingplatform.service.UserService;
import com.example.sharingplatform.utils.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class userController {

    @Resource
    private WorkService workservice;

    @Resource
    private UserService userservice;

    @GetMapping("/testGet")
    public Result testGet(@RequestParam String a) { return Result.success(500,a); }

    @PostMapping("/testPost")
    public Result testPost(@RequestBody String a) { return Result.success(500,a); }

    @GetMapping("/sendmailcode")
    public Result sendmailcode(@RequestParam String mail) throws MessagingException {
        return Result.success(userservice.sendMailCodeForRegister(mail));
    }

    @PostMapping("/register")
    public Result register(@RequestBody user newUser)
    {
        return Result.success(userservice.registerUser(newUser));
    }

    @GetMapping("/login")
    public Result<user> loginController(@RequestParam("email") String email,@RequestParam("password") String password, HttpServletResponse response){
        user user=userservice.ifExist(email);
        if(user==null){
            return Result.error(400,"用户不存在");
        }
        else {
            {
                //user res=userservice.login(email,password);
                if(!Objects.equals(user.getPassword(), password)){
                    return Result.error(403,"wrong password");
                }
                else {
                    user res=userservice.login(user);
                    Cookie cookie = new Cookie("login_token", user.getToken());
                    cookie.setMaxAge(3 * 24 * 60 * 60);//有效期3天
                    cookie.setPath("/");//必须要设置
                    cookie.setDomain("");
                    cookie.setHttpOnly(false);
                    response.addCookie(cookie);
                    return Result.success(res, 200, "successful");
                }
            }
        }
    }//登录

}
