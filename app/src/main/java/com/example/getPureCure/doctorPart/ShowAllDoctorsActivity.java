package com.example.getPureCure.doctorPart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.getPureCure.adapters.DoctorsAdapter;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.assets.SavedValues;
import com.example.getPureCure.objects.Doctor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class ShowAllDoctorsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;

    private ScrollView scrollView;
    private ProgressBar progressBar;

    private TextView noTextView;

    private Dialog toastMessageDialog;

    private ArrayList<Doctor> doctorArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_doctors);

        recyclerView = findViewById(R.id.activity_show_all_doctors_RecyclerView);

        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        scrollView = findViewById(R.id.activity_show_all_doctors_ScrollView);
        progressBar = findViewById(R.id.activity_show_all_doctors_ProgressBar);

        TextView customTextView = findViewById(R.id.activity_show_all_doctors_custom_TextView);
        noTextView = findViewById(R.id.activity_show_all_doctors_no_TextView);

        toastMessageDialog = new Dialog(ShowAllDoctorsActivity.this);

        doctorArrayList = new ArrayList<>();

        Intent intent = getIntent();

        String doctorsType = intent.getStringExtra("doctors_type");

        if (doctorsType.equals("categorizedDoctors")){
            String doctorsCategory = intent.getStringExtra("doctors_category");

            customTextView.setText("All Doctors on " + doctorsCategory + " Category");

            loadCategorizedDoctors(doctorsCategory);

        } else if (doctorsType.equals("suggestedDoctors")){
            customTextView.setText("All Suggested Doctors for You");

            loadSuggestedDoctors(getSavedValues().getAccountId());

        } else if (doctorsType.equals("popularDoctors")){
            customTextView.setText("All Popular Doctors");

            loadPopularDoctors();

        }
    }

    private void loadCategorizedDoctors(String category) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest doctorStringRequest = new StringRequest(Request.Method.GET, API.getGet_doctors_by_category(category),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                JSONObject userIdObject = jsonObject.getJSONObject("user_id");

                                String name = userIdObject.getString("name").trim();
                                String photoUri = userIdObject.getString("photo").trim();
                                String user_id = userIdObject.getString("_id").trim();
                                String category = jsonObject.getString("category").trim();

                                doctorArrayList.add(new Doctor(name, photoUri, user_id, category));
                            }
                            recyclerViewAdapter = new DoctorsAdapter(ShowAllDoctorsActivity.this, doctorArrayList);

                            recyclerView.setAdapter(recyclerViewAdapter);

                            if (length == 0) {
                                noTextView.setVisibility(View.VISIBLE);
                            } else {
                                noTextView.setVisibility(View.GONE);
                            }

                            showView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("catchError", e.toString());
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

        doctorStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(doctorStringRequest);
    }

    private void loadSuggestedDoctors(String accountId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest doctorStringRequest = new StringRequest(Request.Method.GET, API.getGet_suggested_doctors(accountId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                JSONObject userIdObject = jsonObject.getJSONObject("user_id");

                                String name = userIdObject.getString("name").trim();
                                String photoUri = userIdObject.getString("photo").trim();
                                String user_id = userIdObject.getString("_id").trim();
                                String category = jsonObject.getString("category").trim();

                                doctorArrayList.add(new Doctor(name, photoUri, user_id, category));
                            }
                            recyclerViewAdapter = new DoctorsAdapter(ShowAllDoctorsActivity.this, doctorArrayList);

                            recyclerView.setAdapter(recyclerViewAdapter);

                            if (length == 0) {
                                noTextView.setVisibility(View.VISIBLE);
                            } else {
                                noTextView.setVisibility(View.GONE);
                            }

                            showView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("catchError", e.toString());
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

        doctorStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(doctorStringRequest);
    }

    private void loadPopularDoctors() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest doctorStringRequest = new StringRequest(Request.Method.GET, API.getGet_popular_doctors(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                JSONObject userIdObject = jsonObject.getJSONObject("user_id");

                                String name = userIdObject.getString("name").trim();
                                String photoUri = userIdObject.getString("photo").trim();
                                String user_id = userIdObject.getString("_id").trim();
                                String category = jsonObject.getString("category").trim();

                                doctorArrayList.add(new Doctor(name, photoUri, user_id, category));
                            }
                            recyclerViewAdapter = new DoctorsAdapter(ShowAllDoctorsActivity.this, doctorArrayList);

                            recyclerView.setAdapter(recyclerViewAdapter);

                            if (length == 0) {
                                noTextView.setVisibility(View.VISIBLE);
                            } else {
                                noTextView.setVisibility(View.GONE);
                            }

                            showView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("catchError", e.toString());
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

        doctorStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(doctorStringRequest);
    }

    private SavedValues getSavedValues() {
        SharedPreferences sharedPreferences = getSharedPreferences("GetPureCure_SharedPreferences", Context.MODE_PRIVATE);
        return new SavedValues(sharedPreferences);
    }

    private void showView() {
        scrollView.setVisibility(View.VISIBLE);
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
