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
import com.example.getPureCure.adapters.BigBlogAdapter;
import com.example.getPureCure.adapters.SmallBlogAdapter;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.objects.Blog;
import com.example.getPureCure.patientPart.PatientHomeActivity;
import com.example.getPureCure.patientPart.ShowAllBlogActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class PatientBlogFragment extends Fragment {

    private Context context;

    private RecyclerView categorizedBlogRecyclerView, suggestedBlogRecyclerView, recentBlogRecyclerView, popularBlogRecyclerView;
    private RecyclerView.Adapter categorizedBlogRecyclerViewAdapter, suggestedBlogRecyclerViewAdapter,
            recentBlogRecyclerViewAdapter, popularBlogRecyclerViewAdapter;

    private ScrollView scrollView;
    private ProgressBar progressBar;
    private Spinner blogCategorySpinner;

    private TextView allCategorizedBlogTextView, allSuggestedBlogTextView, allRecentBlogTextView, allPopularBlogTextView;
    private TextView noCategorizedBlogTextView, noSuggestedBlogTextView, noRecentBlogTextView, noPopularBlogTextView;

    private ArrayList<Blog> categorizedBlogArrayList, suggestedBlogArrayList, recentBlogArrayList, popularBlogArrayList;
    private ArrayList<String> blogCategoriesArrayList;

    private Boolean categorizedBlog = false;
    private Boolean suggestedBlog = false;
    private Boolean recentBlog = false;
    private Boolean popularBlog = false;
    private Boolean blogCategories = false;

    private String blogCategory = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_patient_blog, container, false);

        context = container.getContext();

        categorizedBlogRecyclerView = root.findViewById(R.id.fragment_patient_blog_categorizedBlog_RecyclerView);
        suggestedBlogRecyclerView = root.findViewById(R.id.fragment_patient_blog_suggestedBlog_RecyclerView);
        recentBlogRecyclerView = root.findViewById(R.id.fragment_patient_blog_recentBlog_RecyclerView);
        popularBlogRecyclerView = root.findViewById(R.id.fragment_patient_blog_popularBlog_RecyclerView);

        RecyclerView.LayoutManager categorizedBlogRecyclerViewLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager suggestedBlogRecyclerViewLayoutManager = new LinearLayoutManager(context);
        RecyclerView.LayoutManager recentBlogRecyclerViewLayoutManager = new LinearLayoutManager(context);
        RecyclerView.LayoutManager popularBlogRecyclerViewLayoutManager = new LinearLayoutManager(context);

        categorizedBlogRecyclerView.setLayoutManager(categorizedBlogRecyclerViewLayoutManager);
        suggestedBlogRecyclerView.setLayoutManager(suggestedBlogRecyclerViewLayoutManager);
        recentBlogRecyclerView.setLayoutManager(recentBlogRecyclerViewLayoutManager);
        popularBlogRecyclerView.setLayoutManager(popularBlogRecyclerViewLayoutManager);

        scrollView = root.findViewById(R.id.fragment_patient_blog_ScrollView);
        progressBar = root.findViewById(R.id.fragment_patient_blog_ProgressBar);
        blogCategorySpinner = root.findViewById(R.id.fragment_patient_blog_blogCategory_Spinner);

        allCategorizedBlogTextView = root.findViewById(R.id.fragment_patient_blog_allCategorizedBlog_TextView);
        allSuggestedBlogTextView = root.findViewById(R.id.fragment_patient_blog_allSuggestedBlog_TextView);
        allRecentBlogTextView = root.findViewById(R.id.fragment_patient_blog_allRecentBlog_TextView);
        allPopularBlogTextView = root.findViewById(R.id.fragment_patient_blog_allPopularBlog_TextView);
        noCategorizedBlogTextView = root.findViewById(R.id.fragment_patient_blog_noCategorizedBlog_TextView);
        noSuggestedBlogTextView = root.findViewById(R.id.fragment_patient_blog_noSuggestedBlog_TextView);
        noRecentBlogTextView = root.findViewById(R.id.fragment_patient_blog_noRecentBlog_TextView);
        noPopularBlogTextView = root.findViewById(R.id.fragment_patient_blog_noPopularBlog_TextView);

        suggestedBlogArrayList = new ArrayList<>();
        recentBlogArrayList = new ArrayList<>();
        popularBlogArrayList = new ArrayList<>();
        blogCategoriesArrayList = new ArrayList<>();

        loadCategories();
        loadOtherBlog();

        blogCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blogCategory = parent.getItemAtPosition(position).toString();

                progressBar.setVisibility(View.VISIBLE);
                loadCategorizedBlog(blogCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        allCategorizedBlogTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllBlogActivity.class);
                intent.putExtra("blog_type", "categorizedBlog");
                intent.putExtra("blog_category", blogCategory);
                startActivity(intent);
            }
        });

        allSuggestedBlogTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllBlogActivity.class);
                intent.putExtra("blog_type", "suggestedBlog");
                startActivity(intent);
            }
        });

        allRecentBlogTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllBlogActivity.class);
                intent.putExtra("blog_type", "recentBlog");
                startActivity(intent);
            }
        });

        allPopularBlogTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowAllBlogActivity.class);
                intent.putExtra("blog_type", "popularBlog");
                startActivity(intent);
            }
        });

        return root;
    }

    private void loadCategories() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest blogCategoriesStringRequest = new StringRequest(Request.Method.GET, API.getGet_blog_categories(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                blogCategoriesArrayList.add(jsonArray.getString(i).trim());
                            }

                            blogCategories = true;

                            ArrayAdapter<String> blogCategoryAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, blogCategoriesArrayList);
                            blogCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            blogCategorySpinner.setAdapter(blogCategoryAdapter);

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

        blogCategoriesStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(blogCategoriesStringRequest);
    }

    private void loadCategorizedBlog(String category) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest categorizedBlogStringRequest = new StringRequest(Request.Method.GET, API.getGet_blog_by_category(category),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            if(length > 10) length = 10;

                            categorizedBlogArrayList = new ArrayList<>();

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

                                categorizedBlogArrayList.add(new Blog(id, authorId, title, titleImageUri, content, category, date, likeCount, commentCount, tags));
                            }
                            categorizedBlogRecyclerViewAdapter = new SmallBlogAdapter(context, categorizedBlogArrayList);

                            categorizedBlogRecyclerView.setAdapter(categorizedBlogRecyclerViewAdapter);

                            categorizedBlog = true;

                            if (length == 0) {
                                allCategorizedBlogTextView.setVisibility(View.GONE);
                                noCategorizedBlogTextView.setVisibility(View.VISIBLE);
                            } else {
                                allCategorizedBlogTextView.setVisibility(View.VISIBLE);
                                noCategorizedBlogTextView.setVisibility(View.GONE);
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

        categorizedBlogStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(categorizedBlogStringRequest);
    }

    private void loadOtherBlog() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest suggestedBlogStringRequest = new StringRequest(Request.Method.GET, API.getGet_suggested_blog(MainActivity.getSavedValues().getAccountId()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            if(length > 5) length = 5;

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

                                suggestedBlogArrayList.add(new Blog(id, authorId, title, titleImageUri, content, category, date, likeCount, commentCount, tags));
                            }
                            suggestedBlogRecyclerViewAdapter = new BigBlogAdapter(context, suggestedBlogArrayList);

                            suggestedBlogRecyclerView.setAdapter(suggestedBlogRecyclerViewAdapter);

                            suggestedBlog = true;

                            if (length == 0) {
                                allSuggestedBlogTextView.setVisibility(View.GONE);
                                noSuggestedBlogTextView.setVisibility(View.VISIBLE);
                            } else {
                                allSuggestedBlogTextView.setVisibility(View.VISIBLE);
                                noSuggestedBlogTextView.setVisibility(View.GONE);
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

        suggestedBlogStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(suggestedBlogStringRequest);

        StringRequest recentBlogStringRequest = new StringRequest(Request.Method.GET, API.getGet_recent_blog(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            if(length > 5) length = 5;

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

                                recentBlogArrayList.add(new Blog(id, authorId, title, titleImageUri, content, category, date, likeCount, commentCount, tags));
                            }
                            recentBlogRecyclerViewAdapter = new BigBlogAdapter(context, recentBlogArrayList);

                            recentBlogRecyclerView.setAdapter(recentBlogRecyclerViewAdapter);

                            recentBlog = true;

                            if (length == 0) {
                                allRecentBlogTextView.setVisibility(View.GONE);
                                noRecentBlogTextView.setVisibility(View.VISIBLE);
                            } else {
                                allRecentBlogTextView.setVisibility(View.VISIBLE);
                                noRecentBlogTextView.setVisibility(View.GONE);
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

        recentBlogStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(recentBlogStringRequest);

        StringRequest popularBlogStringRequest = new StringRequest(Request.Method.GET, API.getGet_popular_blog(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

                            if(length > 5) length = 5;

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
                            popularBlogRecyclerViewAdapter = new BigBlogAdapter(context, popularBlogArrayList);

                            popularBlogRecyclerView.setAdapter(popularBlogRecyclerViewAdapter);

                            popularBlog = true;

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

        popularBlogStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(popularBlogStringRequest);
    }

    private void showView() {
        if (popularBlog && recentBlog && suggestedBlog && categorizedBlog && blogCategories) {
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