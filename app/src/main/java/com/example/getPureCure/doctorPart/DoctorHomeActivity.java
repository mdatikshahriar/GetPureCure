package com.example.getPureCure.doctorPart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.getPureCure.SignInActivity;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.assets.SavedValues;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DoctorHomeActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;

    private static Dialog toastMessageDialog;

    private String accountId, accountToken;

    private NavController navController;

    private int fragmentId;

    private SavedValues savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        BottomNavigationView navView = findViewById(R.id.doctor_nav_view);

        swipeRefreshLayout = findViewById(R.id.activity_doctor_home_SwipeRefreshLayout);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_doctor_home, R.id.navigation_doctor_blog, R.id.navigation_doctor_doctors, R.id.navigation_doctor_hospitals, R.id.navigation_doctor_shop)
                .build();
        navController = Navigation.findNavController(this, R.id.doctor_nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        Toolbar toolbar = findViewById(R.id.doctorToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toastMessageDialog = new Dialog(DoctorHomeActivity.this);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                fragmentId = destination.getId();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                navController.navigate(fragmentId);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.options_refresh){
            navController.navigate(fragmentId);

        } else if (id == R.id.option_account){
            startActivity(new Intent(DoctorHomeActivity.this, ShowOwnAccountActivity.class));

        } else if (id == R.id.option_settings){
            //
        } else if (id == R.id.option_signOut){
            getSavedValues();
            accountId = savedValues.getAccountId();
            accountToken = savedValues.getAccountToken();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, API.getSign_out(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);

                            savedValues.setAccountId("N/A");
                            savedValues.setAccountToken("N/A");
                            startActivity(new Intent(DoctorHomeActivity.this, SignInActivity.class));
                            finish();
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
                    params.put("id", accountId);
                    params.put("token", accountToken);
                    return params;
                }
            };

            stringRequest.setRetryPolicy(MainActivity.getRetryPolicy());

            RequestQueue requestQueue = Volley.newRequestQueue(DoctorHomeActivity.this);
            requestQueue.add(stringRequest);
        }
        return true;
    }

    private void getSavedValues() {
        SharedPreferences sharedPreferences = getSharedPreferences("GetPureCure_SharedPreferences", Context.MODE_PRIVATE);
        savedValues = new SavedValues(sharedPreferences);
    }

    public static void showToastMessage(final String message) {
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
