package com.example.sharingplatform.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name="user")
@Entity
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;
    private String userName;
    private String password;
    private String email;
    private String mailcode;
    private String gender;
    private String phoneNumbee;
    private Date birth;
    private String photoPath;
    private int workNumber;
}
