package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.R;

public class MainActivity extends AppCompatActivity {
    private Button btnPlaylist, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btnPlaylist = findViewById(R.id.btn_playlist);
        btnProfile = findViewById(R.id.btn_profile);

        // Set click listener for Playlist button
        btnPlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.tunestream.PlaylistActivity.class);
            startActivity(intent);
        });

        // Set click listener for Profile button
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.example.tunestream.ProfileActivity.class);
            startActivity(intent);
        });
    }
}
