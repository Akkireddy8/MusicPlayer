package com.example.tunestream;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;

public class PlaylistActivity extends AppCompatActivity {
    private RecyclerView playlistRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        // Initialize RecyclerView
        playlistRecyclerView = findViewById(R.id.playlist_recycler_view);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add adapter and data source for RecyclerView here
        // playlistRecyclerView.setAdapter(new PlaylistAdapter(...));
    }
}
