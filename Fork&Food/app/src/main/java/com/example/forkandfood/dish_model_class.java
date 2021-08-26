package com.example.forkandfood;

public class dish_model_class {

    String dishname,imageurl;
    int dishprice;

    public dish_model_class() {

    }

    public dish_model_class(String dishname, int dishprice, String imageurl) {
        this.dishname = dishname;
        this.dishprice = dishprice;
        this.imageurl = imageurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getDishname() {
        return dishname;
    }

    public int getDishprice() {
        return dishprice;
    }
}
