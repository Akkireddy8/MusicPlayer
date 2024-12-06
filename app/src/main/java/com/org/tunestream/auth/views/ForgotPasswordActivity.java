package com.org.tunestream.auth.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.org.tunestream.databinding.ActivityForgotPasswordBinding;
import com.org.tunestream.firebase_manager.FireStoreManager;


public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    // Send button click handler
    public void onSend(View view) {
        String email = binding.email.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showAlert("Please enter your email id.");
            return;
        }

        // Firestore logic for password retrieval
        FireStoreManager.shared.getPassword(this, email.toLowerCase(), "", password -> {
            forgotPassword(password);
        }
        );
    }

    // Handle password display
    private void forgotPassword(String password) {
        String body = "<h1>Your password is " + password + "</h1>";

        // Show a loading spinner
        ProgressBar loadingIndicator = new ProgressBar(this);
        loadingIndicator.setIndeterminate(true);
        binding.getRoot().addView(loadingIndicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        // Stop loading spinner after a delay (simulating network call)
        loadingIndicator.postDelayed(() -> {
            loadingIndicator.setVisibility(View.GONE);
            showAlert("Password retrieved successfully. Check your email.");
        }, 2000);
    }

    // Helper method to show alert messages
    private void showAlert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

