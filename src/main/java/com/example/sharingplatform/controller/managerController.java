package com.example.sharingplatform.controller;

import com.example.sharingplatform.entity.*;
import com.example.sharingplatform.repository.managerRepository;
import com.example.sharingplatform.repository.reportRepository;
import com.example.sharingplatform.service.NotificationService;
import com.example.sharingplatform.service.UserService;
import com.example.sharingplatform.service.WorkService;
import com.example.sharingplatform.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/manager")
public class managerController {

    @Resource
    managerRepository managerRep;
    @Resource
    WorkService workservice;
    @Resource
    UserService userservice;
    @Resource
    reportRepository reportRep;
    @Resource
    NotificationService notificationservice;

    @GetMapping("/login")   //管理员登录
    public Result<String> loginController(@RequestParam("managerName") String managerName,
                                  @RequestParam("managerPsd") String managerPsd){

        if (Objects.equals(managerName, "root") && Objects.equals(managerPsd, "rootpsd_3306"))
            return Result.success("root",200,"成功");
        if (managerRep.findByManagerNameAndManagerPsd(managerName,managerPsd) != null){
            return Result.success(managerName,200,"成功");
        }
        return Result.error(null,403,"失败");
    }

    @PostMapping("/add")    //添加管理员
    public Result addManagerController(@RequestPart("rootName") String rootName,@RequestPart("rootPsd") String rootPsd,
                                             @RequestPart("newName") String newName,@RequestPart("newPsd") String newPsd)
    {
        if (Objects.equals(rootName, "root") && Objects.equals(rootPsd, "rootpsd_3306"))
        {
            manager entity = new manager();
            entity.setManagerName(newName);
            entity.setManagerPsd(newPsd);
            managerRep.save(entity);
            return Result.success(200,"成功");
        }
        return Result.error(403,"无权限");
    }

    @DeleteMapping("/deletemanager")  //删除管理员
    public Result deleteManagerController(@RequestPart("rootName") String rootName,@RequestPart("rootPsd") String rootPsd,
                                             @RequestPart("deleteName") String name,@RequestPart("deletePsd") String psd)
    {
        if (Objects.equals(rootName, "root") && Objects.equals(rootPsd, "rootpsd_3306"))
        {
            manager entity = managerRep.findByManagerNameAndManagerPsd(name,psd);
            if (entity == null) return Result.error(400,"失败，删除用户不存在");
            managerRep.delete(entity);
            return Result.success(200,"成功");
        }
        return Result.error(403,"无权限");
    }

    @DeleteMapping("/deleteUser")   //注销账号
    public Result deleteUserController(@RequestPart long userID) {
        user res = userservice.getUserByID(userID);
        if (res == null) return Result.error(404,"用户不存在");
        userservice.deleteUser(res);
        return Result.success(200,"用户已删除");
    }

    @PostMapping("/ban")    //封禁账号
    public Result banUserController(@RequestBody long userID){
        user res = userservice.getUserByID(userID);
        if (res == null) return Result.error(404,"用户不存在");
        userservice.banUser(res);
        return Result.success(200,"成功");
    }

    @PostMapping("/deleteWork") //举报成功,删除作品
    public Result deleteWorkController(@RequestBody complaint report){
        long workID = report.getWorkID();
        work res = workservice.getWorkByID(workID);
        if (res==null) return Result.error(404,"动态不存在");
        List<complaint> same = reportRep.findByWorkID(workID);
        for (complaint i : same) {
            i.setStatus(1);
            reportRep.save(i);
            notificationservice.sendNotification(i.getUserID(),"您所举报的动态: "+i.getTitle()+"已被核实为违规，官方已对该动态删除处理，并警告该动态作者，感谢您对平台的贡献。",1);
        }
        notificationservice.sendNotification(res.getUserID(),"您的动态: "+res.getTitle()+"涉嫌违规，平台已对该动态删除，若有异议请联系平台客服",1);
        workservice.deleteWork(res);
        return Result.success(200,"成功");
    }

    @PostMapping("/saveWork")   //举报失败，保留作品
    public Result saveWorkController(@RequestBody complaint report) {
        long workID = report.getWorkID();
        work res = workservice.getWorkByID(workID);
        if (res==null) return Result.error(404,"动态不存在");
        List<complaint> same = reportRep.findByWorkID(workID);
        for (complaint i : same) {
            i.setStatus(2);
            reportRep.save(i);
            notificationservice.sendNotification(i.getUserID(),"您所举报的动态: "+i.getTitle()+"并无违规现象，感谢您对平台的贡献，我们将持续关注该动态的消息。",1);
        }
        return Result.success(200,"成功");
    }
    @GetMapping("/report")  //获取举报列表
    public Result<reportResult> getWorkReport()
    {
        List<complaint> res = reportRep.findByStatusOrderByWorkIDAsc(0);
        if (res.size()==0) return Result.success(null,200,"列表为空");
        reportResult result = new reportResult();
        result.setResultNumber(res.size());
        List<complaint> dist = new ArrayList<>();
        dist.add(res.get(0));
        for (int i=1;i<res.size();++i) if (res.get(i).getWorkID()!=res.get(i-1).getWorkID()) dist.add(res.get(i));
        result.setReportResult(dist);
        return Result.success(result,200,"成功");
    }
}
