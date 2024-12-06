package com.org.tunestream.firebase_manager;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDefaultsManager {
    private static final UserDefaultsManager shared = new UserDefaultsManager();
    private static final String PREFERENCES_NAME = "user_preferences";
    private SharedPreferences sharedPreferences;

    private UserDefaultsManager() {
        // Private constructor to enforce singleton pattern
    }

    public static UserDefaultsManager getInstance(Context context) {
        shared.sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return shared;
    }

    public void clearUserDefaults() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        String email = getEmail();
        return !email.isEmpty();
    }

    public String getEmail() {
        return sharedPreferences.getString("email", "").toLowerCase();
    }

    public String getName() {
        return sharedPreferences.getString("name", "");
    }

    public String getUserName() {
        return sharedPreferences.getString("username", "");
    }

    public String getPhone() {
        return sharedPreferences.getString("phone", "");
    }

    public String getPassword() {
        return sharedPreferences.getString("password", "");
    }

    public String getDocumentId() {
        return sharedPreferences.getString("documentId", "");
    }

    public void setDocumentId(String documentId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("documentId", documentId);
        editor.apply();
    }

    public void saveData(String name, String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    public void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("email");
        editor.remove("name");
        editor.remove("password");
        editor.remove("documentId");
        editor.apply();
    }

    public void saveFavourite(String title) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getEmail().toLowerCase() + title.toLowerCase(), true);
        editor.apply();
    }

    public boolean getFavorites(String title) {
        return sharedPreferences.getBoolean(getEmail().toLowerCase() + title.toLowerCase(), false);
    }

    public void removeFavorite(String title) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(getEmail().toLowerCase() + title.toLowerCase());
        editor.apply();
    }
}

