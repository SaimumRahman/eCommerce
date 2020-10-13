package com.example.ecommerce.Model;

public class Users {

    private String Name, Passwords, PhoneNumber, image, address;

    public Users() {

    }

    public Users(String name, String passwords, String phoneNumber, String image, String address) {
        Name = name;
        Passwords = passwords;
        PhoneNumber = phoneNumber;
        this.image = image;
        this.address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPasswords() {
        return Passwords;
    }

    public void setPasswords(String passwords) {
        Passwords = passwords;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}