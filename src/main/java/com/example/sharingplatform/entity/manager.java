package com.example.sharingplatform.entity;

import javax.persistence.*;

@Table(name="manager")
@Entity
public class manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long managerID;
    private String managerPsd;
    private String managerName;

    public long getManagerID() {
        return managerID;
    }

    public void setManagerID(long managerID) {
        this.managerID = managerID;
    }

    public String getManagerPsd() {
        return managerPsd;
    }

    public void setManagerPsd(String managerPsd) {
        this.managerPsd = managerPsd;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}
