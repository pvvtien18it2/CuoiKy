package com.tienmonkey.cuoiky.Module;

public class Orders {
    private String id, name, price, date, time, address, phone, nameUserBuy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNameUserBuy(String nameUserBuy) {
        this.nameUserBuy = nameUserBuy;
    }

    public String getNameUserBuy() {
        return nameUserBuy;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Orders(String id, String name, String price, String date, String time, String address, String phone, String nameUserBuy) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.time = time;
        this.address = address;
        this.phone = phone;
        this.nameUserBuy = nameUserBuy;
    }

    public Orders() {
    }
}
