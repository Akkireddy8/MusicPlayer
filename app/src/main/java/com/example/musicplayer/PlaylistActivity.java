package com.example.musicplayer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView playlistRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        playlistRecyclerView = findViewById(R.id.playlist_recycler_view);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
