package com.example.projrcttunisiepromo.Model;

import android.widget.EditText;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {
    private String productId;
    private String productname;
    private double productprice;

    private double productpromo;

    private String Description;
    private String productimageUrl;


    // Constructor, getters, and setters

    public Product(String productId, String productName, double price, double productpromo, String imageUrl, String description) {
        this.productId = productId;
        this.productname = productName;
        this.productprice = price;
        this.productimageUrl = imageUrl;
        this.Description = description;
    }


    public Product() {
    }

    public Product(String productName, double price , double productpromo, String imageUrl , String description) {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return productname;
    }

    public void setName(String name) {
        this.productname = name;
    }
    public double getProductprice() {
        return productprice;
    }

    public void setPrice(double price) {
        this.productprice = price;
    }
    public String getImageUrl() {
        return productimageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.productimageUrl = imageUrl;
    }

    public double getProductpromo() {
        return productpromo;
    }

    public void setProductpromo(double productpromo) {
        this.productpromo = productpromo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
