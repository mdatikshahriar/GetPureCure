package com.example.getPureCure.patientPart.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.example.getPureCure.adapters.SmallBlogAdapter;
import com.example.getPureCure.adapters.DoctorsAdapter;
import com.example.getPureCure.adapters.HospitalsAdapter;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.objects.Blog;
import com.example.getPureCure.objects.Doctor;
import com.example.getPureCure.objects.Hospital;
import com.example.getPureCure.patientPart.PatientHomeActivity;
import com.example.getPureCure.patientPart.ShowAllBlogActivity;
import com.example.getPureCure.patientPart.ShowAllDoctorsActivity;
import com.example.getPureCure.patientPart.ShowAllHospitalsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private Context context;

    private RecyclerView popularBlogRecyclerView, popularDoctorsRecyclerView, popularHospitalsRecyclerView;
    private RecyclerView.Adapter popularBlogRecyclerViewAdapter, popularDoctorsRecyclerViewAdapter, popularHospitalsRecyclerViewAdapter;

    private ScrollView scrollView;
    private ProgressBar progressBar;

    private TextView allPopularBlogTextView, allPopularDoctorsTextView, allPopularHospitalsTextView;
    private TextView noPopularBlogTextView, noPopularDoctorsTextView, noPopularHospitalsTextView;

    private ArrayList<Blog> popularBlogArrayList;
    private ArrayList<Doctor> popularDoctorArrayList;
    private ArrayList<Hospital> popularHospitalArrayList;

    private Boolean blog = false;
    private Boolean doctor = false;
    private Boolean hospital = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patient_home, container, false);

        context = container.getContext();

        popularBlogRecyclerView = root.findViewById(R.id.fragment_patient_home_popularBlog_RecyclerView);
        popularDoctorsRecyclerView = root.findViewById(R.id.fragment_patient_home_popularDoctors_RecyclerView);
        popularHospitalsRecyclerView = root.findViewById(R.id.fragment_patient_home_popularHospitals_RecyclerView);

        RecyclerView.LayoutManager popularBlogRecyclerViewLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager popularDoctorsRecyclerViewLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager popularHospitalsRecyclerViewLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        popularBlogRecyclerView.setLayoutManager(popularBlogRecyclerViewLayoutManager);
        popularDoctorsRecyclerView.setLayoutManager(popularDoctorsRecyclerViewLayoutManager);
        popularHospitalsRecyclerView.setLayoutManager(popularHospitalsRecyclerViewLayoutManager);

        scrollView = root.findViewById(R.id.fragment_patient_home_ScrollView);
        progressBar = root.findViewById(R.id.fragment_patient_home_ProgressBar);

        allPopularBlogTextView = root.findViewById(R.id.fragment_patient_home_allPopularBlog_TextView);
        allPopularDoctorsTextView = root.findViewById(R.id.fragment_patient_home_allPopularDoctors_TextView);
        allPopularHospitalsTextView = root.findViewById(R.id.fragment_patient_home_allPopularHospitals_TextView);
        noPopularBlogTextView = root.findViewById(R.id.fragment_patient_home_noPopularBlog_TextView);
        noPopularDoctorsTextView = root.findViewById(R.id.fragment_patient_home_noPopularDoctors_TextView);
        noPopularHospitalsTextView = root.findViewById(R.id.fragment_patient_home_noPopularHospitals_TextView);

        popularBlogArrayList = new ArrayList<>();
        popularDoctorArrayList = new ArrayList<>();
        popularHospitalArrayList = new ArrayList<>();

        loadData();

        allPopularBlogTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllBlogActivity.class);
                intent.putExtra("blog_type", "popularBlog");
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

    private void loadData() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest blogStringRequest = new StringRequest(Request.Method.GET, API.getGet_popular_blog(),
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

                                String id = jsonObject.getString("_id").trim();
                                String authorId = jsonObject.getString("author_id").trim();
                                String title = jsonObject.getString("title").trim();
                                String titleImageUri = jsonObject.getString("image").trim();
                                String content = jsonObject.getString("content").trim();
                                String category = jsonObject.getString("category").trim();
                                String date = jsonObject.getString("date").trim();
                                String likeCount = jsonObject.getString("likes").trim();
                                String commentCount = jsonObject.getString("comments").trim();

                                JSONArray tagArray = jsonObject.getJSONArray("tags");
                                ArrayList<String> tags = new ArrayList<>();

                                for (int j = 0; j < tagArray.length(); j++) {
                                    tags.add(tagArray.getString(j).trim());
                                }

                                popularBlogArrayList.add(new Blog(id, authorId, title, titleImageUri, content, category, date, likeCount, commentCount, tags));
                            }
                            popularBlogRecyclerViewAdapter = new SmallBlogAdapter(context, popularBlogArrayList);

                            popularBlogRecyclerView.setAdapter(popularBlogRecyclerViewAdapter);

                            blog = true;

                            if (length == 0) {
                                allPopularBlogTextView.setVisibility(View.GONE);
                                noPopularBlogTextView.setVisibility(View.VISIBLE);
                            } else {
                                allPopularBlogTextView.setVisibility(View.VISIBLE);
                                noPopularBlogTextView.setVisibility(View.GONE);
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

        blogStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(blogStringRequest);

        StringRequest doctorStringRequest = new StringRequest(Request.Method.GET, API.getGet_popular_doctors(),
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

                                popularDoctorArrayList.add(new Doctor(name, photoUri, user_id, category));
                            }
                            popularDoctorsRecyclerViewAdapter = new DoctorsAdapter(context, popularDoctorArrayList);

                            popularDoctorsRecyclerView.setAdapter(popularDoctorsRecyclerViewAdapter);

                            doctor = true;

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

        doctorStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(doctorStringRequest);

        StringRequest hospitalStringRequest = new StringRequest(Request.Method.GET, API.getGet_popular_hospitals(),
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

                                popularHospitalArrayList.add(new Hospital(id, name, category, address, contact, openHour, facilities, tests));
                            }
                            popularHospitalsRecyclerViewAdapter = new HospitalsAdapter(context, popularHospitalArrayList);

                            popularHospitalsRecyclerView.setAdapter(popularHospitalsRecyclerViewAdapter);

                            hospital = true;

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

        hospitalStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(hospitalStringRequest);
    }

    private void showView() {
        if (blog && doctor && hospital) {
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