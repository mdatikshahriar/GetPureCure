package com.example.getPureCure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getPureCure.R;
import com.example.getPureCure.objects.TreatmentHistory;

import java.util.ArrayList;

public class TreatmentHistoryAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {

    private Context context;

    private ArrayList<TreatmentHistory> treatmentHistoryArray;

    public TreatmentHistoryAdapter(Context context, ArrayList<TreatmentHistory> treatmentHistoryArray) {
        this.context = context;
        this.treatmentHistoryArray = treatmentHistoryArray;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_treatment_history, parent, false);
        return new TreatmentHistoryAdapter.TreatmentHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TreatmentHistory treatmentHistory = treatmentHistoryArray.get(position);

        TreatmentHistoryHolder treatmentHistoryHolder = (TreatmentHistoryHolder) holder;

        TextView dateTextView = treatmentHistoryHolder.dateTextView;
        TextView doctorNameTextView = treatmentHistoryHolder.doctorNameTextView;

        LinearLayout diseasesLinearLayout = treatmentHistoryHolder.diseasesLinearLayout;
        LinearLayout testsLinearLayout = treatmentHistoryHolder.testsLinearLayout;
        LinearLayout medicinesLinearLayout = treatmentHistoryHolder.medicinesLinearLayout;

        dateTextView.setText(treatmentHistory.getDate());
        doctorNameTextView.setText(treatmentHistory.getDoctorName());

        for (int i = 0; i < treatmentHistory.getDiseases().size(); i++) {
            TextView textView = new TextView(context);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 0, 10, 0);

            textView.setLayoutParams(layoutParams);
            textView.setText(treatmentHistory.getDiseases().get(i));
            textView.setBackgroundResource(R.drawable.primary_rounded_rectangle);
            textView.setPadding(20,10,20,10);
            diseasesLinearLayout.addView(textView);
        }

        for (int i = 0; i < treatmentHistory.getTests().size(); i++) {
            TextView textView = new TextView(context);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 0, 10, 0);

            textView.setLayoutParams(layoutParams);
            textView.setText(treatmentHistory.getTests().get(i));
            textView.setBackgroundResource(R.drawable.primary_rounded_rectangle);
            textView.setPadding(20,10,20,10);
            testsLinearLayout.addView(textView);
        }

        for (int i = 0; i < treatmentHistory.getMedicines().size(); i++) {
            TextView textView = new TextView(context);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 0, 10, 0);

            textView.setLayoutParams(layoutParams);
            textView.setText(treatmentHistory.getMedicines().get(i));
            textView.setBackgroundResource(R.drawable.primary_rounded_rectangle);
            textView.setPadding(20,10,20,10);
            medicinesLinearLayout.addView(textView);
        }

    }

    @Override
    public int getItemCount() {
        return treatmentHistoryArray.size();
    }

    private class TreatmentHistoryHolder extends RecyclerView.ViewHolder {
        private TextView dateTextView, doctorNameTextView;
        private LinearLayout diseasesLinearLayout, testsLinearLayout, medicinesLinearLayout;

        TreatmentHistoryHolder(View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.item_treatment_history_date_TextView);
            doctorNameTextView = itemView.findViewById(R.id.item_treatment_history_doctorName_TextView);
            diseasesLinearLayout = itemView.findViewById(R.id.item_treatment_history_diseases_LinearLayout);
            testsLinearLayout = itemView.findViewById(R.id.item_treatment_history_tests_LinearLayout);
            medicinesLinearLayout = itemView.findViewById(R.id.item_treatment_history_medicines_LinearLayout);
        }
    }
}
