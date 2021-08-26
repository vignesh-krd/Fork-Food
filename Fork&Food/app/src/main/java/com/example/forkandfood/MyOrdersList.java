package com.example.forkandfood;

public class MyOrdersList {
    String date,time,order_category,total_price;
    int order_num;

    public MyOrdersList() {
    }

    public MyOrdersList(String date, String time, String order_category, String total_price, int order_num) {
        this.date = date;
        this.time = time;
        this.order_category = order_category;
        this.total_price = total_price;
        this.order_num = order_num;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getOrder_category() {
        return order_category;
    }

    public int getOrder_num() {
        return order_num;
    }
}
