package com.example.sharingplatform.service;

import com.example.sharingplatform.entity.user;
import com.example.sharingplatform.entity.work;
import com.example.sharingplatform.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;


public interface WorkService {
    void like(work work, user user);

    work getWorkByID(long workID);
}
