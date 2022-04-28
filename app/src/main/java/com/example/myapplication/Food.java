package com.example.myapplication;

public class Food {
    String name, expirationDate, type;
    Integer image;
    boolean notification;

    public Food(String name, String expirationDate, String type, Integer image, boolean notification) {
        this.name = name;
        this.expirationDate = expirationDate;
        this.type = type;
        this.image = image;
        this.notification = notification;
    }

    public Food(String name, Integer image){
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }
}
