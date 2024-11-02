package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnPlaylist, btnProfile, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnPlaylist = findViewById(R.id.btn_playlist);
        btnProfile = findViewById(R.id.btn_profile);
        btnLogin = findViewById(R.id.btn_login);

        // Set click listener for Playlist button
        btnPlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlaylistActivity.class);
            startActivity(intent);
        });

        // Set click listener for Profile button
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserProfile.class);
            startActivity(intent);
        });

        // Set click listener for Login button
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
