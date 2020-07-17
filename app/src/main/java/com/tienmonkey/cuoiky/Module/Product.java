package com.tienmonkey.cuoiky.Module;

public class Product {
    private String name, des, price, image, date, time, nameUserPost, category, idProduct;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getNameUserPost() {
        return nameUserPost;
    }

    public void setNameUserPost(String nameUserPost) {
        this.nameUserPost = nameUserPost;
    }

    public Product(String name, String des, String price, String image, String date, String time, String nameUserPost, String category, String idProduct) {
        this.name = name;
        this.des = des;
        this.price = price;
        this.image = image;
        this.date = date;
        this.time = time;
        this.nameUserPost = nameUserPost;
        this.category = category;
        this.idProduct = idProduct;
    }

    public Product() {
    }
}
