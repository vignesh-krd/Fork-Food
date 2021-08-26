package com.example.forkandfood;

public class CustomerDetails {
    String name,mobile_number,date,time,members,table_chioce,order_category;

    public CustomerDetails() {
    }

    public CustomerDetails(String name, String mobile_number, String date, String time,String order_category) {
        this.name = name;
        this.mobile_number = mobile_number;
        this.date = date;
        this.time = time;
        this.order_category = order_category;
    }

    public CustomerDetails(String name, String date, String time, String members, String table_chioce, String order_category) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.members = members;
        this.table_chioce = table_chioce;
        this.order_category = order_category;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getTable_chioce() {
        return table_chioce;
    }

    public void setTable_chioce(String table_chioce) {
        this.table_chioce = table_chioce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrder_category() {
        return order_category;
    }

    public void setOrder_category(String order_category) {
        this.order_category = order_category;
    }
}
