package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.R;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private Button loginButton;
    private TextView signUpPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        signUpPrompt = findViewById(R.id.sign_up_prompt);

        // Login button listener (For now, simply navigates to MainActivity)
        loginButton.setOnClickListener(v -> {
            // You can add login validation here
            Intent intent = new Intent(LoginActivity.this, com.example.musicplayer.MainActivity.class);
            startActivity(intent);
        });

        // Sign-up prompt listener
        signUpPrompt.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, com.example.musicplayer.SignupActivity.class);
            startActivity(intent);
        });
    }
}
