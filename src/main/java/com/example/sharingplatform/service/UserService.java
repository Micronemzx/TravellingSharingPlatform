package com.example.sharingplatform.service;

import com.example.sharingplatform.entity.user;
import com.example.sharingplatform.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    String sendMailCode(String email) throws MessagingException;
    String registerUser(user newUser);
    String sendMailCodeForRegister(String mail) throws MessagingException;

    user login(user user);

    user ifExist(String email);
    user getUserByID(long userID);
    String resetpassword(String email, String password);

    void saveDetail(user userInfo, user res);

    boolean checkToken(HttpServletRequest request);

    Result savePhoto(long userID, MultipartFile file);

    Result deleteUser(user res);

    void logout(user res);

    List<user> searchUser(String userName, HttpServletResponse response);
}
