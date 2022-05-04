package com.example.myapplication;

public class Food {
    String name, xMonth, xDay, xYear, type;
    Integer image;
    boolean notification;

    public Food(String name, String xMonth, String xDay, String xYear, String type, Integer image, boolean notification) {
        this.name = name;
        this.xMonth = xMonth;
        this.xDay = xDay;
        this.xYear = xYear;
        this.type = type;
        this.image = image;
        this.notification = notification;
    }

    public String getxMonth() {
        return xMonth;
    }

    public void setxMonth(String xMonth) {
        this.xMonth = xMonth;
    }

    public String getxDay() {
        return xDay;
    }

    public void setxDay(String xDay) {
        this.xDay = xDay;
    }

    public String getxYear() {
        return xYear;
    }

    public void setxYear(String xYear) {
        this.xYear = xYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
