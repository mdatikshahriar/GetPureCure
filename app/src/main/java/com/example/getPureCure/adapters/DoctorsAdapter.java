package com.example.getPureCure.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getPureCure.R;
import com.example.getPureCure.objects.Doctor;
import com.example.getPureCure.patientPart.ShowSingleDoctorActivity;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DoctorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    private ArrayList<Doctor> doctorsArray;

    public DoctorsAdapter(Context context, ArrayList<Doctor> doctorsArray) {
        this.context = context;
        this.doctorsArray = doctorsArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small_doctor, parent, false);
        return new DoctorsAdapter.DoctorsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Doctor doctor = doctorsArray.get(position);

        DoctorsAdapter.DoctorsHolder doctorsHolder = (DoctorsAdapter.DoctorsHolder) holder;

        CircularImageView profilePhotoCircularImageView = doctorsHolder.profilePhotoCircularImageView;
        TextView doctorNameTextView = doctorsHolder.doctorNameTextView;
        TextView doctorSpecialtiesTextView = doctorsHolder.doctorSpecialtiesTextView;

        if(doctor.getPhotoUri() == null || doctor.getPhotoUri().isEmpty()){
            Picasso.get().load(R.drawable.ic_account)
                    .centerCrop()
                    .fit()
                    .error(R.drawable.ic_account)
                    .into(profilePhotoCircularImageView);
        }else {
            Picasso.get().load(doctor.getPhotoUri())
                    .centerCrop()
                    .fit()
                    .error(R.drawable.ic_account)
                    .into(profilePhotoCircularImageView);
        }

        doctorNameTextView.setText(doctor.getName());
        doctorSpecialtiesTextView.setText(doctor.getCategory());

        doctorsHolder.userId = doctor.getUserId();
    }

    @Override
    public int getItemCount() {
        return doctorsArray.size();
    }

    private class DoctorsHolder extends RecyclerView.ViewHolder {
        private CircularImageView profilePhotoCircularImageView;
        private LinearLayout doctorBodyLinearLayout;
        private TextView doctorNameTextView, doctorSpecialtiesTextView;

        private String userId;

        DoctorsHolder(View itemView) {
            super(itemView);

            profilePhotoCircularImageView = itemView.findViewById(R.id.item_small_doctor_profilePhoto_CircularImageView);
            doctorBodyLinearLayout = itemView.findViewById(R.id.item_small_doctor_doctorBody_LinearLayout);
            doctorNameTextView = itemView.findViewById(R.id.item_small_doctor_doctorName_TextView);
            doctorSpecialtiesTextView = itemView.findViewById(R.id.item_small_doctor_doctorSpecialties_TextView);

            userId = null;

            doctorBodyLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowSingleDoctorActivity.class);
                    intent.putExtra("user_id", userId);
                    context.startActivity(intent);
                }
            });
        }
    }
}
