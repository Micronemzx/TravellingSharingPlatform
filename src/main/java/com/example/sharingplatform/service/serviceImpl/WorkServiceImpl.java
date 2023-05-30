package com.example.sharingplatform.service.serviceImpl;

import com.example.sharingplatform.entity.*;
import com.example.sharingplatform.service.WorkService;
import com.example.sharingplatform.repository.*;
import com.example.sharingplatform.utils.Result;

import org.hibernate.jdbc.Work;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jdk.nashorn.internal.objects.NativeMath.min;

@Service
public class WorkServiceImpl implements WorkService {
    @Resource
    workRepository workRep;
    @Resource
    pictureRepository picRep;
    @Resource
    reportRepository reportRep;
    @Resource
    likeRepository likeRep;
    @Resource
    placeRepository placeRep;

    @Override
    public void like(work work,long userID) {
        work.setLikeNumber(work.getLikeNumber()+1);
        work.setHotPoint(work.getHotPoint()+1);
        workRep.save(work);
        likeLink relation = new likeLink();
        relation.setWorkID(work.getWorkID());
        relation.setUserID(userID);
        likeRep.save(relation);
    }
    @Override
    public likeLink getLikeLink(long workID, long userID) { return likeRep.findByWorkIDAndUserID(workID,userID); }
    @Override
    public List<String> getHotWorkList() {
        Sort sort = Sort.by(Sort.Direction.DESC,"hotPoint");
        PageRequest pr = PageRequest.of(0, 10, sort);
        List<place> res = placeRep.findAll(pr).getContent();
        List<String> result = new ArrayList<>();
        for (place i : res) { result.add(i.getName()); }
        return result;
    }
    @Override
    public List<String> getHotPlaceList() {
        Sort sort = Sort.by(Sort.Direction.DESC,"hotPoint");
        PageRequest pr = PageRequest.of(0, 10, sort);
        List<work> res = workRep.findAll(pr).getContent();
        List<String> result = new ArrayList<>();
        for (work i : res) { result.add(i.getTitle()); }
        return result;
    }
    @Override
    public work getWorkByID(long workID) {
        return workRep.findByWorkID(workID);
    }

    @Override
    public void deleteWork(work res) { workRep.delete(res); }

    private static final List<String> SUPPORTED_TYPES = Arrays.asList("image/jpeg", "image/png","image/svg","image/bmp","image/svg");

    public boolean checkPicture(MultipartFile file) { return SUPPORTED_TYPES.contains(file.getContentType());  }
    public void saveWork(work workInfo) { workRep.save(workInfo); }

    @Override
    public Result savePhoto(long workID, long userID, int cnt, MultipartFile partFile) {
        ApplicationHome home = new ApplicationHome(getClass());
        File jarfile = home.getSource();
        String path = jarfile.getParentFile().toString()+"/uploads/";
        String userPathRoot = path + userID + "/" + workID + "/";
        String filepath = userPathRoot + userID + "_" + workID + "_" + cnt + "." + partFile.getContentType();
        File dest = new File(filepath);

        if (!dest.getParentFile().exists()) {
            if (!dest.getParentFile().mkdirs()) return Result.error(500,"upload failed,mkdirs failed");
        }
        try {
            partFile.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(500,"upload failed " + e.getMessage());
        }
        picture pic = new picture();
        pic.setSavePath(filepath);
        pic.setWorkID(workID);
        picRep.save(pic);
        return Result.success(200,filepath);
    }

    @Override
    public void deletePicture(long workID) {
        picRep.deleteByWorkID(workID);
    }

    @Override
    public List<work> searchWork(String title,int page)
    {
        List<work> res = workRep.findByTitle(title);
        if (res.size()<=(page-1)*10) return null;
        List<work> result = new ArrayList<>();
        for (int i=(page-1)*10;i<min(res.size()<page*10);++i)
            result.add(res.get(i));
        return result;
    }

    @Override
    public void complaintWork(long workID, long userID) {
        complaint res = reportRep.findByWorkIDAndUserID(workID,userID);
        if (res==null)
        {
            res = new complaint();
            res.setWorkID(workID);
            res.setUserID(userID);
            res.setTitle(getWorkByID(workID).getTitle());
            reportRep.save(res);
        }
    }

    @Override
    public String sendPicture(work work,HttpServletResponse response) {
        List<picture> res = picRep.findByWorkID(work.getWorkID());
        StringBuilder result = new StringBuilder();
        int cnt = 0;
        for (picture i : res)
        {
            cnt++;
            String ans = downloadPicture(i.getSavePath(),response);
            result.append("picture ").append(cnt).append(":").append(ans).append(" ");
        }
        //return "successful";
        return result.toString();
    }

    static String downloadPicture(String downloadUrl, HttpServletResponse resp) {
        if (!downloadUrl.isEmpty()) {
            File file = new File(downloadUrl);
            if (!file.exists()) {
                return "file is not exist";
            }
            resp.reset();
            resp.setContentType("application/octet-stream");
            resp.setCharacterEncoding("utf-8");
            resp.setContentLength((int)file.length());
            resp.setHeader("Content-Disposition","attachment;filename="+file.getName());
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null;
            try {
                os =resp.getOutputStream();
                bis = new BufferedInputStream(Files.newInputStream(file.toPath()));
                int i = bis.read(buff);
                while (i!=-1) {
                    os.write(buff,0,buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
            finally {
                if (bis != null) {
                    try {
                        bis.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "successful";
        }
        else {
            return "file is not exist";
        }
    }
}
