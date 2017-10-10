package com.augmentedrealityapp.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kavasthi on 8/9/2017.
 */

public class CatData {
    private String id;
    @SerializedName("tax_category")
    private String taxCategory;
    @SerializedName("cat_image")
    private String catImage;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(String taxCategory) {
        this.taxCategory = taxCategory;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }
}
