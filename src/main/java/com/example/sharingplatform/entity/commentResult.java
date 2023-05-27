package com.example.sharingplatform.entity;

import java.util.List;

public class commentResult {
    long resultNumber;
    List<comment> workResult;

    public long getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(long resultNumber) {
        this.resultNumber = resultNumber;
    }

    public List<comment> getWorkResult() {
        return workResult;
    }

    public void setWorkResult(List<comment> workResult) {
        this.workResult = workResult;
    }
}
