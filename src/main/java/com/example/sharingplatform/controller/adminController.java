package com.example.sharingplatform.controller;

import com.example.sharingplatform.entity.*;
import com.example.sharingplatform.repository.administratorRepository;
import com.example.sharingplatform.repository.reportRepository;
import com.example.sharingplatform.service.UserService;
import com.example.sharingplatform.service.WorkService;
import com.example.sharingplatform.utils.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/administrator")
public class adminController {

    @Resource
    administratorRepository adminRep;
    @Resource
    WorkService workservice;
    @Resource
    UserService userservice;
    @Resource
    reportRepository reportRep;

    @GetMapping("/login")   //管理员登录
    public Result<String> loginController(@RequestParam("administratorName") String administratorName,
                                  @RequestParam("administratorPsd") String administratorPsd){

        if (Objects.equals(administratorName, "root") && Objects.equals(administratorPsd, "rootpsd_3306"))
            return Result.success("root",200,"成功");
        if (adminRep.findByAdministratorNameAndAdministratorPsd(administratorName,administratorPsd) != null){
            return Result.success(administratorName,200,"成功");
        }
        return Result.error(null,403,"失败");
    }

    @PostMapping("/add")    //添加管理员
    public Result addAdministratorController(@RequestPart("rootName") String rootName,@RequestPart("rootPsd") String rootPsd,
                                             @RequestPart("newName") String newName,@RequestPart("newPsd") String newPsd)
    {
        if (Objects.equals(rootName, "root") && Objects.equals(rootPsd, "rootpsd_3306"))
        {
            administrator entity = new administrator();
            entity.setAdministratorName(newName);
            entity.setAdministratorPsd(newPsd);
            adminRep.save(entity);
            return Result.success(200,"成功");
        }
        return Result.error(403,"无权限");
    }

    @DeleteMapping("/deleteAdmin")  //删除管理员
    public Result deleteAdministratorController(@RequestPart("rootName") String rootName,@RequestPart("rootPsd") String rootPsd,
                                             @RequestPart("deleteName") String name,@RequestPart("deletePsd") String psd)
    {
        if (Objects.equals(rootName, "root") && Objects.equals(rootPsd, "rootpsd_3306"))
        {
            administrator entity = adminRep.findByAdministratorNameAndAdministratorPsd(name,psd);
            if (entity == null) return Result.error(400,"失败，删除用户不存在");
            adminRep.delete(entity);
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
        report.setStatus(1);
        reportRep.save(report);
        workservice.deleteWork(res);
        return Result.success(200,"成功");
    }

    @PostMapping("/saveWork")
    public Result saveWorkController(@RequestBody complaint report) {
        long workID = report.getWorkID();
        work res = workservice.getWorkByID(workID);
        if (res==null) return Result.error(404,"动态不存在");
        report.setStatus(2);
        reportRep.save(report);
        workservice.deleteWork(res);
        return Result.success(200,"成功");
    }
    @GetMapping("/report")
    public Result<reportResult> getWorkReport()
    {
        List<complaint> res = reportRep.findByStatusOrderByIdAsc(0);
        reportResult result = new reportResult();
        result.setResultNumber(res.size());
        result.setReportResult(res);
        return Result.success(result,200,"成功");
    }
}
