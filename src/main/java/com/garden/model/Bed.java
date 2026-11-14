package com.garden.model;

import com.garden.enums.BedType;
import com.garden.enums.Sun;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "beds")
public class Bed {
    @Id
    private String id;
    private String userId;
    private String name;
    private BedType bedType;
    private Sun sunExposure;
    private int areaSqFt;

    public Bed() {
        //No argument constructor
    }

    public Bed(String userId, String name, BedType bedType, Sun sunExposure, int areaSqFt) {
        this.userId = userId;
        this.name = name;
        this.bedType = bedType;
        this.sunExposure = sunExposure;
        this.areaSqFt = areaSqFt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BedType getBedType() {
        return bedType;
    }

    public void setBedType(BedType bedType) {
        this.bedType = bedType;
    }

    public Sun getSunExposure() {
        return sunExposure;
    }

    public void setSunExposure(Sun sunExposure) {
        this.sunExposure = sunExposure;
    }

    public int getAreaSqFt() {
        return areaSqFt;
    }

    public void setAreaSqFt(int areaSqFt) {
        this.areaSqFt = areaSqFt;
    }
}
