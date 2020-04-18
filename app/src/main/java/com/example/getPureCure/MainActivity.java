package com.example.getPureCure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.assets.SavedValues;
import com.example.getPureCure.patientPart.PatientHomeActivity;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static RetryPolicy retryPolicy;
    private static SavedValues savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("GetPureCure_SharedPreferences", Context.MODE_PRIVATE);
        savedValues = new SavedValues(sharedPreferences);

        retryPolicy = new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        final String accountId = savedValues.getAccountId();
        final String accountToken = savedValues.getAccountToken();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isOnline()){
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, API.getVerify_user_session(accountId, accountToken),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Response", response);
                                    String isLoggedIn = response.trim();

                                    if (isLoggedIn.equals("true")){
                                        String accountType = savedValues.getAccountType();

                                        switch (accountType) {
                                            case "patient":
                                                startActivity(new Intent(MainActivity.this, PatientHomeActivity.class));
                                                finish();
                                                break;
                                            case "doctor":
                                                //
                                                break;
                                            case "nutritionist":
                                                //
                                                break;
                                            case "medical student":
                                                //
                                                break;
                                        }
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                                        intent.putExtra("error_message", "");
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.d("responseError", volleyError.toString());

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

                                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                                    intent.putExtra("error_message", message);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                    stringRequest.setRetryPolicy(retryPolicy);

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    intent.putExtra("error_message", "No internet connection!");
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public static SavedValues getSavedValues() {
        return savedValues;
    }
}
