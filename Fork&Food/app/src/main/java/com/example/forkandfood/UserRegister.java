package com.example.forkandfood;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRegister {
    public String name, mail, mobile_no;

    public UserRegister () {
    }

    public UserRegister(String name, String mail, String mobile_no) {
        this.name = name;
        this.mail = mail;
        this.mobile_no = mobile_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

}