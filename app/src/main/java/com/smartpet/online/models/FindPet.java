package com.smartpet.online.models;

import java.io.Serializable;

public class FindPet implements Serializable {
    private String uid;
    private String name;
    private String phoneNum;
    private String petName;
    private String petType;
    private String petPrice;
    private String petImage;
    private String petDesc;
    private String petDate;

    public FindPet(String uid, String name, String phoneNum, String petName, String petType, String petPrice, String petImage, String petDesc, String petDate) {
        this.uid = uid;
        this.name = name;
        this.phoneNum = phoneNum;
        this.petName = petName;
        this.petType = petType;
        this.petPrice = petPrice;
        this.petImage = petImage;
        this.petDesc = petDesc;
        this.petDate = petDate;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getPetName() {
        return petName;
    }

    public String getPetType() {
        return petType;
    }

    public String getPetPrice() {
        return petPrice;
    }

    public String getPetImage() {
        return petImage;
    }

    public String getPetDesc() {
        return petDesc;
    }

    public String getPetDate() {
        return petDate;
    }
}
