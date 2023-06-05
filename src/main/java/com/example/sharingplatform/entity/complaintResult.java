package com.example.sharingplatform.entity;

import java.util.List;

public class complaintResult {
     long resultNumber;
     List<complaint> reportResult;

    public long getResultNumber() {
        return resultNumber;
    }

    public List<complaint> getReportResult() {
        return reportResult;
    }

    public void setResultNumber(long resultNumber) {
        this.resultNumber = resultNumber;
    }

    public void setReportResult(List<complaint> reportResult) {
        this.reportResult = reportResult;
    }
}
