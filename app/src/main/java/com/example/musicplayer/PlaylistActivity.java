package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView playlistRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private List<Playlist> playlistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        // Initialize RecyclerView
        playlistRecyclerView = findViewById(R.id.playlist_recycler_view);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data for playlists
        playlistList = new ArrayList<>();
        playlistList.add(new Playlist("Song 1", "Description 1"));
        playlistList.add(new Playlist("Song 2", "Description 2"));
        playlistList.add(new Playlist("Song 3", "Description 3"));

        // Initialize Adapter
        playlistAdapter = new PlaylistAdapter(playlistList, this::onSongSelected);
        playlistRecyclerView.setAdapter(playlistAdapter);
    }

    // Method to handle song selection
    private void onSongSelected(Playlist playlist) {
        Intent intent = new Intent(PlaylistActivity.this, NowPlayingActivity.class);
        intent.putExtra("song_name", playlist.getName());
        startActivity(intent);
    }
}

