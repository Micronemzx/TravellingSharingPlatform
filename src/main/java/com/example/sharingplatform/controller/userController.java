package com.example.sharingplatform.controller;

import com.example.sharingplatform.entity.*;
import com.example.sharingplatform.service.WorkService;
import com.example.sharingplatform.service.UserService;
import com.example.sharingplatform.utils.*;

import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class userController {

    @Resource
    private WorkService workservice;

    @Resource
    private UserService userservice;

    @GetMapping("/testGet") //测试GET
    public Result testGet(@RequestParam String a) { return Result.success(500,a); }

    @PostMapping("/testPost")   //测试POST
    public Result testPost(@RequestBody String a) { return Result.success(500,a); }

    @GetMapping("/sendmailcode")    //发送邮箱验证码
    public Result sendmailcode(@RequestParam String mail) throws MessagingException {
        return Result.success(userservice.sendMailCodeForRegister(mail));
    }

    @PostMapping("/register")   //注册
    public Result register(@RequestBody user newUser)
    {
        return Result.success(userservice.registerUser(newUser));
    }

    @GetMapping("/login")   //登录
    public Result<user> loginController(@RequestParam("email") String email,@RequestParam("password") String password, HttpServletResponse response){
        user user=userservice.ifExist(email);
        if(user==null){
            return Result.success(null,200,"用户不存在");
        }
        else {
            {
                //user res=userservice.login(email,password);
                if(!Objects.equals(user.getPassword(), password)){
                    return Result.success(null,200,"wrong password");
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

    @GetMapping("/verify")  //找回密码-验证身份
    public Result<user> verifyController(@RequestBody String email,@RequestBody String mailCode)
    {
        user res = userservice.ifExist(email);
        if (Objects.equals(res.getMailCode(), mailCode))
        {
            return Result.success(res,200,"成功");
        }
        return Result.success(res,200,"验证码错误");
    }

    @PostMapping("/resetpassword")  //重置密码
    public Result resetPasswordController(@RequestBody String email,@RequestBody String password)
    {
        return Result.success(200,userservice.resetpassword(email,password));
    }

    @PostMapping(path="/setPersonalDetail",consumes = {"multipart/form-data"})
    public Result setPersonalDetailController(@RequestPart("user") user userInfo,
                                              @RequestPart("photo") @NotNull MultipartFile file,
                                              HttpServletRequest request){
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");

        user res = userservice.getUserByID(userInfo.getUserID());
        if (res == null) return Result.error(500,"失败，用户不存在");
        Result result = userservice.savePhoto(userInfo.getUserID(),file);
        if (result.getCode()!=200) return result;

        userservice.saveDetail(userInfo,res);
        return Result.success(200,"成功");
    }
}
