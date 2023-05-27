package com.example.sharingplatform.entity;

import java.util.List;
import com.example.sharingplatform.entity.work;

public class workResult {
    long resultNumber;
    List<work> workResult;

    public long getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(long resultNumber) {
        this.resultNumber = resultNumber;
    }

    public List<work> getWorkResult() {
        return workResult;
    }

    public void setWorkResult(List<work> workResult) {
        this.workResult = workResult;
    }
}
