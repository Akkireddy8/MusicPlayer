package com.org.tunestream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.org.tunestream.firebase_manager.UserDefaultsManager;

public class SceneDelegate extends AppCompatActivity {
    private static SceneDelegate shared;
    private Activity myActivity;

    public static SceneDelegate getInstance() {
        return shared;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shared = this;
        myActivity = this;
        loginCheckOrRestart();
    }

    public void loginCheckOrRestart() {
        boolean isLoggedIn = UserDefaultsManager.getInstance(this).isLoggedIn();
       /* Intent intent;
        if (isLoggedIn) {
            intent = new Intent(this, HomeMainNavigationActivity.class);
        } else {
            intent = new Intent(this, HomeNavigationActivity.class);
        }
        startActivity(intent);*/
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Called when the activity is becoming visible to the user.
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Called when the activity will start interacting with the user.
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Called when the system is about to put the activity into the background.
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Called when the activity is no longer visible to the user.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Called before the activity is destroyed.
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Called after the activity has been stopped, just prior to it being started again.
    }
}

