package com.example.getPureCure.patientPart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.getPureCure.adapters.SmallBlogAdapter;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.objects.Blog;
import com.example.getPureCure.adapters.BigBlogAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class ShowAllBlogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;

    private ScrollView scrollView;
    private ProgressBar progressBar;

    private Dialog toastMessageDialog;

    private TextView noTextView;

    private ArrayList<Blog> blogArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_blog);

        recyclerView = findViewById(R.id.activity_show_all_blog_RecyclerView);

        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        scrollView = findViewById(R.id.activity_show_all_blog_ScrollView);
        progressBar = findViewById(R.id.activity_show_all_blog_ProgressBar);

        TextView customTextView = findViewById(R.id.activity_show_all_blog_custom_TextView);
        noTextView = findViewById(R.id.activity_show_all_blog_no_TextView);

        toastMessageDialog = new Dialog(ShowAllBlogActivity.this);

        blogArrayList = new ArrayList<>();

        Intent intent = getIntent();

        String blogType = intent.getStringExtra("blog_type");

        if (blogType.equals("categorizedBlog")){
            String blogCategory = intent.getStringExtra("blog_category");

            customTextView.setText("All Blog on " + blogCategory + " Category");

            loadCategorizedBlog(blogCategory);

        } else if (blogType.equals("suggestedBlog")){
            customTextView.setText("All Suggested Blog for You");

            loadSuggestedBlog(MainActivity.getSavedValues().getAccountId());

        } else if (blogType.equals("recentBlog")){
            customTextView.setText("All Recent Blog");

            loadRecentBlog();

        } else if (blogType.equals("popularBlog")){
            customTextView.setText("All Popular Blog");

            loadPopularBlog();

        }
    }

    private void loadCategorizedBlog(String blogCategory) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest categorizedBlogStringRequest = new StringRequest(Request.Method.GET, API.getGet_blog_by_category(blogCategory),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

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

                                blogArrayList.add(new Blog(id, authorId, title, titleImageUri, content, category, date, likeCount, commentCount, tags));
                            }
                            recyclerViewAdapter = new BigBlogAdapter(ShowAllBlogActivity.this,blogArrayList);

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

        categorizedBlogStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(categorizedBlogStringRequest);
    }

    private void loadSuggestedBlog(String accountId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest suggestedBlogStringRequest = new StringRequest(Request.Method.GET, API.getGet_suggested_blog(accountId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

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

                                blogArrayList.add(new Blog(id, authorId, title, titleImageUri, content, category, date, likeCount, commentCount, tags));
                            }
                            recyclerViewAdapter = new BigBlogAdapter(ShowAllBlogActivity.this, blogArrayList);

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

        suggestedBlogStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(suggestedBlogStringRequest);
    }

    private void loadRecentBlog() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest recentBlogStringRequest = new StringRequest(Request.Method.GET, API.getGet_recent_blog(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

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

                                blogArrayList.add(new Blog(id, authorId, title, titleImageUri, content, category, date, likeCount, commentCount, tags));
                            }
                            recyclerViewAdapter = new BigBlogAdapter(ShowAllBlogActivity.this, blogArrayList);

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

        recentBlogStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(recentBlogStringRequest);
    }

    private void loadPopularBlog() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest popularBlogStringRequest = new StringRequest(Request.Method.GET, API.getGet_popular_blog(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            int length = jsonArray.length();

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

                                blogArrayList.add(new Blog(id, authorId, title, titleImageUri, content, category, date, likeCount, commentCount, tags));
                            }
                            recyclerViewAdapter = new BigBlogAdapter(ShowAllBlogActivity.this, blogArrayList);

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

        popularBlogStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(popularBlogStringRequest);
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
