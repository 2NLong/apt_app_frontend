package com.ptithcm.apt.models.profileuser;


public class Apartment {
    private String name;
    private String status;
    private int iconResId;

    public Apartment(String name, String status, int iconResId) {
        this.name = name;
        this.status = status;
        this.iconResId = iconResId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public int getIconResId() {
        return iconResId;
    }
}
