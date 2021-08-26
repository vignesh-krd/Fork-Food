package com.example.forkandfood;

public class FoodList {

    String dishname;
    int dishprice,dishquantity;

    public FoodList(String dishname, int dishprice, int dishquantity) {
        this.dishname = dishname;
        this.dishprice = dishprice;
        this.dishquantity = dishquantity;
    }

    public FoodList() {
    }

    public String getDishname() {
        return dishname;
    }

    public void setDishname(String dishname) {
        this.dishname = dishname;
    }

    public int getDishprice() {
        return dishprice;
    }

    public void setDishprice(int dishprice) {
        this.dishprice = dishprice;
    }

    public int getDishquantity() {
        return dishquantity;
    }

    public void setDishquantity(int dishquantity) {
        this.dishquantity = dishquantity;
    }
}
