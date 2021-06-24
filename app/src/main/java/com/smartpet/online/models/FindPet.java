package com.smartpet.online.models;

import java.io.Serializable;

public class FindPet implements Serializable {
    private final String uid;
    private final String name;
    private final String phoneNum;
    private final String petName;
    private final String petType;
    private final String petPrice;
    private final String petImage;
    private final String petDesc;
    private final String petDate;

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
