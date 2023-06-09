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
import java.io.IOException;
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
    @GetMapping("/checkroot")
    public Result checkRootController(@RequestParam String rootName,@RequestParam String rootPsd){
        if (Objects.equals(rootName, "root") && Objects.equals(rootPsd, "rootpsd_3306"))
            return Result.success(true,200,"认证成功");
        return Result.error(false,500,"无权限");
    }
    @PostMapping("/add")    //添加管理员
    public Result addManagerController(@RequestBody manager newManager)
    {
            managerRep.save(newManager);
            return Result.success(200,"成功");
    }

    @PostMapping("/deletemanager")  //删除管理员
    public Result deleteManagerController(@RequestBody manager manager)
    {
        manager entity = managerRep.findByManagerNameAndManagerPsd(manager.getManagerName(), manager.getManagerPsd());
        if (entity == null) return Result.error(400,"失败，删除用户不存在");
        managerRep.delete(entity);
        return Result.success(200,"成功");
    }

    @PostMapping("/deleteUser")   //注销账号
    public Result deleteUserController(@RequestBody long userID) {
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
    public Result<complaintResult> getWorkReport()
    {
        List<complaint> res = reportRep.findByStatusOrderByWorkIDAsc(0);
        if (res.size()==0) return Result.success(null,200,"列表为空");
        System.out.println(res.size());
        complaintResult ans = new complaintResult();
        ans.setResultNumber(res.size());
        ans.setReportResult(res);
        List<complaint> tmp = new ArrayList<>();
        try {
            tmp.add(res.get(0));
            for (int i = 1; i < res.size(); ++i)
                if (res.get(i).getWorkID() != res.get(i - 1).getWorkID()) tmp.add(res.get(i));
            ans.setReportResult(tmp);
        } finally {
            System.out.println(tmp.size());
        }
        return Result.success(ans,200,"成功");
    }
}
