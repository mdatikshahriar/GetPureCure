package com.example.getPureCure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    }

    @Override
    public int getItemCount() {
        return treatmentHistoryArray.size();
    }

    private class TreatmentHistoryHolder extends RecyclerView.ViewHolder {
        TreatmentHistoryHolder(View itemView) {
            super(itemView);
        }
    }
}
