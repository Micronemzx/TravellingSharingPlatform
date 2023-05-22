package com.example.sharingplatform.service;

import com.example.sharingplatform.entity.user;

import javax.mail.MessagingException;

public interface UserService {
    String sendMailCode(String email) throws MessagingException;
    String registerUser(user newUser);
    String sendMailCodeForRegister(String mail) throws MessagingException;

    user login(user user);

    user ifExist(String email);
}
