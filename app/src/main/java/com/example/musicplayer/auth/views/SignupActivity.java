package com.org.tunestream.auth.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.org.tunestream.databinding.ActivitySignupBinding;
import com.org.tunestream.firebase_manager.FireStoreManager;
import com.org.tunestream.home.HomeActivity;

public class SignupActivity extends AppCompatActivity implements FireStoreManager.FireStoreCallback<Boolean> {

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    // Sign Up button click handler
    public void onSignUp(View view) {
        if (validate()) {
            // Firestore signup logic
            FireStoreManager.shared.signUp(
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
        if (TextUtils.isEmpty(binding.username.getText().toString().trim())) {
            showAlert("Please enter name.");
            return false;
        }

        if (TextUtils.isEmpty(binding.email.getText().toString().trim())) {
            showAlert("Please enter email.");
            return false;
        }

        if (TextUtils.isEmpty(binding.password.getText().toString().trim())) {
            showAlert("Please enter password.");
            return false;
        }

        return true;
    }

    // Helper method to show alert
    private void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCallback(Boolean result) {
        if (result) {
            showAlert("Signup successful.");
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            showAlert("Signup failed.");
        }
    }
}

