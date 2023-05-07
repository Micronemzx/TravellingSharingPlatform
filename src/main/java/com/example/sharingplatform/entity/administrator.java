package com.example.sharingplatform.entity;

import javax.persistence.*;

@Table(name="administrator")
@Entity
public class administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long administratorID;
    private String administratorPsd;
    private String AdministratorName;

    public long getAdministratorID() {
        return administratorID;
    }

    public void setAdministratorID(long administratorID) {
        this.administratorID = administratorID;
    }

    public String getAdministratorPsd() {
        return administratorPsd;
    }

    public void setAdministratorPsd(String administratorPsd) {
        this.administratorPsd = administratorPsd;
    }

    public String getAdministratorName() {
        return AdministratorName;
    }

    public void setAdministratorName(String administratorName) {
        AdministratorName = administratorName;
    }
}
