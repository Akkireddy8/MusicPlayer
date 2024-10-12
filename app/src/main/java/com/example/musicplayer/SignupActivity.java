package com.example.tunestream;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.R;

public class SignupActivity extends AppCompatActivity {
    private EditText usernameSignup, email, passwordSignup, retypePassword;
    private Button signupButton;
    private TextView loginPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        usernameSignup = findViewById(R.id.username_signup);
        email = findViewById(R.id.email);
        passwordSignup = findViewById(R.id.password_signup);
        retypePassword = findViewById(R.id.retype_password);
        signupButton = findViewById(R.id.signup_button);
        loginPrompt = findViewById(R.id.login_prompt);

        // Sign-up button listener (Navigates to LoginActivity after sign-up logic)
        signupButton.setOnClickListener(v -> {
            // Add sign-up logic here
            Intent intent = new Intent(SignupActivity.this, com.example.tunestream.LoginActivity.class);
            startActivity(intent);
        });

        // Login prompt listener
        loginPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, com.example.tunestream.LoginActivity.class);
            startActivity(intent);
        });
    }
}
