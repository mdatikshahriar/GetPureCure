package com.example.getPureCure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.getPureCure.assets.API;
import com.example.getPureCure.assets.SavedValues;
import com.example.getPureCure.doctorPart.DoctorHomeActivity;
import com.example.getPureCure.patientPart.PatientHomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    private Dialog toastMessageDialog;

    private SavedValues savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        toastMessageDialog = new Dialog(SignInActivity.this);

        Intent intent = getIntent();

        final String error_message = intent.getStringExtra("error_message");

        if(error_message != null && !error_message.isEmpty()) {
            showToastMessage(error_message);
        }

        final EditText emailEditText, passwordEditText;
        TextView forgotPasswordTextView, signUpTextView;

        emailEditText = findViewById(R.id.activity_sign_in_email_EditText);
        passwordEditText = findViewById(R.id.activity_sign_in_password_EditText);
        Button signInButton = findViewById(R.id.activity_sign_in_signIn_Button);
        forgotPasswordTextView = findViewById(R.id.activity_sign_in_forgotPassword_TextView);
        signUpTextView = findViewById(R.id.activity_sign_in_signUp_TextView);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email, password;

                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if(email.isEmpty()){
                    emailEditText.setError("Please enter something");
                    return;
                }else if (password.isEmpty()){
                    passwordEditText.setError("Please enter something");
                    return;
                }  else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailEditText.setError("Invalid email address");
                    return;
                } else if(password.length() < 6) {
                    passwordEditText.setError("Password length can't be less than 6");
                    return;
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, API.getSign_in(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    JSONObject userObject = jsonObject.getJSONObject("user");

                                    String accountId = userObject.getString("_id").trim();
                                    String accountToken = jsonObject.getString("token").trim();
                                    String accountType = userObject.getString("type").trim();
                                    String accountName = userObject.getString("name").trim();
                                    String accountPhotoUri = userObject.getString("photo").trim();

                                    getSavedValues();

                                    savedValues.setAccountId(accountId);
                                    savedValues.setAccountToken(accountToken);
                                    savedValues.setAccountType(accountType);
                                    savedValues.setAccountName(accountName);
                                    savedValues.setAccountPhotoUri(accountPhotoUri);

                                    Log.d("user", accountType);

                                    switch (accountType) {
                                        case "patient":
                                            startActivity(new Intent(SignInActivity.this, PatientHomeActivity.class));
                                            finish();
                                            break;
                                        case "doctor":
                                            startActivity(new Intent(SignInActivity.this, DoctorHomeActivity.class));
                                            finish();
                                            break;
                                        case "nutritionist":

                                            break;
                                        case "medical student":

                                            break;
                                    }
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
                        })
                {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("password", password);
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

                RequestQueue requestQueue = Volley.newRequestQueue(SignInActivity.this);
                requestQueue.add(stringRequest);
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    private void getSavedValues() {
        SharedPreferences sharedPreferences = getSharedPreferences("GetPureCure_SharedPreferences", Context.MODE_PRIVATE);
        savedValues = new SavedValues(sharedPreferences);
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
                Log.d("tag", "Handler finished");
            }
        }, 5000);
    }
}
