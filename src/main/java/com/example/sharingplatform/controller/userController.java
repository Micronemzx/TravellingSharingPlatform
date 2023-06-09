package com.example.sharingplatform.controller;

import com.example.sharingplatform.entity.*;
import com.example.sharingplatform.service.NotificationService;
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
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class userController {

    @Resource
    private UserService userservice;
    @Resource
    private NotificationService notificationservice;

    @GetMapping("/testGet") //测试GET
    public Result testGet(@RequestParam String a) { return Result.success(500,a); }

    @PostMapping("/testPost")   //测试POST
    public Result testPost(@RequestBody String a) { return Result.success(500,a); }

    @GetMapping("/sendmailcode")    //发送邮箱验证码
    public Result sendmailcode(@RequestParam String mail) throws MessagingException {
        return Result.success(200,userservice.sendMailCode(mail));
    }

    @PostMapping("/register")   //注册
    public Result register(@RequestBody user newUser)
    {
        String res = userservice.registerUser(newUser);
        if (Objects.equals(res, "成功注册")) return Result.success(200,res);
        else return Result.error(500,res);
    }

    @GetMapping("/login")   //登录
    public Result<user> loginController(@RequestParam("email") String email,
                                        @RequestParam("password") String password,
                                        HttpServletResponse response){
        user user=userservice.ifExist(email);
        if(user==null){
            return Result.success(null,200,"用户不存在");
        }
        else {
            if (!userservice.ifBanned(user)) return Result.error(null,403,"用户已被封禁");
            else
            {
                //user res=userservice.login(email,password);
                if(!Objects.equals(user.getPassword(), password)){
                    return Result.error(null,500,"wrong password");
                }
                else {
                    user res=userservice.login(user);
                    Cookie cookie = new Cookie("login_token", user.getToken());
                    cookie.setMaxAge(3 * 24 * 60 * 60);//有效期3天
                    cookie.setPath("/");//必须要设置
                    cookie.setDomain("");
                    cookie.setHttpOnly(false);
                    response.addCookie(cookie);
                    res.setToken(null);
                    res.setPassword(null);
                    return Result.success(res, 200, "successful");
                }
            }
        }
    }//登录

    @GetMapping("/verify")  //找回密码-验证身份
    public Result<user> verifyController(@RequestParam String email,@RequestParam String mailCode)
    {
        user res = userservice.ifExist(email);
        if (res == null) return Result.error(null,404,"不存在该用户");
        return userservice.verifyUser(res,email,mailCode);
    }

    @PostMapping("/resetpassword")  //重置密码
    public Result resetPasswordController(@RequestBody user email_password)
    {
        String email=email_password.getEmail();
        String password=email_password.getPassword();
        System.out.println(email+' '+password);
        return Result.success(200,userservice.resetpassword(email,password));
    }

    @PostMapping(path="/setPersonalDetail",consumes = {"multipart/form-data"})  //修改用户信息（头像，生日，电话，性别）
    public Result setPersonalDetailController(@RequestPart("user") user userInfo,
                                              @RequestPart("photo") @NotNull MultipartFile file,
                                              HttpServletRequest request){
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");

        user res = userservice.getUserByID(userInfo.getUserID());
        if (res == null) return Result.error(500,"失败，用户不存在");
        Result result = userservice.savePhoto(userInfo.getUserID(),file);
        if (result.getCode()!=200) return result;
        res.setAvatar(result.getMsg());
        userservice.saveDetail(userInfo,res);
        return Result.success(200,"成功");
    }

    @PostMapping("/delete")   //删除用户
    public Result deleteUserController(@RequestBody long userID,HttpServletRequest request) {
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        user res = userservice.getUserByID(userID);
        if (res!=null) { userservice.deleteUser(res); return Result.success(200,"注销成功"); }
        return Result.error(403,"用户不存在");
    }

    @GetMapping("/logout")      //用户登出
    public Result logoutController(@RequestParam long userID,HttpServletRequest request,HttpServletResponse response)
    {
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        user res = userservice.getUserByID(userID);
        if (res==null||res.getLogin()==0) return Result.error(400,"失败");
        userservice.logout(res);
        Cookie cookie = new Cookie("login_token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return Result.success(200,"successful");
    }

    @GetMapping("/search")  //搜索用户
    public Result<userResult> searchUserController(@RequestParam String userName, HttpServletRequest request){
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(null,401,"NeedLogin");
        List<user> res = userservice.searchUser(userName);
        userResult userresult = new userResult();
        userresult.setResultNumber(res.size());
        userresult.setUserResult(res);
        return Result.success(userresult,200,"成功");
    }

    @GetMapping("/getNotice")    //获取通知
    public Result<notificationResult> getNotificationController(@RequestParam long userID,HttpServletRequest request){
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(null,401,"NeedLogin");
        List<notification> res = notificationservice.getNotification(userID);
        notificationResult result = new notificationResult();
        result.setResultNumber(res.size());
        result.setNotificationResult(res);
        return Result.success(result);
    }

    @GetMapping("/getAvatar")  //获取头像
    public Result getPictureController(@RequestParam long userID,HttpServletResponse response){
        user res = userservice.getUserByID(userID);
        if (res==null||res.getAvatar()==null) return Result.error(404,"不存在");

        String ans = userservice.sendPicture(res.getAvatar(),response);
        if (Objects.equals(ans, "successful")) return Result.success(200,"成功");
        else return Result.error(500,ans);
    }
}
