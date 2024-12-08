package com.org.tunestream;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.org.tunestream.auth.views.LoginActivity;
import com.org.tunestream.home.HomeActivity;

public class MainApp extends Application implements ViewModelStoreOwner {

    private ViewModelStore appViewModelStore;

    @Override
    public void onCreate() {
        super.onCreate();
        appViewModelStore = new ViewModelStore();
        SharedPreferences sharedPreferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        checkLogin(email);
    }

    private void checkLogin(String email) {
        Intent intent;
        if (email.isEmpty()) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, HomeActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return appViewModelStore;
    }
}
