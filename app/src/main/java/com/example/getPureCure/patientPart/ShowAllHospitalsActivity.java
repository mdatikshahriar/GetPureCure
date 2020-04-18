package com.example.getPureCure.patientPart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.example.getPureCure.adapters.HospitalsAdapter;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.objects.Hospital;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class ShowAllHospitalsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;

    private ScrollView scrollView;
    private ProgressBar progressBar;

    private TextView noTextView;

    private Dialog toastMessageDialog;

    private ArrayList<Hospital> hospitalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_hospitals);

        recyclerView = findViewById(R.id.activity_show_all_hospitals_RecyclerView);

        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        scrollView = findViewById(R.id.activity_show_all_hospitals_ScrollView);
        progressBar = findViewById(R.id.activity_show_all_hospitals_ProgressBar);

        TextView customTextView = findViewById(R.id.activity_show_all_hospitals_custom_TextView);
        noTextView = findViewById(R.id.activity_show_all_hospitals_no_TextView);

        toastMessageDialog = new Dialog(ShowAllHospitalsActivity.this);

        hospitalArrayList = new ArrayList<>();

        Intent intent = getIntent();

        String hospitalsType = intent.getStringExtra("hospitals_type");

        if (hospitalsType.equals("categorizedHospitals")){
            String hospitalsCategory = intent.getStringExtra("hospitals_category");

            customTextView.setText("All Hospitals on " + hospitalsCategory + " Category");

            loadCategorizedHospitals(hospitalsCategory);

        } else if (hospitalsType.equals("suggestedHospitals")){
            customTextView.setText("All Suggested Hospitals for You");

            loadSuggestedHospitals(MainActivity.getSavedValues().getAccountId());

        } else if (hospitalsType.equals("popularHospitals")){
            customTextView.setText("All Popular Hospitals");

            loadPopularHospitals();

        }
    }

    private void loadCategorizedHospitals(String category) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest hospitalStringRequest = new StringRequest(Request.Method.GET, API.getGet_hospitals_by_category(category),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //edit it
                                String id = jsonObject.getString("_id").trim();
                                String name = jsonObject.getString("name").trim();
                                String category = jsonObject.getString("category").trim();
                                String address = jsonObject.getString("address").trim();
                                String contact = jsonObject.getString("contact").trim();
                                String openHour = jsonObject.getString("open_hour").trim();

                                JSONArray facilityArray = jsonObject.getJSONArray("facilities");
                                JSONArray testArray = jsonObject.getJSONArray("tests");
                                ArrayList<String> facilities = new ArrayList<>();
                                ArrayList<String> tests = new ArrayList<>();

                                for (int j = 0; j < facilityArray.length(); j++) {
                                    facilities.add(facilityArray.getString(j).trim());
                                }

                                for (int k = 0; k < testArray.length(); k++) {
                                    tests.add(testArray.getString(k).trim());
                                }

                                hospitalArrayList.add(new Hospital(id, name, category, address, contact, openHour, facilities, tests));
                            }
                            recyclerViewAdapter = new HospitalsAdapter(ShowAllHospitalsActivity.this, hospitalArrayList);

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

        hospitalStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(hospitalStringRequest);
    }

    private void loadSuggestedHospitals(String accountId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest hospitalStringRequest = new StringRequest(Request.Method.GET, API.getGet_suggested_hospitals(accountId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //edit it
                                String id = jsonObject.getString("_id").trim();
                                String name = jsonObject.getString("name").trim();
                                String category = jsonObject.getString("category").trim();
                                String address = jsonObject.getString("address").trim();
                                String contact = jsonObject.getString("contact").trim();
                                String openHour = jsonObject.getString("open_hour").trim();

                                JSONArray facilityArray = jsonObject.getJSONArray("facilities");
                                JSONArray testArray = jsonObject.getJSONArray("tests");
                                ArrayList<String> facilities = new ArrayList<>();
                                ArrayList<String> tests = new ArrayList<>();

                                for (int j = 0; j < facilityArray.length(); j++) {
                                    facilities.add(facilityArray.getString(j).trim());
                                }

                                for (int k = 0; k < testArray.length(); k++) {
                                    tests.add(testArray.getString(k).trim());
                                }

                                hospitalArrayList.add(new Hospital(id, name, category, address, contact, openHour, facilities, tests));
                            }
                            recyclerViewAdapter = new HospitalsAdapter(ShowAllHospitalsActivity.this, hospitalArrayList);

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

        hospitalStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(hospitalStringRequest);
    }

    private void loadPopularHospitals() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest hospitalStringRequest = new StringRequest(Request.Method.GET, API.getGet_popular_hospitals(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                //edit it
                                String id = jsonObject.getString("_id").trim();
                                String name = jsonObject.getString("name").trim();
                                String category = jsonObject.getString("category").trim();
                                String address = jsonObject.getString("address").trim();
                                String contact = jsonObject.getString("contact").trim();
                                String openHour = jsonObject.getString("open_hour").trim();

                                JSONArray facilityArray = jsonObject.getJSONArray("facilities");
                                JSONArray testArray = jsonObject.getJSONArray("tests");
                                ArrayList<String> facilities = new ArrayList<>();
                                ArrayList<String> tests = new ArrayList<>();

                                for (int j = 0; j < facilityArray.length(); j++) {
                                    facilities.add(facilityArray.getString(j).trim());
                                }

                                for (int k = 0; k < testArray.length(); k++) {
                                    tests.add(testArray.getString(k).trim());
                                }

                                hospitalArrayList.add(new Hospital(id, name, category, address, contact, openHour, facilities, tests));
                            }
                            recyclerViewAdapter = new HospitalsAdapter(ShowAllHospitalsActivity.this, hospitalArrayList);

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

        hospitalStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(hospitalStringRequest);
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
