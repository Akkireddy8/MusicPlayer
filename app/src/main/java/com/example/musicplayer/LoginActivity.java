package com.example.musicplayer;

//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.musicplayer.R;
//
//public class LoginActivity extends AppCompatActivity {
//    private EditText username, password;
//    private Button loginButton;
//    private TextView signUpPrompt;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        // Initialize UI elements
//        username = findViewById(R.id.username);
//        password = findViewById(R.id.password);
//        loginButton = findViewById(R.id.login_button);
//        signUpPrompt = findViewById(R.id.sign_up_prompt);
//
//        // Login button listener (For now, simply navigates to MainActivity)
//        loginButton.setOnClickListener(v -> {
//            // You can add login validation here
//            Intent intent = new Intent(LoginActivity.this, com.example.musicplayer.MainActivity.class);
//            startActivity(intent);
//        });
//
//        // Sign-up prompt listener
//        signUpPrompt.setOnClickListener(v -> {
//            Intent intent = new Intent(LoginActivity.this, com.example.musicplayer.SignupActivity.class);
//            startActivity(intent);
//        });
//    }
//}

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView signUpPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // Ensure this matches your login layout file

        // Initialize the "Sign Up" prompt TextView
        signUpPrompt = findViewById(R.id.sign_up_prompt);

        // Set click listener for "Sign Up" prompt
        signUpPrompt.setOnClickListener(v -> {
            // Redirect to SignUpActivity when "Sign Up" is clicked
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}