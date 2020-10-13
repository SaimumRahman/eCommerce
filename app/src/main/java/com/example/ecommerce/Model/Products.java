package com.example.ecommerce.Model;

public class Products {
    private String product_name, description, price, Image, time, category, pid, date, product_state;

    public Products() {

    }

    public Products(String product_name, String description, String price, String image, String time, String category, String pid, String date, String product_state) {
        this.product_name = product_name;
        this.description = description;
        this.price = price;
        Image = image;
        this.time = time;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.product_state = product_state;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduct_state() {
        return product_state;
    }

    public void setProduct_state(String product_state) {
        this.product_state = product_state;
    }
}