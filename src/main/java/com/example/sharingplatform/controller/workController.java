package com.example.sharingplatform.controller;

import com.example.sharingplatform.entity.likeLink;
import com.example.sharingplatform.entity.user;
import com.example.sharingplatform.entity.work;
import com.example.sharingplatform.entity.workResult;
import com.example.sharingplatform.service.UserService;
import com.example.sharingplatform.service.WorkService;
import com.example.sharingplatform.utils.Result;
import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/work")
public class workController {
    @Resource
    private WorkService workservice;
    @Resource
    private UserService userservice;
    @GetMapping("/testGet") //测试GET
    public Result testGet(@RequestParam String a) { return Result.success(500,a); }

    @PostMapping("/testPost")   //测试POST
    public Result testPost(@RequestBody String a) { return Result.success(500,a); }

    @PostMapping("/like")       //动态点赞
    public Result likeWorkController(@RequestBody likeLink like,
                                     HttpServletRequest request)
    {
        long userID = like.getUserID();
        long workID = like.getWorkID();
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        user userRes = userservice.getUserByID(userID);
        work workRes = workservice.getWorkByID(workID);
        if (userRes==null||workRes==null) { return Result.error(500,"不存在该用户或该动态"); }
        if (workservice.getLikeLink(workID,userID) != null) { return Result.error(500,"您已点过赞不可重复点赞"); }
        workservice.like(workRes,userID);
        return Result.success(200,"成功");
    }

    @PostMapping("/delete")       //删除动态
    public Result deleteWorkController(@RequestBody long workID,HttpServletRequest request){
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        work res = workservice.getWorkByID(workID);
        if (res!=null) { workservice.deleteWork(res); return Result.success(200,"删除成功"); }
        else return Result.error(404,"动态不存在");
    }

    @GetMapping("/information")     //获取动态详情
    public Result<work> getWorkDetailsController(@RequestParam long workID, HttpServletResponse response){
        work res = workservice.getWorkByID(workID);
        if (res==null) return Result.error(null,404,"动态不存在");
        workservice.sendPicture(res,response);
        return Result.success(res,200,"获取成功");
    }

    @PostMapping(path="/add",consumes = {"multipart/form-data"})        //增添动态
    public Result addWorkController(@RequestPart("work") work workInfo,
                                    @RequestPart("picture") @NotNull MultipartFile[] file,
                                    HttpServletRequest request){
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        for (MultipartFile partFile : file){
            if (partFile.isEmpty()) return Result.error(403,"文件为空");
            if (!workservice.checkPicture(partFile)) return Result.error(403,"文件类型不支持");
        }
        workInfo.setCreateTime(new Date());
        workservice.saveWork(workInfo);
        int cnt=0;
        for (MultipartFile partFile : file)
        {
            cnt++;
            Result res = workservice.savePhoto(workInfo.getWorkID(),workInfo.getUserID(),cnt,partFile);
            if (res.getCode()!=200)
            {
                    workservice.deletePicture(workInfo.getWorkID());
                    workservice.deleteWork(workInfo);
                    return res;
            }
        }
        return Result.success(200,"成功");
    }

    @GetMapping("/search")          //搜索动态标题
    public Result<workResult> searchWorkController(@RequestParam("title") String title,
                                                   @RequestParam("page") int page,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response){
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(null,401,"NeedLogin");
        List<work> res = workservice.searchWork(title,page);
        for (work i : res) workservice.sendPicture(i,response);
        workResult result = new workResult();
        result.setResultNumber(res.size());
        result.setWorkResult(res);
        return Result.success(result,200,"成功");
    }

    @PostMapping("/complaint")      //举报动态
    public Result complaintWorkController(@RequestPart("workID") long workID,
                                          @RequestPart("userID") long userID,
                                          HttpServletRequest request) {
        boolean isLogin=userservice.checkToken(request);
        if(!isLogin) return Result.error(401,"NeedLogin");
        workservice.complaintWork(workID,userID);
        return Result.success(200,"成功");
    }

    @GetMapping("/getList")     //获取话题热度榜单
    public Result<List<String>> getHotWorkListController() {
        return Result.success(workservice.getHotWorkList(),200,"成功");
    }

    @GetMapping("/getPlaceList")    //获取地点榜单
    public Result<List<String>> getHotPlaceListController() {
        return Result.success(workservice.getHotPlaceList(),200,"成功");
    }
}
