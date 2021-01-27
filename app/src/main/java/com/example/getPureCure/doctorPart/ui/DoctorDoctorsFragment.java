package com.example.getPureCure.doctorPart.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.getPureCure.adapters.DoctorsAdapter;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.assets.SavedValues;
import com.example.getPureCure.doctorPart.DoctorHomeActivity;
import com.example.getPureCure.doctorPart.ShowAllDoctorsActivity;
import com.example.getPureCure.objects.Doctor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DoctorDoctorsFragment extends Fragment {

    private Context context;

    private RecyclerView categorizedDoctorsRecyclerView, suggestedDoctorsRecyclerView, popularDoctorsRecyclerView;
    private RecyclerView.Adapter categorizedDoctorsRecyclerViewAdapter, suggestedDoctorsRecyclerViewAdapter, popularDoctorsRecyclerViewAdapter;

    private ScrollView scrollView;
    private ProgressBar progressBar;
    private Spinner doctorsCategorySpinner;

    private TextView allCategorizedDoctorsTextView, allSuggestedDoctorsTextView, allPopularDoctorsTextView;
    private TextView noCategorizedDoctorsTextView, noSuggestedDoctorsTextView, noPopularDoctorsTextView;

    private ArrayList<Doctor> categorizedDoctorsArrayList, suggestedDoctorsArrayList, popularDoctorsArrayList;
    private ArrayList<String> doctorCategoriesArrayList;

    private Boolean categorizedDoctors = false;
    private Boolean suggestedDoctors = false;
    private Boolean popularDoctors = false;
    private Boolean doctorCategories = false;

    private String doctorsCategory = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patient_doctors, container, false);

        context = container.getContext();

        categorizedDoctorsRecyclerView = root.findViewById(R.id.fragment_patient_doctors_categorizedDoctors_RecyclerView);
        suggestedDoctorsRecyclerView = root.findViewById(R.id.fragment_patient_doctors_suggestedDoctors_RecyclerView);
        popularDoctorsRecyclerView = root.findViewById(R.id.fragment_patient_doctors_popularDoctors_RecyclerView);

        RecyclerView.LayoutManager categorizedDoctorsRecyclerViewLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager suggestedDoctorsRecyclerViewLayoutManager = new GridLayoutManager(context, 2);
        RecyclerView.LayoutManager popularDoctorsRecyclerViewLayoutManager = new GridLayoutManager(context, 2);

        categorizedDoctorsRecyclerView.setLayoutManager(categorizedDoctorsRecyclerViewLayoutManager);
        suggestedDoctorsRecyclerView.setLayoutManager(suggestedDoctorsRecyclerViewLayoutManager);
        popularDoctorsRecyclerView.setLayoutManager(popularDoctorsRecyclerViewLayoutManager);

        scrollView = root.findViewById(R.id.fragment_patient_doctors_ScrollView);
        progressBar = root.findViewById(R.id.fragment_patient_doctors_ProgressBar);
        doctorsCategorySpinner = root.findViewById(R.id.fragment_patient_doctors_doctorCategory_Spinner);

        allCategorizedDoctorsTextView = root.findViewById(R.id.fragment_patient_doctors_allCategorizedDoctors_TextView);
        allSuggestedDoctorsTextView = root.findViewById(R.id.fragment_patient_doctors_allSuggestedDoctors_TextView);
        allPopularDoctorsTextView = root.findViewById(R.id.fragment_patient_doctors_allPopularDoctors_TextView);
        noCategorizedDoctorsTextView = root.findViewById(R.id.fragment_patient_doctors_noCategorizedDoctor_TextView);
        noSuggestedDoctorsTextView = root.findViewById(R.id.fragment_patient_doctors_noSuggestedDoctor_TextView);
        noPopularDoctorsTextView = root.findViewById(R.id.fragment_patient_doctors_noPopularDoctor_TextView);

        suggestedDoctorsArrayList = new ArrayList<>();
        popularDoctorsArrayList = new ArrayList<>();
        doctorCategoriesArrayList = new ArrayList<>();

        loadCategories();
        loadOtherDoctors();

        doctorsCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                doctorsCategory = parent.getItemAtPosition(position).toString();

                progressBar.setVisibility(View.VISIBLE);
                loadCategorizedDoctors(doctorsCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        allCategorizedDoctorsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllDoctorsActivity.class);
                intent.putExtra("doctors_type", "categorizedDoctors");
                intent.putExtra("doctors_category", doctorsCategory);
                startActivity(intent);
            }
        });

        allSuggestedDoctorsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllDoctorsActivity.class);
                intent.putExtra("doctors_type", "suggestedDoctors");
                startActivity(intent);
            }
        });

        allPopularDoctorsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllDoctorsActivity.class);
                intent.putExtra("doctors_type", "popularDoctors");
                startActivity(intent);
            }
        });

        return root;
    }

    private void loadCategories() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest doctorCategoriesStringRequest = new StringRequest(Request.Method.GET, API.getGet_doctor_categories(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                doctorCategoriesArrayList.add(jsonArray.getString(i).trim());
                            }

                            doctorCategories = true;

                            ArrayAdapter<String> doctorsCategoryAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, doctorCategoriesArrayList);
                            doctorsCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            doctorsCategorySpinner.setAdapter(doctorsCategoryAdapter);

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
        doctorCategoriesStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(doctorCategoriesStringRequest);
    }

    private void loadCategorizedDoctors(String category) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest categorizedDoctorStringRequest = new StringRequest(Request.Method.GET, API.getGet_doctors_by_category(category),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            if(length > 10) length = 10;

                            categorizedDoctorsArrayList = new ArrayList<>();

                            for (int i = 0; i < length; i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                JSONObject userIdObject = jsonObject.getJSONObject("user_id");

                                String name = userIdObject.getString("name").trim();
                                String photoUri = userIdObject.getString("photo").trim();
                                String user_id = userIdObject.getString("_id").trim();
                                String category = jsonObject.getString("category").trim();

                                categorizedDoctorsArrayList.add(new Doctor(name, photoUri, user_id, category));
                            }
                            categorizedDoctorsRecyclerViewAdapter = new DoctorsAdapter(context, categorizedDoctorsArrayList);

                            categorizedDoctorsRecyclerView.setAdapter(categorizedDoctorsRecyclerViewAdapter);

                            categorizedDoctors = true;

                            if (length == 0) {
                                allCategorizedDoctorsTextView.setVisibility(View.GONE);
                                noCategorizedDoctorsTextView.setVisibility(View.VISIBLE);
                            } else {
                                allCategorizedDoctorsTextView.setVisibility(View.VISIBLE);
                                noCategorizedDoctorsTextView.setVisibility(View.GONE);
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

        categorizedDoctorStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(categorizedDoctorStringRequest);
    }

    private void loadOtherDoctors() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest suggestedDoctorsStringRequest = new StringRequest(Request.Method.GET, API.getGet_suggested_doctors(getSavedValues().getAccountId()),
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

                                JSONObject userIdObject = jsonObject.getJSONObject("user_id");

                                String name = userIdObject.getString("name").trim();
                                String photoUri = userIdObject.getString("photo").trim();
                                String user_id = userIdObject.getString("_id").trim();
                                String category = jsonObject.getString("category").trim();

                                suggestedDoctorsArrayList.add(new Doctor(name, photoUri, user_id, category));
                            }
                            suggestedDoctorsRecyclerViewAdapter = new DoctorsAdapter(context, suggestedDoctorsArrayList);

                            suggestedDoctorsRecyclerView.setAdapter(suggestedDoctorsRecyclerViewAdapter);

                            suggestedDoctors = true;

                            if (length == 0) {
                                allSuggestedDoctorsTextView.setVisibility(View.GONE);
                                noSuggestedDoctorsTextView.setVisibility(View.VISIBLE);
                            } else {
                                allSuggestedDoctorsTextView.setVisibility(View.VISIBLE);
                                noSuggestedDoctorsTextView.setVisibility(View.GONE);
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

        suggestedDoctorsStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(suggestedDoctorsStringRequest);

        StringRequest popularDoctorsStringRequest = new StringRequest(Request.Method.GET, API.getGet_popular_doctors(),
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

                                JSONObject userIdObject = jsonObject.getJSONObject("user_id");

                                String name = userIdObject.getString("name").trim();
                                String photoUri = userIdObject.getString("photo").trim();
                                String user_id = userIdObject.getString("_id").trim();
                                String category = jsonObject.getString("category").trim();

                                popularDoctorsArrayList.add(new Doctor(name, photoUri, user_id, category));
                            }
                            popularDoctorsRecyclerViewAdapter = new DoctorsAdapter(context, popularDoctorsArrayList);

                            popularDoctorsRecyclerView.setAdapter(popularDoctorsRecyclerViewAdapter);

                            popularDoctors = true;

                            if (length == 0) {
                                allPopularDoctorsTextView.setVisibility(View.GONE);
                                noPopularDoctorsTextView.setVisibility(View.VISIBLE);
                            } else {
                                allPopularDoctorsTextView.setVisibility(View.VISIBLE);
                                noPopularDoctorsTextView.setVisibility(View.GONE);
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

        popularDoctorsStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(popularDoctorsStringRequest);
    }

    private SavedValues getSavedValues() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("GetPureCure_SharedPreferences", Context.MODE_PRIVATE);
        return new SavedValues(sharedPreferences);
    }

    private void showView() {
        if (categorizedDoctors && suggestedDoctors && popularDoctors && doctorCategories) {
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

        DoctorHomeActivity.showToastMessage(message);
    }
}