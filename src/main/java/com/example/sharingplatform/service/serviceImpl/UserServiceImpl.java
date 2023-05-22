package com.example.sharingplatform.service.serviceImpl;

import com.example.sharingplatform.entity.email;
import com.example.sharingplatform.entity.user;
import com.example.sharingplatform.service.UserService;
import com.example.sharingplatform.repository.*;
import com.example.sharingplatform.utils.uuID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private JavaMailSender mailSender;
    @Resource
    private mailRepository mailRep;
    @Resource
    private userRepository userRep;
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
    @Override
    public String registerUser(user newUser) {
        email res = mailRep.findByEmail(newUser.getEmail());
        if (!Objects.equals(res.getMailCode(), newUser.getMailCode())) return "邮箱验证码错误";
        userRep.save(newUser);
        return "成功注册";
    }

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
    public user ifExist(String email){
        user res= userRep.findByEmail(email);
        return res;
    }
}