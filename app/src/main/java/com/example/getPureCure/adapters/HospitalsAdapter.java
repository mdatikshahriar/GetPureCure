package com.example.getPureCure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getPureCure.R;
import com.example.getPureCure.objects.Hospital;

import java.util.ArrayList;

public class HospitalsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private ArrayList<Hospital> hospitalsArray;

    public HospitalsAdapter(Context context, ArrayList<Hospital> hospitalsArray) {
        this.context = context;
        this.hospitalsArray = hospitalsArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small_hospital, parent, false);
        return new HospitalsAdapter.HospitalsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Hospital hospital = hospitalsArray.get(position);

        HospitalsAdapter.HospitalsHolder hospitalsHolder = (HospitalsAdapter.HospitalsHolder) holder;

        ImageView hospitalPhotoImageView = hospitalsHolder.hospitalPhotoImageView;
        TextView hospitalNameTextView = hospitalsHolder.hospitalNameTextView;
        TextView hospitalAddressTextView = hospitalsHolder.hospitalAddressTextView;

        hospitalPhotoImageView.setImageResource(R.drawable.default_hospital_picture);

        hospitalNameTextView.setText(hospital.getName());
        hospitalAddressTextView.setText(hospital.getAddress());
    }

    @Override
    public int getItemCount() {
        return hospitalsArray.size();
    }

    private class HospitalsHolder extends RecyclerView.ViewHolder {
        ImageView hospitalPhotoImageView;
        LinearLayout hospitalBodyLinearLayout;
        TextView hospitalNameTextView, hospitalAddressTextView;

        HospitalsHolder(View itemView) {
            super(itemView);

            hospitalPhotoImageView = itemView.findViewById(R.id.item_small_hospital_hospitalPhoto_ImageView);
            hospitalBodyLinearLayout = itemView.findViewById(R.id.item_small_hospital_hospitalBody_LinearLayout);
            hospitalNameTextView = itemView.findViewById(R.id.item_small_hospital_hospitalName_TextView);
            hospitalAddressTextView = itemView.findViewById(R.id.item_small_hospital_hospitalAddress_TextView);

            hospitalBodyLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });
        }
    }
}
