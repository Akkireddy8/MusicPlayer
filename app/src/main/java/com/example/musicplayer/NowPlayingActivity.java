package com.example.musicplayer;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NowPlayingActivity extends AppCompatActivity {

    private TextView trackName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        // Get the selected song name from Intent
        String songName = getIntent().getStringExtra("song_name");

        // Display the song name
        trackName = findViewById(R.id.track_name);
        trackName.setText(songName);
    }
}