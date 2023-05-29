package com.example.sharingplatform.service.serviceImpl;

import com.example.sharingplatform.entity.email;
import com.example.sharingplatform.entity.user;
import com.example.sharingplatform.service.UserService;
import com.example.sharingplatform.repository.*;
import com.example.sharingplatform.utils.Result;
import com.example.sharingplatform.utils.uuID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private JavaMailSender mailSender;
    @Resource
    private mailRepository mailRep;
    @Resource
    private userRepository userRep;
    @Resource
    private workRepository workRep;
    public boolean isVaildToken(String token) {
        user user=userRep.findByToken(token);
        if(user==null) return false;
        else return user.getLogin() == 1;
    }
    public boolean checkToken(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        if(cookies==null) return false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login_token")) {//有储存token的cookie
                return isVaildToken(cookie.getValue());
            }
        }
        return false;
    }


    private String getRandCode() {
        String all = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        int len = all.length();
        Random rand = new Random();
        StringBuilder code= new StringBuilder();
        for (int i=1;i<=8;++i) {
            int index = rand.nextInt(len);
            code.append(all.charAt(index));
        }
        return code.toString();
    }

    private void sendMail(String receiver,String message) throws MessagingException {

        //复杂邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom("旅游分享平台");
        messageHelper.setTo(receiver);
        messageHelper.setSubject("旅游分享平台邮箱验证码");
        messageHelper.setText("亲爱的用户：\n     您好！您正在使用旅游分享平台的身份验证服务，本次请求验证码为：\n"+message+"\n请勿向他人泄露该验证码，否则您的账号将有被盗号的风险。\n");
        mailSender.send(mimeMessage);
    }

    @Override
    public String sendMailCode(String mail) throws MessagingException {
        //判断是否对同一个邮箱重复发送
        email entity = mailRep.findByEmail(mail);
        Date now = new Date();
        long clock = now.getTime();
        if (entity != null) {
            long gap=clock-entity.getLastUpdateTime().getTime();
            gap=gap/1000/60;
            if (gap==0) return "请勿频繁发送，稍后重试";
        }
        else {
            entity = new email();
            entity.setEmail(mail);
        }
        //生成随机验证码
        String mailCode = getRandCode();
        //发送邮件
        entity.setMailCode(mailCode);
        entity.setLastUpdateTime(now);
        mailRep.save(entity);
        sendMail(mail,mailCode);
        return "成功";
    }
    @Override
    public String sendMailCodeForRegister(String mail) throws MessagingException {
        email entity = mailRep.findByEmail(mail);
        if (entity.getOwnerID()!=0) return "该邮箱已注册账户，请直接登录或更换邮箱";
        else return sendMailCode(mail);
    }
    //用户注册，判断用户填写的邮箱验证码与邮箱数据库存储的是否一致
    @Override
    public String registerUser(user newUser) {
        email res = mailRep.findByEmail(newUser.getEmail());
        if (!Objects.equals(res.getMailCode(), newUser.getMailCode())) return "邮箱验证码错误";
        userRep.save(newUser);
        return "成功注册";
    }

    //用户登录，标记login，获取token
    @Override
    public user login(user user) {
        if(user!=null){
            String token= uuID.getUUID();
            user.setToken(token);
            user.setLogin(1);
            userRep.save(user);
        }
        return user;
    }
    @Override
    public user ifExist(String email){ return userRep.findByEmail(email); }
    @Override
    public boolean ifBanned(user res) {if (res.getLogin()<0) return false; return true; }
    @Override
    public user getUserByID(long userID) { return userRep.findByUserID(userID); }
    @Override
    public String resetpassword(String email, String password) {
        user res = ifExist(email);
        if (res != null) {
            res.setPassword(password);
            return "成功";
        }
        else return "用户不存在";
    }

    //更新用户信息
    @Override
    public void saveDetail(user userInfo, user res) {
        res.setBirth(userInfo.getBirth());
        res.setGender(userInfo.getGender());
        res.setPhoneNumber(userInfo.getPhoneNumber());
        res.setUserName(userInfo.getUserName());
        userRep.save(res);
    }
    private static final List<String> SUPPORTED_TYPES = Arrays.asList("image/jpeg", "image/png","image/svg","image/bmp","image/svg");
    private boolean isSupportedType(MultipartFile file) {
        return SUPPORTED_TYPES.contains(file.getContentType());
    }
    //保存用户头像
    @Override
    public Result savePhoto(long userID, MultipartFile file) {
        if (file.isEmpty()) return Result.error(403,"文件为空");        //判断文件是否为空
        if (!isSupportedType(file)) { return Result.error(403,"文件类型不支持"); } //判断文件类型是否是图片
        //用户头像地址为：程序运行地址/uploads/{userID}/{userID}.*
        ApplicationHome home = new ApplicationHome(getClass());
        File jarfile = home.getSource();
        String path = jarfile.getParentFile().toString()+"/uploads/";
        String userPathRoot = path + userID + "/";
        String filepath = userPathRoot + userID + "." + file.getContentType();
        File dest = new File(filepath);

        if (!dest.getParentFile().exists()) {
            if (!dest.getParentFile().mkdirs()) return Result.error(500,"upload failed,mkdirs failed");
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(500,"upload failed " + e.getMessage());
        }
        return Result.success(200,filepath);
    }

    @Override
    public void deleteUser(user res) {
        workRep.deleteByUserID(res.getUserID());
        userRep.delete(res);
        //return Result.success(200,"注销成功");
    }

    @Override
    public void banUser(user res)
    {
        res.setLogin(-1);
        userRep.save(res);
    }
    @Override
    public void logout(user res)
    {
        res.setLogin(0);
        res.setToken(null);
        userRep.save(res);
    }

    @Override
    public List<user> searchUser(String userName, HttpServletResponse response)
    {
        List<user> res = userRep.findByUserName("%"+userName+"%");
        for (user i:res)
        {
            i.setToken(null);
            i.setPassword(null);
            downloadPicture(i.getAvatar(),response);
            File tmp = new File(i.getAvatar());
            i.setAvatar(tmp.getName());
        }
        return res;
    }

    private String downloadPicture(String downloadUrl,HttpServletResponse resp) {
        return downloadPicture(downloadUrl, resp);
    }
}