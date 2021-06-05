package com.smartpet.online.models;

public class FindDoctor {
    private final String uid;
    private final String name;
    private final String phoneNum;
    private final String clinicName;
    private final String clinicLocation;
    private final String userType;

    public FindDoctor(String uid, String name, String phoneNum, String clinicName, String clinicLocation, String userType) {
        this.uid = uid;
        this.name = name;
        this.phoneNum = phoneNum;
        this.clinicName = clinicName;
        this.clinicLocation = clinicLocation;
        this.userType = userType;
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

    public String getClinicName() {
        return clinicName;
    }

    public String getClinicLocation() {
        return clinicLocation;
    }

    public String getUserType() {
        return userType;
    }
}
