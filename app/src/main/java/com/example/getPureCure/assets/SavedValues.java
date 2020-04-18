package com.example.getPureCure.assets;

import android.content.SharedPreferences;

public class SavedValues {
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    public SavedValues(SharedPreferences sharedpreferences) {
        this.sharedpreferences = sharedpreferences;
        this.editor = sharedpreferences.edit();
        editor.apply();
    }

    public String getAccountId() {
        return sharedpreferences.getString("ACCOUNT_ID", "N/A");
    }

    public void setAccountId(String accountId) {
        editor.putString("ACCOUNT_ID", accountId);
        editor.apply();
    }

    public String getAccountToken() {
        return sharedpreferences.getString("ACCOUNT_TOKEN", "N/A");
    }

    public void setAccountToken(String accountToken) {
        editor.putString("ACCOUNT_TOKEN", accountToken);
        editor.apply();
    }

    public String getAccountType() {
        return sharedpreferences.getString("ACCOUNT_TYPE", "N/A");
    }

    public void setAccountType(String accountType) {
        editor.putString("ACCOUNT_TYPE", accountType);
        editor.apply();
    }

    public String getAccountName() {
        return sharedpreferences.getString("ACCOUNT_NAME", "N/A");
    }

    public void setAccountName(String accountName) {
        editor.putString("ACCOUNT_NAME", accountName);
        editor.apply();
    }

    public String getAccountPhotoUri() {
        return sharedpreferences.getString("ACCOUNT_PHOTO_URI", "N/A");
    }

    public void setAccountPhotoUri(String accountPhotoUri) {
        editor.putString("ACCOUNT_PHOTO_URI", accountPhotoUri);
        editor.apply();    }
}
