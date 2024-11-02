package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView signUpPrompt;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Ensure this matches your login layout file

        // Initialize EditText fields and login button
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        // Initialize the "Sign Up" prompt TextView
        signUpPrompt = findViewById(R.id.sign_up_prompt);

        // Set click listener for "Sign Up" prompt
        signUpPrompt.setOnClickListener(v -> {
            // Redirect to SignUpActivity when "Sign Up" is clicked
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        // Set click listener for the "Login" button
        loginButton.setOnClickListener(v -> {
            // Get the username and password input from the EditText fields
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Basic check to ensure fields are not empty
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                // For now, assume the login is successful and redirect to PlaylistActivity
                Intent intent = new Intent(LoginActivity.this, com.example.musicplayer.PlaylistActivity.class);
                startActivity(intent);
            }
        });
    }
}
