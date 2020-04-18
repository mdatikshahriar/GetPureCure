package com.example.getPureCure.patientPart;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;

import com.example.getPureCure.R;

public class ShowSingleHospitalActivity extends AppCompatActivity {

    private Dialog toastMessageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_hospital);
    }
}
