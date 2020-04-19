package com.example.getPureCure.objects;

import java.util.ArrayList;

public class TreatmentHistory {
    private String id, doctorName, date;
    private ArrayList<String> diseases, tests, medicines;

    public TreatmentHistory(String id, String doctorName, String date, ArrayList<String> diseases, ArrayList<String> tests, ArrayList<String> medicines) {
        this.id = id;
        this.doctorName = doctorName;
        this.date = date;
        this.diseases = diseases;
        this.tests = tests;
        this.medicines = medicines;
    }

    public String getId() {
        return id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getDiseases() {
        return diseases;
    }

    public ArrayList<String> getTests() {
        return tests;
    }

    public ArrayList<String> getMedicines() {
        return medicines;
    }
}
