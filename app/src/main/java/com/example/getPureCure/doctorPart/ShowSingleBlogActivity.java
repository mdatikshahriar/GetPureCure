package com.example.getPureCure.doctorPart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.getPureCure.adapters.CommentAdapter;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.assets.SavedValues;
import com.example.getPureCure.assets.Time;
import com.example.getPureCure.objects.Comment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class ShowSingleBlogActivity extends AppCompatActivity {

    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;

    private ImageView blogImageView, authorProfilePhotoImageView;

    private RecyclerView blogCommentsRecyclerView;
    private RecyclerView.Adapter blogCommentsRecyclerViewAdapter;

    private TextView blogTitleTextView, blogDateTextView, blogAuthorNameTextView, blogAuthorTypeTextView,
            blogContentTextView, blogLikeTextView, blogCommentTextView, blogCommentsTextView;

    private EditText addCommentEditText;

    private Button addLikeButton;

    private Dialog toastMessageDialog;

    private ArrayList<Comment> commentArrayList;

    private String blogId, accountId;

    private int commentCount, likeCount;

    private Boolean isLiked = false;

    private SavedValues savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_blog);

        nestedScrollView = findViewById(R.id.activity_show_single_blog_NestedScrollView);
        progressBar = findViewById(R.id.activity_show_single_blog_ProgressBar);

        blogImageView = findViewById(R.id.activity_show_single_blog_ImageView);
        authorProfilePhotoImageView = findViewById(R.id.activity_show_single_blog_authorProfilePhoto_CircularImageView);

        blogCommentsRecyclerView = findViewById(R.id.activity_show_single_blog_blogComments_RecyclerView);

        RecyclerView.LayoutManager blogCommentsRecyclerViewLayoutManager = new LinearLayoutManager(this);

        blogCommentsRecyclerView.setLayoutManager(blogCommentsRecyclerViewLayoutManager);

        blogTitleTextView = findViewById(R.id.activity_show_single_blog_blogTitle_TextView);
        blogDateTextView = findViewById(R.id.activity_show_single_blog_blogDate_TextView);
        blogAuthorNameTextView = findViewById(R.id.activity_show_single_blog_blogAuthorName_TextView);
        blogAuthorTypeTextView = findViewById(R.id.activity_show_single_blog_blogAuthorType_TextView);
        blogContentTextView = findViewById(R.id.activity_show_single_blog_blogContent_TextView);
        blogLikeTextView = findViewById(R.id.activity_show_single_blog_blogLike_TextView);
        blogCommentTextView = findViewById(R.id.activity_show_single_blog_blogComment_TextView);
        blogCommentsTextView = findViewById(R.id.activity_show_single_blog_blogComments_TextView);

        addCommentEditText = findViewById(R.id.activity_show_single_blog_addComment_EditText);

        Button addCommentButton = findViewById(R.id.activity_show_single_blog_addComment_Button);
        addLikeButton = findViewById(R.id.activity_show_single_blog_addLike_Button);

        toastMessageDialog = new Dialog(ShowSingleBlogActivity.this);

        commentArrayList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            blogContentTextView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
            blogContentTextView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        Intent intent = getIntent();

        getSavedValues();

        blogId = intent.getStringExtra("blog_id");
        accountId = savedValues.getAccountId();

        loadBlog();

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newComment = addCommentEditText.getText().toString();

                if (newComment.isEmpty()) {
                    addCommentEditText.setError("Type Something!");
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    addComment(newComment);

                    addCommentEditText.setText("");

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(inputManager).hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        addLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                addOrRemoveLike();
            }
        });
    }

    private void loadBlog() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest blogStringRequest = new StringRequest(Request.Method.GET, API.getGet_specific_blog(blogId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONObject blogObject = new JSONObject(response);

                            blogTitleTextView.setText(blogObject.getString("title").trim());

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                blogContentTextView.setText(Html.fromHtml(blogObject.getString("content").trim(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                blogContentTextView.setText(Html.fromHtml(blogObject.getString("content").trim()));
                            }

                            String imageUri = blogObject.getString("image").trim();

                            if (imageUri.isEmpty()){
                                Picasso.get().load(R.drawable.default_blog_picture)
                                        .centerCrop()
                                        .fit()
                                        .error(R.drawable.default_blog_picture)
                                        .into(blogImageView);
                            } else {
                                Picasso.get().load(imageUri)
                                        .centerCrop()
                                        .fit()
                                        .error(R.drawable.default_blog_picture)
                                        .into(blogImageView);
                            }

                            blogDateTextView.setText(Time.getTimeDate(blogObject.getString("date").trim()));

                            JSONObject userObject = blogObject.getJSONObject("user");

                            blogAuthorNameTextView.setText(userObject.getString("name").trim());
                            blogAuthorTypeTextView.setText(userObject.getString("type").trim());

                            String profilePhotoUri = userObject.getString("photo").trim();

                            if (profilePhotoUri.isEmpty()){
                                Picasso.get().load(R.drawable.ic_account)
                                        .centerCrop()
                                        .fit()
                                        .error(R.drawable.ic_account)
                                        .into(authorProfilePhotoImageView);
                            } else {
                                Picasso.get().load(profilePhotoUri)
                                        .centerCrop()
                                        .fit()
                                        .error(R.drawable.ic_account)
                                        .into(authorProfilePhotoImageView);
                            }

                            likeCount = Integer.parseInt(blogObject.getString("likes").trim());

                            if (likeCount > 1) {
                                blogLikeTextView.setText(likeCount + " Likes");
                            } else {
                                blogLikeTextView.setText(likeCount + " Like");
                            }

                            JSONArray commentArray = blogObject.getJSONArray("comments");

                            for (int i = 0; i < commentArray.length(); i++) {
                                JSONObject commentObject = commentArray.getJSONObject(i);

                                JSONObject commentUserObject = commentObject.getJSONObject("user_id");

                                JSONArray replyArray = commentObject.getJSONArray("replies");

                                String commentId = commentObject.getString("_id").trim();
                                String authorId = commentUserObject.getString("_id").trim();
                                String blogId = commentObject.getString("blog_id").trim();
                                String authorName = commentUserObject.getString("name").trim();
                                String authorImageUri = commentUserObject.getString("photo").trim();
                                String content = commentObject.getString("comment").trim();
                                String date = commentObject.getString("date").trim();
                                String replyCount = String.valueOf(replyArray.length());

                                commentArrayList.add(new Comment(commentId, authorId, blogId, authorName, authorImageUri, content, date, replyCount));
                            }

                            blogCommentsRecyclerViewAdapter = new CommentAdapter(commentArrayList);

                            blogCommentsRecyclerView.setAdapter(blogCommentsRecyclerViewAdapter);

                            commentCount = commentArray.length();

                            if (commentCount > 1) {
                                blogCommentTextView.setText(commentCount + " Comments");
                                blogCommentsTextView.setText("Comments");
                            } else if(commentCount == 0) {
                                blogCommentTextView.setText(commentCount + " Comment");
                                blogCommentsTextView.setVisibility(View.GONE);
                            } else {
                                blogCommentTextView.setText(commentCount + " Comment");
                                blogCommentsTextView.setText("Comment");
                            }

                            JSONArray likeArray = blogObject.getJSONArray("liked_by");

                            for (int i = 0; i < likeArray.length(); i++) {
                                JSONObject likeObject = likeArray.getJSONObject(i);

                                String likerId = likeObject.getString("user_id").trim();

                                if (likerId.equals(accountId))
                                    isLiked = true;
                            }

                            if (isLiked) {
                                addLikeButton.setBackgroundResource(R.drawable.ic_thumbs_up_primary);
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

        blogStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(blogStringRequest);
    }

    private void addComment(final String comment) {
        Date currentTime = Calendar.getInstance().getTime();
        final String time = currentTime.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.getAdd_comment(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject commentObject = jsonObject.getJSONObject("comment");

                            JSONArray replyArray = commentObject.getJSONArray("replies");

                            String commentId = commentObject.getString("_id").trim();
                            String authorId = commentObject.getString("user_id").trim();
                            String blogId = commentObject.getString("blog_id").trim();
                            String authorName = savedValues.getAccountName();
                            String authorImageUri = savedValues.getAccountPhotoUri();
                            String content = commentObject.getString("comment").trim();
                            String date = commentObject.getString("date").trim();
                            String replyCount = String.valueOf(replyArray.length());

                            commentArrayList.add(new Comment(commentId, authorId, blogId, authorName, authorImageUri, content, date, replyCount));

                            blogCommentsRecyclerViewAdapter = new CommentAdapter(commentArrayList);

                            blogCommentsRecyclerView.setAdapter(blogCommentsRecyclerViewAdapter);

                            commentCount = commentCount + 1;

                            if (commentCount > 1) {
                                blogCommentTextView.setText(commentCount + " Comments");
                                blogCommentsTextView.setText("Comments");
                            } else {
                                blogCommentTextView.setText(commentCount + " Comment");
                                blogCommentsTextView.setVisibility(View.VISIBLE);
                                blogCommentsTextView.setText("Comment");
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
                })
        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("blog_id", blogId);
                params.put("user_id", accountId);
                params.put("date", time);
                params.put("comment", comment);
                return params;
            }
        };

        stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
        requestQueue.add(stringRequest);
    }

    private void addOrRemoveLike() {
        if(isLiked) {
            isLiked = false;

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, API.getUnlike_blog(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);

                            addLikeButton.setBackgroundResource(R.drawable.ic_thumbs_up_black);

                            likeCount = likeCount - 1;

                            if (likeCount > 1) {
                                blogLikeTextView.setText(likeCount + " Likes");
                            } else {
                                blogLikeTextView.setText(likeCount + " Like");
                            }

                            showView();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("responseError", volleyError.toString());
                            showError(volleyError);
                        }
                    })
            {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("blog_id", blogId);
                    params.put("user_id", accountId);
                    return params;
                }
            };

            stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
            requestQueue.add(stringRequest);

        } else {
            isLiked = true;

            Date currentTime = Calendar.getInstance().getTime();
            final String time = currentTime.toString();

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, API.getLike_blog(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);

                            addLikeButton.setBackgroundResource(R.drawable.ic_thumbs_up_primary);

                            likeCount = likeCount + 1;

                            if (likeCount > 1) {
                                blogLikeTextView.setText(likeCount + " Likes");
                            } else {
                                blogLikeTextView.setText(likeCount + " Like");
                            }

                            showView();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("responseError", volleyError.toString());
                            showError(volleyError);
                        }
                    })
            {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("blog_id", blogId);
                    params.put("user_id", accountId);
                    params.put("date", time);
                    return params;
                }
            };

            stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());
            requestQueue.add(stringRequest);
        }
    }

    private void getSavedValues() {
        SharedPreferences sharedPreferences = getSharedPreferences("GetPureCure_SharedPreferences", Context.MODE_PRIVATE);
        savedValues = new SavedValues(sharedPreferences);
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
