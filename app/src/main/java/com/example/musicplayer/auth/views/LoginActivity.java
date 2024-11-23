package com.org.tunestream.auth.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.org.tunestream.SceneDelegate;
import com.org.tunestream.databinding.ActivityLoginBinding;
import com.org.tunestream.firebase_manager.FireStoreManager;
import com.org.tunestream.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onSignupTapped(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }

    // Login button click handler
    public void onLoginTapped(View view) {
        String email = binding.txtEmail.getText().toString().trim();
        String password = binding.txtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showAlert("Please enter your username.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showAlert("Please enter your password.");
            return;
        }

        // Firestore login logic
        FireStoreManager.shared.login(this, email.toLowerCase(), password, success -> {
            if (success) {
                //SceneDelegate.getInstance().loginCheckOrRestart();
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                showAlert("Invalid username or password.");
            }
        });
    }


    // Method to show an alert as a Toast (alternative to iOS UIAlertController)
    private void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

