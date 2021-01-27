package com.example.getPureCure.doctorPart;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.getPureCure.R;

public class ShowSingleHospitalActivity extends AppCompatActivity {

    private Dialog toastMessageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_hospital);
    }
}
