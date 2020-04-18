package com.example.getPureCure.patientPart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.getPureCure.assets.API;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ShowSingleDoctorActivity extends AppCompatActivity {

    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;

    private ImageView profilePhotoCircularImageView;

    private TextView doctorNameTextView, doctorCategoryTextView, doctorAboutTextView, doctorCategory2TextView,
            doctorBmdcCodeTextView, doctorEmailTextView, doctorPhoneTextView, doctorChamberAddressTextView,
            doctorVisitingTimeTextView;

    private LinearLayout doctorDegreesLinearLayout;

    private Dialog toastMessageDialog;

    private String userId, phoneNo, emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_doctor);

        nestedScrollView = findViewById(R.id.activity_show_single_doctor_NestedScrollView);
        progressBar = findViewById(R.id.activity_show_single_doctor_ProgressBar);

        profilePhotoCircularImageView = findViewById(R.id.activity_show_single_doctor_profilePhoto_CircularImageView);

        doctorNameTextView = findViewById(R.id.activity_show_single_doctor_doctorName_TextView);
        doctorCategoryTextView = findViewById(R.id.activity_show_single_doctor_doctorCategory_TextView);
        doctorAboutTextView = findViewById(R.id.activity_show_single_doctor_doctorAbout_TextView);
        doctorCategory2TextView = findViewById(R.id.activity_show_single_doctor_doctorCategory2_TextView);
        doctorBmdcCodeTextView = findViewById(R.id.activity_show_single_doctor_doctorBmdcCode_TextView);
        doctorEmailTextView = findViewById(R.id.activity_show_single_doctor_doctorEmail_TextView);
        doctorPhoneTextView = findViewById(R.id.activity_show_single_doctor_doctorPhone_TextView);
        doctorChamberAddressTextView = findViewById(R.id.activity_show_single_doctor_doctorChamberAddress_TextView);
        doctorVisitingTimeTextView = findViewById(R.id.activity_show_single_doctor_doctorVisitingTime_TextView);

        doctorDegreesLinearLayout = findViewById(R.id.activity_show_single_doctor_doctorDegrees_LinearLayout);

        Button callButton = findViewById(R.id.activity_show_single_doctor_call_Button);
        Button messageButton = findViewById(R.id.activity_show_single_doctor_message_Button);
        Button emailButton = findViewById(R.id.activity_show_single_doctor_email_Button);

        toastMessageDialog = new Dialog(ShowSingleDoctorActivity.this);

        Intent intent = getIntent();

        userId = intent.getStringExtra("user_id");

        loadDoctor();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNo, null));
                startActivity(intent);
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNo, null));
                startActivity(intent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailAddress, null));
                startActivity(Intent.createChooser(intent, "Send email..."));
            }
        });

    }

    private void loadDoctor() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest doctorStringRequest = new StringRequest(Request.Method.GET, API.getGet_specific_doctors(userId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);

                        try {
                            JSONObject doctorObject = new JSONObject(response);

                            JSONObject userObject = doctorObject.getJSONObject("user_id");

                            String imageUri = userObject.getString("photo").trim();

                            if (imageUri.isEmpty()){
                                Picasso.get().load(R.drawable.ic_account)
                                        .centerCrop()
                                        .fit()
                                        .error(R.drawable.ic_account)
                                        .into(profilePhotoCircularImageView);
                            } else {
                                Picasso.get().load(imageUri)
                                        .centerCrop()
                                        .fit()
                                        .error(R.drawable.ic_account)
                                        .into(profilePhotoCircularImageView);
                            }

                            doctorNameTextView.setText(userObject.getString("name").trim());
                            doctorCategoryTextView.setText(doctorObject.getString("category").trim().toUpperCase());

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                doctorAboutTextView.setText(Html.fromHtml(doctorObject.getString("about").trim(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                doctorAboutTextView.setText(Html.fromHtml(doctorObject.getString("about").trim()));
                            }

                            doctorCategory2TextView.setText(doctorObject.getString("category").trim());

                            if (doctorObject.has("bmdc_code")) {
                                doctorBmdcCodeTextView.setText(doctorObject.getString("bmdc_code").trim());
                            }

                            JSONArray degreeArray = doctorObject.getJSONArray("degrees");

                            for (int i = 0; i < degreeArray.length(); i++) {
                                TextView textView = new TextView(ShowSingleDoctorActivity.this);

                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(10, 0, 10, 0);

                                textView.setLayoutParams(layoutParams);
                                textView.setText(degreeArray.getString(i).trim().toUpperCase());
                                textView.setBackgroundResource(R.drawable.primary_rounded_rectangle);
                                textView.setPadding(20,10,20,10);
                                doctorDegreesLinearLayout.addView(textView);
                            }

                            phoneNo = doctorObject.getString("contact").trim();
                            emailAddress = userObject.getString("email").trim();

                            doctorEmailTextView.setText(emailAddress);
                            doctorPhoneTextView.setText(phoneNo);
                            doctorChamberAddressTextView.setText(doctorObject.getString("chamber_address").trim());
                            doctorVisitingTimeTextView.setText(doctorObject.getString("visiting_time").trim());

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

        doctorStringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

        requestQueue.add(doctorStringRequest);
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
