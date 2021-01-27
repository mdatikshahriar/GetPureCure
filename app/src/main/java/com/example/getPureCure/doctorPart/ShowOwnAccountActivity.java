package com.example.getPureCure.doctorPart;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.getPureCure.MainActivity;
import com.example.getPureCure.R;
import com.example.getPureCure.adapters.TreatmentHistoryAdapter;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.assets.SavedValues;
import com.example.getPureCure.objects.TreatmentHistory;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class ShowOwnAccountActivity extends AppCompatActivity {

    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;

    private ImageView profilePhotoCircularImageView;

    private TextView accountNameTextView, accountTypeTextView, addressTextView,
            phoneTextView, emailTextView, physicalHeightTextView,
            physicalWeightTextView, bloodGroupTextView;

    private LinearLayout physicalDiseasesLinearLayout;

    private RecyclerView treatmentHistoryRecyclerView;
    private RecyclerView.Adapter treatmentHistoryRecyclerViewAdapter;

    private Dialog toastMessageDialog;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_own_account);

        nestedScrollView = findViewById(R.id.activity_show_own_account_NestedScrollView);
        progressBar = findViewById(R.id.activity_show_own_account_ProgressBar);

        profilePhotoCircularImageView = findViewById(R.id.activity_show_own_account_profilePhoto_CircularImageView);

        accountNameTextView = findViewById(R.id.activity_show_own_account_accountName_TextView);
        accountTypeTextView = findViewById(R.id.activity_show_own_account_accountType_TextView);
        addressTextView = findViewById(R.id.activity_show_own_account_address_TextView);
        phoneTextView = findViewById(R.id.activity_show_own_account_phone_TextView);
        emailTextView = findViewById(R.id.activity_show_own_account_email_TextView);
        physicalHeightTextView = findViewById(R.id.activity_show_own_account_physicalHeight_TextView);
        physicalWeightTextView = findViewById(R.id.activity_show_own_account_physicalWeight_TextView);
        bloodGroupTextView = findViewById(R.id.activity_show_own_account_bloodGroup_TextView);

        TextView updateAccountTextView = findViewById(R.id.activity_show_own_account_updateAccount_TextView);
        TextView updatePhysicalInfoTextView = findViewById(R.id.activity_show_own_account_updatePhysicalInfo_TextView);
        TextView addTreatmentHistoryTextView = findViewById(R.id.activity_show_own_account_addTreatmentHistory_TextView);

        physicalDiseasesLinearLayout = findViewById(R.id.activity_show_own_account_physicalDiseases_LinearLayout);

        treatmentHistoryRecyclerView = findViewById(R.id.activity_show_own_account_treatmentHistory_RecyclerView);

        RecyclerView.LayoutManager treatmentHistoryRecyclerViewLayoutManager = new LinearLayoutManager(this);

        treatmentHistoryRecyclerView.setLayoutManager(treatmentHistoryRecyclerViewLayoutManager);

        toastMessageDialog = new Dialog(ShowOwnAccountActivity.this);

        userId = getSavedValues().getAccountId();

        getPatientHistory();

        updateAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        updatePhysicalInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        addTreatmentHistoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    private void getPatientHistory() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, API.getGet_patient_history(userId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String imageUri = jsonObject.getString("photo").trim();

                            if (imageUri.isEmpty()){
                                Picasso.get().load(R.drawable.ic_account)
                                        .centerCrop()
                                        .fit()
                                        .error(R.drawable.ic_account)
                                        .into(profilePhotoCircularImageView);
                            } else {
                                Picasso.get().load(imageUri)
                                        .centerCrop()
                                        .fit()
                                        .error(R.drawable.ic_account)
                                        .into(profilePhotoCircularImageView);
                            }

                            accountNameTextView.setText(jsonObject.getString("name").trim());
                            accountTypeTextView.setText(jsonObject.getString("type").trim().toUpperCase());

                            if (jsonObject.has("address")) {
                                addressTextView.setText(jsonObject.getString("address").trim());
                            }

                            phoneTextView.setText(jsonObject.getString("phone").trim());
                            emailTextView.setText(jsonObject.getString("email").trim());

                            if (jsonObject.has("physical_info")) {
                                JSONObject physicalObject = jsonObject.getJSONObject("physical_info");

                                physicalHeightTextView.setText(physicalObject.getString("height").trim());
                                physicalWeightTextView.setText(physicalObject.getString("weight").trim());
                                bloodGroupTextView.setText(physicalObject.getString("blood").trim());
                            }

                            if (jsonObject.has("diseases")) {
                                JSONArray diseaseArray = jsonObject.getJSONArray("diseases");

                                for (int i = 0; i < diseaseArray.length(); i++) {
                                    TextView textView = new TextView(ShowOwnAccountActivity.this);

                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams.setMargins(10, 0, 10, 0);

                                    textView.setLayoutParams(layoutParams);
                                    textView.setText(diseaseArray.getString(i).trim().toUpperCase());
                                    textView.setBackgroundResource(R.drawable.primary_rounded_rectangle);
                                    textView.setPadding(20,10,20,10);
                                    physicalDiseasesLinearLayout.addView(textView);
                                }
                            }

                            ArrayList<TreatmentHistory> treatmentHistoryArrayList = new ArrayList<>();

                            if (jsonObject.has("history")) {
                                JSONArray historyArray = jsonObject.getJSONArray("history");

                                for (int i = 0; i < historyArray.length(); i++) {
                                    JSONObject historyObject = historyArray.getJSONObject(i);

                                    String id = historyObject.getString("_id").trim();
                                    String doctorName = historyObject.getString("doctor_name").trim();
                                    String date = historyObject.getString("date").trim();

                                    JSONArray diseaseArray = historyObject.getJSONArray("diseases");
                                    JSONArray testArray = historyObject.getJSONArray("tests");
                                    JSONArray medicineArray = historyObject.getJSONArray("medicines");

                                    ArrayList<String> diseases = new ArrayList<>();
                                    ArrayList<String> tests = new ArrayList<>();
                                    ArrayList<String> medicines = new ArrayList<>();

                                    for (int j = 0; j < diseaseArray.length(); j++) {
                                        diseases.add(diseaseArray.getString(j).trim());
                                    }

                                    for (int j = 0; j < testArray.length(); j++) {
                                        tests.add(testArray.getString(j).trim());
                                    }

                                    for (int j = 0; j < medicineArray.length(); j++) {
                                        medicines.add(medicineArray.getString(j).trim());
                                    }

                                    treatmentHistoryArrayList.add(new TreatmentHistory(id, doctorName, date, diseases, tests, medicines));
                                }

                                treatmentHistoryRecyclerViewAdapter = new TreatmentHistoryAdapter(ShowOwnAccountActivity.this, treatmentHistoryArrayList);

                                treatmentHistoryRecyclerView.setAdapter(treatmentHistoryRecyclerViewAdapter);
                            }

                            showView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("catchError", e.toString());
                            showView();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("responseError", volleyError.toString());
                        showError(volleyError);
                    }
                });

        stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(stringRequest);
    }

    private SavedValues getSavedValues() {
        SharedPreferences sharedPreferences = getSharedPreferences("GetPureCure_SharedPreferences", Context.MODE_PRIVATE);
        return new SavedValues(sharedPreferences);
    }

    private void showView() {
        nestedScrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showError(VolleyError volleyError) {
        String message = null;
        if (volleyError.networkResponse != null) {
            message = new String(volleyError.networkResponse.data, StandardCharsets.UTF_8);
        } else if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }

        showToastMessage(message);
    }

    private void showToastMessage(final String message) {
        toastMessageDialog.setContentView(R.layout.item_toast_message);
        toastMessageDialog.setCanceledOnTouchOutside(true);

        TextView toast_TextView = toastMessageDialog.findViewById(R.id.item_toast_message_toast_TextView);
        toast_TextView.setText(message);

        toastMessageDialog.show();
        Objects.requireNonNull(toastMessageDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toastMessageDialog.cancel();
            }
        }, 5000);
    }
}
