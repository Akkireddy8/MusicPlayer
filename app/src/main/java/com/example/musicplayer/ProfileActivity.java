package com.example.tunestream;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.R;

public class ProfileActivity extends AppCompatActivity {
    private TextView trackName;
    private Button prevButton, playButton, nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI elements
        trackName = findViewById(R.id.track_name);
        prevButton = findViewById(R.id.prev_button);
        playButton = findViewById(R.id.play_button);
        nextButton = findViewById(R.id.next_button);

        // Add button functionality here
        // e.g., playButton.setOnClickListener(...)
    }
}

