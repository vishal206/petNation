package com.example.petnation.Models;

public class OrderList {
    String Brand,Name,amount,quantity;

    public OrderList(String brand, String name, String amount, String quantity) {
        Brand = brand;
        Name = name;
        this.amount = amount;
        this.quantity = quantity;
    }

    public OrderList() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
