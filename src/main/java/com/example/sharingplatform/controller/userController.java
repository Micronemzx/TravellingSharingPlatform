package com.example.sharingplatform.controller;

import com.example.sharingplatform.entity.*;
import com.example.sharingplatform.service.workService;
import com.example.sharingplatform.service.userService;
import com.example.sharingplatform.utils.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class userController {

    @Resource
    private workService workservice;

    @Resource
    private userService userservice;

    @GetMapping("/testGet")
    public Result testGet(@RequestParam String a) { return Result.success(500,a); }

    @PostMapping("/testPost")
    public Result testPost(@RequestBody String a) { return Result.success(500,a); }

    @GetMapping("/sendmailcode")
    public Result sendmailcode(@RequestParam String mail)
    {

    }

    @PostMapping("/register")
    public Result register(@RequestBody user newUser)
    {

    }

    @GetMapping("login")
    public Result login(@RequestPart("email") String mail,@RequestPart("password") String password )
    {

    }

}
