package com.example.getPureCure.objects;

import com.example.getPureCure.objects.Doctor;

import java.util.ArrayList;

public class Hospital {
    private String id, name, rating, category, address, contact, openHour;
    private ArrayList<Doctor> doctors;
    private ArrayList<String> schedules, facilities, tests;

    public Hospital(String id, String name, String category, String address, String contact, String openHour, ArrayList<String> facilities, ArrayList<String> tests) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.address = address;
        this.contact = contact;
        this.openHour = openHour;
        this.facilities = facilities;
        this.tests = tests;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getOpenHour() {
        return openHour;
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public ArrayList<String> getSchedules() {
        return schedules;
    }

    public ArrayList<String> getFacilities() {
        return facilities;
    }

    public ArrayList<String> getTests() {
        return tests;
    }
}
