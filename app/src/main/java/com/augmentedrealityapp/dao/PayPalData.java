package com.augmentedrealityapp.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kavasthi on 9/22/2017.
 */

public class PayPalData {
    private String platform;
    private String id;
    private String state;
    @SerializedName("create_time")
    private String createTime;
    @SerializedName("currency_code")
    private String currencyCode;
    @SerializedName("short_description")
    private String shortDescription;
    private String amount;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
