package com.augmentedrealityapp.retrofit;

/**
 * Created by prasharma on 1/11/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIError {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getResult() {
        return message;
    }

    public void setResult(String result) {
        this.message = result;
    }

}
