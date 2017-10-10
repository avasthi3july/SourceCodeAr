package com.augmentedrealityapp.dao;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kavasthi on 9/6/2017.
 */

public class CartData implements Serializable{
    @SerializedName("product_id")
    private String productId;
    private int qty;
    private int price;
    @SerializedName("product_detail")
    private ProductData productData;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }
}
