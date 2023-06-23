package com.company.petadoptionapp;


public class UserModel {


    String userName, userMobile, userPassword , userMail, image;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UserModel(String userName, String userMobile, String userPassword, String userMail, String image) {
        this.userName = userName;
        this.userMobile = userMobile;
        this.userPassword = userPassword;
        this.userMail = userMail;
        this.image = image;
    }

    public UserModel(String name, String userName, String userMobile) {
    }
}

