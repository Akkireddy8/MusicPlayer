package com.org.tunestream.auth.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.org.tunestream.databinding.ActivitySignupBinding;
import com.org.tunestream.firebase_manager.FireStoreManager;
import com.org.tunestream.firebase_manager.UserDefaultsManager;
import com.org.tunestream.home.HomeActivity;

public class SignupActivity extends AppCompatActivity implements FireStoreManager.FireStoreCallback<Boolean> {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onSignUp(View view) {
        if (validate()) {
            FireStoreManager.shared.signUp(this,
                    binding.email.getText().toString().trim(),
                    binding.username.getText().toString().trim(),
                    binding.password.getText().toString().trim(), this
            );
        }
    }

    // Login button click handler (navigate back)
    public void onLogin(View view) {
        finish(); // Simulates navigationController.popViewController(animated: true)
    }

    // Validation method
    private boolean validate() {
        String username = binding.username.getText().toString().trim();
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        // Username validation
        if (TextUtils.isEmpty(username)) {
            showToast("Please enter your name.");
            return false;
        }
        if (username.length() < 3) {
            showToast("Name must be at least 3 characters long.");
            return false;
        }

        // Email validation
        if (TextUtils.isEmpty(email)) {
            showToast("Please enter your email.");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email address.");
            return false;
        }

        // Password validation
        if (TextUtils.isEmpty(password)) {
            showToast("Please enter a password.");
            return false;
        }
        if (password.length() < 6) {
            showToast("Password must be at least 6 characters long.");
            return false;
        }
        if (!containsUppercase(password)) {
            showToast("Password must contain at least one uppercase letter.");
            return false;
        }
        if (!containsDigit(password)) {
            showToast("Password must contain at least one digit.");
            return false;
        }

        return true;
    }

    // Helper method to show Toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Utility method to check if string contains at least one uppercase letter
    private boolean containsUppercase(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    // Utility method to check if string contains at least one digit
    private boolean containsDigit(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCallback(Boolean result) {
        if (result) {
            showToast("Signup successful.");
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            showToast("Signup failed. Please try again.");
        }
    }
}
