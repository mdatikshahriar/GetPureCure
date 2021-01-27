package com.example.getPureCure.doctorPart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.getPureCure.MainActivity;
import com.example.getPureCure.R;
import com.example.getPureCure.assets.API;
import com.example.getPureCure.assets.SavedValues;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewBlogActivity extends AppCompatActivity {

    private Dialog toastMessageDialog;
    private ProgressBar progressBar;

    private TextView blogImageFileNameTextView;

    private EditText blogTitleEditText, blogContentEditText, blogTagsEditText;

    private Spinner blogCategorySpinner;

    private Button blogImageButton, postBlogButton;

    private ImageView blogImageImageView;

    private ArrayList<String> blogCategoriesArrayList;

    private String blogCategory = null, image_document = null, image_url = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_blog);

        progressBar = findViewById(R.id.activity_new_blog_ProgressBar);

        toastMessageDialog = new Dialog(NewBlogActivity.this);

        blogImageFileNameTextView = findViewById(R.id.activity_new_blog_blogImageFileName_TextView);

        blogTitleEditText = findViewById(R.id.activity_new_blog_blogTitle_EditText);
        blogContentEditText = findViewById(R.id.activity_new_blog_blogContent_EditText);
        blogTagsEditText = findViewById(R.id.activity_new_blog_blogTags_EditText);

        blogCategorySpinner = findViewById(R.id.activity_new_blog_blogCategory_Spinner);

        blogImageButton = findViewById(R.id.activity_new_blog_blogImage_Button);
        postBlogButton = findViewById(R.id.activity_new_blog_postBlog_Button);

        blogImageImageView = findViewById(R.id.activity_new_blog_blogImage_ImageView);

        blogCategoriesArrayList = new ArrayList<>();

        loadCategories();

        blogCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                blogCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        blogImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        postBlogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title, content, tags;

                title = blogTitleEditText.getText().toString();
                content = blogContentEditText.getText().toString();
                tags = blogTagsEditText.getText().toString();

                if (title.isEmpty()) {
                    blogTitleEditText.setError("Please enter the Blog title");
                    return;
                } else if (blogCategory.isEmpty()) {
                    Toast.makeText(NewBlogActivity.this, "Please select a category for the blog.", Toast.LENGTH_LONG).show();
                    return;
                } else if (content.isEmpty()) {
                    blogContentEditText.setError("Please write some content for the blog");
                    return;
                } else if (tags.isEmpty()) {
                    blogTagsEditText.setError("Please write at least one tag");
                    return;
                } else if (image_url == null) {
                    Toast.makeText(NewBlogActivity.this, "Please upload an image for the cover of the blog.", Toast.LENGTH_LONG).show();
                    return;
                } else if (image_url.isEmpty()) {
                    Toast.makeText(NewBlogActivity.this, "Please upload an image for the cover of the blog.", Toast.LENGTH_LONG).show();
                    return;
                }

                final String accountId = getSavedValues().getAccountId();

                Date currentTime = Calendar.getInstance().getTime();
                final String time = currentTime.toString();

                final String formattedTags = formatTags(tags);

                progressBar.setVisibility(View.VISIBLE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, API.getAdd_blog(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response", response);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status").trim();

                                    showToastMessage(status);

                                    progressBar.setVisibility(View.INVISIBLE);

                                    if (status.equals("Added Successfully")) {
                                        onBackPressed();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("catchError", e.toString());
                                    progressBar.setVisibility(View.INVISIBLE);
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

                                progressBar.setVisibility(View.INVISIBLE);
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
                        params.put("author_id", accountId);
                        params.put("title", title);
                        params.put("content", content);
                        params.put("image", image_url);
                        params.put("date", time);
                        params.put("category", blogCategory);
                        params.put("tags", formattedTags);
                        Log.d("params", params.toString());
                        return params;
                    }
                };

                RetryPolicy retryPolicy = new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

                stringRequest.setRetryPolicy(retryPolicy);

                RequestQueue requestQueue = Volley.newRequestQueue(NewBlogActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }

    private void loadCategories() {
        RequestQueue requestQueue = Volley.newRequestQueue(NewBlogActivity.this);

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

                            ArrayAdapter<String> blogCategoryAdapter = new ArrayAdapter<>(NewBlogActivity.this, android.R.layout.simple_spinner_item, blogCategoriesArrayList);
                            blogCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            blogCategorySpinner.setAdapter(blogCategoryAdapter);
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

    private String formatTags(String tags) {
        String[] tagsArray = tags.split(",");
        StringBuilder tagsString = new StringBuilder("[");
        int i = 0;
        for (String s : tagsArray) {
            tagsString.append("\"").append(s).append("\"");
            if (i < tagsArray.length - 1) {
                tagsString.append(",");
            }
            i++;
        }
        tagsString.append("]");
        return tagsString.toString();
    }

    private void selectImage() {
        final CharSequence[] options = { "Capture by Camera", "Select from Gallery", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(NewBlogActivity.this);
        builder.setTitle("Add Cover Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Capture by Camera"))
                {
                    if (ContextCompat.checkSelfPermission(NewBlogActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    }
                    else {
                        requestDevicePermission(1);
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "image.jpg");
                    Uri photoURI = FileProvider.getUriForFile(NewBlogActivity.this, getApplicationContext().getPackageName() + ".provider", f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    if (ContextCompat.checkSelfPermission(NewBlogActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        startActivityForResult(intent, 1);
                    }
                    else {
                        requestDevicePermission(1);
                    }
                }
                else if (options[item].equals("Select from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.create().show();
    }

    public void requestDevicePermission(int ans) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, 1);
            ActivityCompat.requestPermissions(NewBlogActivity.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("result", Integer.toString(requestCode));
        Log.d("result", Integer.toString(resultCode));
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : Objects.requireNonNull(f.listFiles())) {
                    if (temp.getName().equals("image.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    String fileName = f.getName() + " uploaded";
                    blogImageFileNameTextView.setText(fileName);
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 1000);
                    Toast.makeText(getApplicationContext(), "Uploading", Toast.LENGTH_SHORT).show();
                    blogImageImageView.setVisibility(View.GONE);
                    bitMapToString(bitmap);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                String fileName = picturePath.substring(picturePath.lastIndexOf("/") + 1) + " uploaded";
                blogImageFileNameTextView.setText(fileName);
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = getResizedBitmap(thumbnail, 1000);
                Log.w("path of image from", picturePath+"");
                Toast.makeText(getApplicationContext(), "Uploading", Toast.LENGTH_SHORT).show();
                blogImageImageView.setVisibility(View.GONE);
                bitMapToString(thumbnail);
            }

            uploadImage();
        }
    }

    private void uploadImage() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String URL = "https://api.imgur.com/3/image";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("image", image_document);
            final String requestBody = jsonBody.toString();

            JsonObjectRequest uploadToImgur = new JsonObjectRequest(Request.Method.POST, URL,
                    jsonBody, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject object = response.getJSONObject("data");
                        String url = object.getString("link");
                        Picasso.get()
                                .load(url)
                                .centerCrop()
                                .fit()
                                .error(R.drawable.default_blog_picture)
                                .into(blogImageImageView);

                        blogImageImageView.setVisibility(View.VISIBLE);

                        image_url = url;

                    } catch (JSONException e) {
                        e.printStackTrace();
                        blogImageImageView.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("responseError", volleyError.toString());

                    showError(volleyError);
                }
            }) {

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Client-ID ead116aab30174c");
                    return headers;
                }
            };

            uploadToImgur.setRetryPolicy(new DefaultRetryPolicy(20000, 0, 0));
            requestQueue.add(uploadToImgur);
        }catch (Exception e){
            e.printStackTrace();
            blogImageImageView.setVisibility(View.VISIBLE);
        }
    }

    public void bitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 20, baos);
        byte[] b = baos.toByteArray();
        image_document = Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private SavedValues getSavedValues() {
        SharedPreferences sharedPreferences = getSharedPreferences("GetPureCure_SharedPreferences", Context.MODE_PRIVATE);
        return new SavedValues(sharedPreferences);
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
}