package com.example.sharingplatform.entity;

import java.util.List;

public class notificationResult {
    int resultNumber;
    List<notification> notificationResult;

    public int getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(int resultNumber) {
        this.resultNumber = resultNumber;
    }

    public List<notification> getNotificationResult() {
        return notificationResult;
    }

    public void setNotificationResult(List<notification> notificationResult) {
        this.notificationResult = notificationResult;
    }
}
