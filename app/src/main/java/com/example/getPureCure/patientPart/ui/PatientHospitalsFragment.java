package com.example.getPureCure.patientPart.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.getPureCure.adapters.HospitalsAdapter;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.objects.Hospital;
import com.example.getPureCure.patientPart.PatientHomeActivity;
import com.example.getPureCure.patientPart.ShowAllHospitalsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class PatientHospitalsFragment extends Fragment {

    private Context context;

    private RecyclerView categorizedHospitalsRecyclerView, suggestedHospitalsRecyclerView, popularHospitalsRecyclerView;
    private RecyclerView.Adapter categorizedHospitalsRecyclerViewAdapter, suggestedHospitalsRecyclerViewAdapter, popularHospitalsRecyclerViewAdapter;

    private ScrollView scrollView;
    private ProgressBar progressBar;
    private Spinner hospitalsCategorySpinner;

    private TextView allCategorizedHospitalsTextView, allSuggestedHospitalsTextView, allPopularHospitalsTextView;
    private TextView noCategorizedHospitalsTextView, noSuggestedHospitalsTextView, noPopularHospitalsTextView;

    private ArrayList<Hospital> categorizedHospitalsArrayList, suggestedHospitalsArrayList, popularHospitalsArrayList;
    private ArrayList<String> hospitalCategoriesArrayList;

    private Boolean categorizedHospitals = false;
    private Boolean suggestedHospitals = false;
    private Boolean popularHospitals = false;
    private Boolean hospitalCategories = false;

    private String hospitalsCategory = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patient_hospitals, container, false);

        context = container.getContext();

        categorizedHospitalsRecyclerView = root.findViewById(R.id.fragment_patient_hospitals_categorizedHospitals_RecyclerView);
        suggestedHospitalsRecyclerView = root.findViewById(R.id.fragment_patient_hospitals_suggestedHospitals_RecyclerView);
        popularHospitalsRecyclerView = root.findViewById(R.id.fragment_patient_hospitals_popularHospitals_RecyclerView);

        RecyclerView.LayoutManager categorizedHospitalsRecyclerViewLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager suggestedHospitalsRecyclerViewLayoutManager = new GridLayoutManager(context, 2);
        RecyclerView.LayoutManager popularHospitalsRecyclerViewLayoutManager = new GridLayoutManager(context, 2);

        categorizedHospitalsRecyclerView.setLayoutManager(categorizedHospitalsRecyclerViewLayoutManager);
        suggestedHospitalsRecyclerView.setLayoutManager(suggestedHospitalsRecyclerViewLayoutManager);
        popularHospitalsRecyclerView.setLayoutManager(popularHospitalsRecyclerViewLayoutManager);

        scrollView = root.findViewById(R.id.fragment_patient_hospitals_ScrollView);
        progressBar = root.findViewById(R.id.fragment_patient_hospitals_ProgressBar);
        hospitalsCategorySpinner = root.findViewById(R.id.fragment_patient_hospitals_hospitalCategory_Spinner);

        allCategorizedHospitalsTextView = root.findViewById(R.id.fragment_patient_hospitals_allCategorizedHospitals_TextView);
        allSuggestedHospitalsTextView = root.findViewById(R.id.fragment_patient_hospitals_allSuggestedHospitals_TextView);
        allPopularHospitalsTextView = root.findViewById(R.id.fragment_patient_hospitals_allPopularHospitals_TextView);
        noCategorizedHospitalsTextView = root.findViewById(R.id.fragment_patient_hospitals_noCategorizedHospital_TextView);
        noSuggestedHospitalsTextView = root.findViewById(R.id.fragment_patient_hospitals_noSuggestedHospital_TextView);
        noPopularHospitalsTextView = root.findViewById(R.id.fragment_patient_hospitals_noPopularHospital_TextView);

        suggestedHospitalsArrayList = new ArrayList<>();
        popularHospitalsArrayList = new ArrayList<>();
        hospitalCategoriesArrayList = new ArrayList<>();

        loadCategories();
        loadOtherHospitals();

        hospitalsCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hospitalsCategory = parent.getItemAtPosition(position).toString();

                progressBar.setVisibility(View.VISIBLE);
                loadCategorizedHospitals(hospitalsCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        allCategorizedHospitalsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllHospitalsActivity.class);
                intent.putExtra("hospitals_type", "categorizedHospitals");
                intent.putExtra("hospitals_category", hospitalsCategory);
                startActivity(intent);
            }
        });

        allSuggestedHospitalsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllHospitalsActivity.class);
                intent.putExtra("hospitals_type", "suggestedHospitals");
                startActivity(intent);
            }
        });

        allPopularHospitalsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllHospitalsActivity.class);
                intent.putExtra("hospitals_type", "popularHospitals");
                startActivity(intent);
            }
        });


        return root;
    }

    private void loadCategories() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest hospitalCategoriesStringRequest = new StringRequest(Request.Method.GET, API.getGet_hospital_categories(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                hospitalCategoriesArrayList.add(jsonArray.getString(i).trim());
                            }

                            hospitalCategories = true;

                            ArrayAdapter<String> hospitalsCategoryAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, hospitalCategoriesArrayList);
                            hospitalsCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            hospitalsCategorySpinner.setAdapter(hospitalsCategoryAdapter);

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
        hospitalCategoriesStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(hospitalCategoriesStringRequest);
    }

    private void loadCategorizedHospitals(String category) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest categorizedHospitalStringRequest = new StringRequest(Request.Method.GET, API.getGet_hospitals_by_category(category),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            if(length > 10) length = 10;

                            categorizedHospitalsArrayList = new ArrayList<>();

                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

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

                                categorizedHospitalsArrayList.add(new Hospital(id, name, category, address, contact, openHour, facilities, tests));
                            }
                            categorizedHospitalsRecyclerViewAdapter = new HospitalsAdapter(context, categorizedHospitalsArrayList);

                            categorizedHospitalsRecyclerView.setAdapter(categorizedHospitalsRecyclerViewAdapter);

                            categorizedHospitals = true;

                            if (length == 0) {
                                allCategorizedHospitalsTextView.setVisibility(View.GONE);
                                noCategorizedHospitalsTextView.setVisibility(View.VISIBLE);
                            } else {
                                allCategorizedHospitalsTextView.setVisibility(View.VISIBLE);
                                noCategorizedHospitalsTextView.setVisibility(View.GONE);
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

        categorizedHospitalStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(categorizedHospitalStringRequest);
    }

    private void loadOtherHospitals() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest suggestedHospitalStringRequest = new StringRequest(Request.Method.GET, API.getGet_suggested_hospitals(MainActivity.getSavedValues().getAccountId()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            if(length > 10) length = 10;

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

                                suggestedHospitalsArrayList.add(new Hospital(id, name, category, address, contact, openHour, facilities, tests));
                            }
                            suggestedHospitalsRecyclerViewAdapter = new HospitalsAdapter(context, suggestedHospitalsArrayList);

                            suggestedHospitalsRecyclerView.setAdapter(suggestedHospitalsRecyclerViewAdapter);

                            suggestedHospitals = true;

                            if (length == 0) {
                                allSuggestedHospitalsTextView.setVisibility(View.GONE);
                                noSuggestedHospitalsTextView.setVisibility(View.VISIBLE);
                            } else {
                                allSuggestedHospitalsTextView.setVisibility(View.VISIBLE);
                                noSuggestedHospitalsTextView.setVisibility(View.GONE);
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

        suggestedHospitalStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(suggestedHospitalStringRequest);

        StringRequest popularHospitalStringRequest = new StringRequest(Request.Method.GET, API.getGet_popular_hospitals(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            if(length > 10) length = 10;

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

                                popularHospitalsArrayList.add(new Hospital(id, name, category, address, contact, openHour, facilities, tests));
                            }
                            popularHospitalsRecyclerViewAdapter = new HospitalsAdapter(context, popularHospitalsArrayList);

                            popularHospitalsRecyclerView.setAdapter(popularHospitalsRecyclerViewAdapter);

                            popularHospitals = true;

                            if (length == 0) {
                                allPopularHospitalsTextView.setVisibility(View.GONE);
                                noPopularHospitalsTextView.setVisibility(View.VISIBLE);
                            } else {
                                allPopularHospitalsTextView.setVisibility(View.VISIBLE);
                                noPopularHospitalsTextView.setVisibility(View.GONE);
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

        popularHospitalStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(popularHospitalStringRequest);

    }

    private void showView() {
        if (categorizedHospitals && suggestedHospitals && popularHospitals && hospitalCategories) {
            scrollView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
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

        PatientHomeActivity.showToastMessage(message);
    }
}