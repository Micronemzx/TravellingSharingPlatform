package com.example.sharingplatform.entity;

import javax.persistence.*;

@Table(name="place")
@Entity
public class place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private long hotPoint;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getHotPoint() {
        return hotPoint;
    }

    public void setHotPoint(long hotPoint) {
        this.hotPoint = hotPoint;
    }
}
