package com.example.projrcttunisiepromo.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Product {
    private String productId;
    private String productname;
    private double productprice;
    private String productimageUrl;

    // Constructor, getters, and setters

    public Product(String productId, String productName, double price, String imageUrl) {
        this.productId = productId;
        this.productname = productName;
        this.productprice = price;
        this.productimageUrl = imageUrl;
    }


    public Product() {
    }

    public Product(String productName, double price, String imageUrl) {
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
}
