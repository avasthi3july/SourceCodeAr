package com.augmentedrealityapp.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kavasthi on 9/2/2017.
 */

public class UserData {
    private String email;
    private String id;
    private String name;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("user_active_status")
    private String userActiveStatus;
    @SerializedName("cart_count")
    private int cartCount;


    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserActiveStatus() {
        return userActiveStatus;
    }

    public void setUserActiveStatus(String userActiveStatus) {
        this.userActiveStatus = userActiveStatus;
    }
}
