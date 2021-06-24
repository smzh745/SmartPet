package com.smartpet.online.models;

import java.io.Serializable;

public class FindShop implements Serializable {
    private final String uid;
    private final String name;
    private final String phonNumber;
    private final String userType;
    private final String shopName;
    private final String shopLocation;

    public FindShop(String uid, String name, String phonNumber,  String shopName, String shopLocation,String userType) {
        this.uid = uid;
        this.name = name;
        this.phonNumber = phonNumber;
        this.userType = userType;
        this.shopName = shopName;
        this.shopLocation = shopLocation;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPhonNumber() {
        return phonNumber;
    }

    public String getUserType() {
        return userType;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopLocation() {
        return shopLocation;
    }
}
