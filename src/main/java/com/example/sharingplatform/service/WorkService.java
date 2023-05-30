package com.example.sharingplatform.service;

import com.example.sharingplatform.entity.likeLink;
import com.example.sharingplatform.entity.user;
import com.example.sharingplatform.entity.work;
import com.example.sharingplatform.utils.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface WorkService {

    void like(work work, long userID);

    likeLink getLikeLink(long workID, long userID);

    work getWorkByID(long workID);

    void deleteWork(work res);

    String sendPicture(work res, HttpServletResponse response);

    boolean checkPicture(MultipartFile file);

    void saveWork(work work);

    Result savePhoto(long workID, long userID, int cnt, MultipartFile partFile);

    void deletePicture(long workID);
    List<work> searchWork(String title,int page);

    void complaintWork(long workID, long userID);

    List<String> getHotWorkList();

    List<String> getHotPlaceList();
}
