package com.augmentedrealityapp.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kavasthi on 9/1/2017.
 */

public class ProfileData {
    private String name;
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
