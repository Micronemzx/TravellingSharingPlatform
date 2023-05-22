package com.example.sharingplatform.utils;


import java.util.UUID;

public class uuID {
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}