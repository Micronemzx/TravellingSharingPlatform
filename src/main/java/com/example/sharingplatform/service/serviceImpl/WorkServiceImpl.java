package com.example.sharingplatform.service.serviceImpl;

import com.example.sharingplatform.entity.user;
import com.example.sharingplatform.entity.work;
import com.example.sharingplatform.service.WorkService;
import com.example.sharingplatform.repository.*;
import com.example.sharingplatform.utils.Result;
import com.example.sharingplatform.utils.uuID;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WorkServiceImpl implements WorkService {
    @Resource
    workRepository workRep;

    @Override
    public void like(work work, user user) {
        work.setLikeNumber(work.getLikeNumber()+1);
    }

    @Override
    public work getWorkByID(long workID) {
        return workRep.findByWorkID(workID);
    }
}
