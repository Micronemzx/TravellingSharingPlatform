package com.example.sharingplatform.entity;

import java.util.List;

public class reportResult {
    long resultNumber;
    List<complaint> reportResult;

    public void setResultNumber(long resultNumber) {
        this.resultNumber = resultNumber;
    }

    public void setReportResult(List<complaint> reportResult) {
        this.reportResult = reportResult;
    }
}
