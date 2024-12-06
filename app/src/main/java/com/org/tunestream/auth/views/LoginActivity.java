package com.org.tunestream.auth.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

        if (!validate(email, password)) {
            return;
        }

        FireStoreManager.shared.login(this, email.toLowerCase(), password, success -> {
            if (success) {
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                showAlert("Invalid username or password.");
            }
        });
    }

    private boolean validate(String email, String password) {
        // Email validation
        if (TextUtils.isEmpty(email)) {
            showAlert("Please enter your email.");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showAlert("Please enter a valid email address.");
            return false;
        }

        // Password validation
        if (TextUtils.isEmpty(password)) {
            showAlert("Please enter your password.");
            return false;
        }
        if (password.length() < 6) {
            showAlert("Password must be at least 6 characters long.");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
