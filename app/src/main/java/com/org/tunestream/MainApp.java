package com.org.tunestream;

import android.app.Application;
import android.content.Intent;

import com.org.tunestream.auth.views.LoginActivity;
import com.org.tunestream.firebase_manager.UserDefaultsManager;
import com.org.tunestream.home.HomeActivity;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*UserDefaultsManager userDefaultsManager = UserDefaultsManager.getInstance(getBaseContext());
        if (!userDefaultsManager.isLoggedIn())
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        else startActivity(new Intent(getBaseContext(), HomeActivity.class));*/
    }
}
