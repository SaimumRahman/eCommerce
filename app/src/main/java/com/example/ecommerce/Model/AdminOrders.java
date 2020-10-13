package com.example.ecommerce.Model;

public class AdminOrders {

    private String name,Phone_Number, Address,City,Date, Time,Status,Total_Amount;

    public AdminOrders() {
    }

    public AdminOrders(String name, String phone_Number, String address, String city, String date, String time, String status, String total_Amount) {
        this.name = name;
        Phone_Number = phone_Number;
        Address = address;
        City = city;
        Date = date;
        Time = time;
        Status = status;
        Total_Amount = total_Amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTotal_Amount() {
        return Total_Amount;
    }

    public void setTotal_Amount(String total_Amount) {
        Total_Amount = total_Amount;
    }
}
