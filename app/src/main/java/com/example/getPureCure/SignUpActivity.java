package com.example.getPureCure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.getPureCure.patientPart.PatientHomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Dialog toastMessageDialog;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toastMessageDialog = new Dialog(SignUpActivity.this);

        final EditText nameEditText, emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText;

        nameEditText = findViewById(R.id.activity_sign_up_name_EditText);
        emailEditText = findViewById(R.id.activity_sign_up_email_EditText);
        phoneEditText = findViewById(R.id.activity_sign_up_phone_EditText);
        passwordEditText = findViewById(R.id.activity_sign_up_password_EditText);
        confirmPasswordEditText = findViewById(R.id.activity_sign_up_confirmPassword_EditText);
        Button signUpButton = findViewById(R.id.activity_sign_up_signUp_Button);
        TextView signInTextView = findViewById(R.id.activity_sign_up_signIn_TextView);
        Spinner accountTypeSpinner = findViewById(R.id.activity_sign_up_accountType_Spinner);

        ArrayAdapter<CharSequence> accountTypeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.account_types_array, android.R.layout.simple_spinner_item);
        accountTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(accountTypeSpinnerAdapter);
        accountTypeSpinner.setOnItemSelectedListener(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name, email, phone, password, confirmPassword;

                name = nameEditText.getText().toString();
                email = emailEditText.getText().toString();
                phone = phoneEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();

                if(name.isEmpty()) {
                    nameEditText.setError("Please enter something");
                    return;
                } else if(email.isEmpty()) {
                    emailEditText.setError("Please enter something");
                    return;
                } else if(phone.isEmpty()) {
                    passwordEditText.setError("Please enter something");
                    return;
                } else if(password.isEmpty()) {
                    passwordEditText.setError("Please enter something");
                    return;
                } else if(confirmPassword.isEmpty()) {
                    confirmPasswordEditText.setError("Please enter something");
                    return;
                } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailEditText.setError("Invalid email address");
                    return;
                } else if(password.length() < 6) {
                    passwordEditText.setError("Password length can't be less than 6");
                    return;
                } else if(!password.equals(confirmPassword)) {
                    showToastMessage("Password didn't match");
                    return;
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, API.getSign_up(),
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

                                    MainActivity.getSavedValues().setAccountId(accountId);
                                    MainActivity.getSavedValues().setAccountToken(accountToken);
                                    MainActivity.getSavedValues().setAccountType(accountType);
                                    MainActivity.getSavedValues().setAccountName(accountName);
                                    MainActivity.getSavedValues().setAccountPhotoUri(accountPhotoUri);

                                    switch (accountType) {
                                        case "patient":
                                            startActivity(new Intent(SignUpActivity.this, PatientHomeActivity.class));
                                            finish();
                                            break;
                                        case "doctor":

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
                                if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 500) {
                                    message = "Email already exist";
                                } else if (volleyError.networkResponse != null) {
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
                        params.put("name", name);
                        params.put("email", email);
                        params.put("phone", phone);
                        params.put("password", password);
                        params.put("type", type);
                        return params;
                    }
                };

                stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

                RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
                requestQueue.add(stringRequest);
            }
        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
