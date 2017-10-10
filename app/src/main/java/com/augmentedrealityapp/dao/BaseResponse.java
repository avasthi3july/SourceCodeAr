package com.augmentedrealityapp.dao;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kavasthi on 8/8/2017.
 */

public class BaseResponse<T> {
    private String message;
    @SerializedName("cart_count")
    private int cartCount;
    private boolean status;
    private T data;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }
}
