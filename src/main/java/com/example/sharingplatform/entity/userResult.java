package com.example.sharingplatform.entity;

import java.util.List;
import com.example.sharingplatform.entity.user;

public class userResult {
    long resultNumber;
    List<user> userResult;

    public long getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(long resultNumber) {
        this.resultNumber = resultNumber;
    }

    public List<user> getUserResult() {
        return userResult;
    }

    public void setUserResult(List<user> userResult) {
        this.userResult = userResult;
    }
}
