package com.example.sharingplatform.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name="email", uniqueConstraints={@UniqueConstraint(columnNames={"email"})},indexes = {@Index(columnList = "email")})
@Entity
public class email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String mailCode;
    private Date lastUpdateTime;
    private long ownerID;

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMailCode() {
        return mailCode;
    }

    public void setMailCode(String mailCode) {
        this.mailCode = mailCode;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
