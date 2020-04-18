package com.example.getPureCure.objects;

import java.util.ArrayList;

public class Doctor {
    private String name, email, phone, photoUri, userId, category, about, contact, bmdcCode, chamberAddress, visitingTime;
    private ArrayList<String> Degrees;
    private Boolean isVerified;

    public Doctor(String name, String photoUri, String userId, String category) {
        this.name = name;
        this.photoUri = photoUri;
        this.userId = userId;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public String getUserId() {
        return userId;
    }

    public String getCategory() {
        return category;
    }

    public String getAbout() {
        return about;
    }

    public String getContact() {
        return contact;
    }

    public String getBmdcCode() {
        return bmdcCode;
    }

    public String getChamberAddress() {
        return chamberAddress;
    }

    public String getVisitingTime() {
        return visitingTime;
    }

    public ArrayList<String> getDegrees() {
        return Degrees;
    }

    public Boolean getVerified() {
        return isVerified;
    }
}
