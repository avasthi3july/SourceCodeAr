package com.augmentedrealityapp.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kavasthi on 8/19/2017.
 */

public class ProductData implements Serializable{
    private String id;
    private String name;
    private String sku;
    private String price;
    @SerializedName("special_price")
    private String specialPrice;
    @SerializedName("product_name")
    private String productName;
    private String qty;
    private String description;
    @SerializedName("ar_image")
    private String arImage;
    private String image;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArImage() {
        return arImage;
    }

    public void setArImage(String arImage) {
        this.arImage = arImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /*public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ProductData createFromParcel(Parcel in) {
            return new ProductData(in);
        }

        public ProductData[] newArray(int size) {
            return new ProductData[size];
        }
    };
    public ProductData(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
        this.sku =  in.readString();
        this.price =  in.readString();
        this.specialPrice =  in.readString();
        this.description =  in.readString();
        this.arImage =  in.readString();
        this.qty =  in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.sku);
        dest.writeString(this.price);
        dest.writeString(this.specialPrice);
        dest.writeString(this.description);
        dest.writeString(this.arImage);
        dest.writeString(this.qty);
    }*/
}
