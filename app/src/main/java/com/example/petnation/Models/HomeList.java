package com.example.petnation.Models;

import android.widget.TextView;

public class HomeList {
    String Address,Description,image,name,phone;

    public HomeList(String address, String description, String image, String name, String phone) {
        Address = address;
        Description = description;
        this.image = image;
        this.name = name;
        this.phone = phone;
    }

    public HomeList() {
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
