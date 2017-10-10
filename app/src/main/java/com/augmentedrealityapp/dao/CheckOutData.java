package com.augmentedrealityapp.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kavasthi on 9/26/2017.
 */

public class CheckOutData {
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("category_id")
    private String categoryId;
    @SerializedName("sub_total")
    private String subTotal;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }
}
