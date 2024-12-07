package com.org.tunestream.playlist.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.org.tunestream.DialogUtils;
import com.org.tunestream.R;
import com.org.tunestream.adapters.SongsDetailsAdapter;
import com.org.tunestream.databinding.ActivitySongsDetailsBinding;
import com.org.tunestream.databinding.CustomAppBarBinding;
import com.org.tunestream.player.PlayerBaseActivity;
import com.org.tunestream.interfaces.OnSongClickInterface;
import com.org.tunestream.models.Playlist;
import com.org.tunestream.models.Song;
import com.org.tunestream.viewmodels.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SongsDetailsActivity extends PlayerBaseActivity implements OnSongClickInterface {

    private ActivitySongsDetailsBinding songsDetailsBinding;
    private CustomAppBarBinding customAppBarBinding;
    private Playlist playlist;
    private List<Song> songs = new ArrayList<>();
    private String moveFrom = "";
    private SongsDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songsDetailsBinding = ActivitySongsDetailsBinding.inflate(getLayoutInflater());
        ViewGroup viewGroup = findViewById(R.id.activity_content);
        viewGroup.addView(songsDetailsBinding.getRoot());

        customAppBarBinding = songsDetailsBinding.customAppBar;
        customAppBarBinding.backImage.setOnClickListener(v -> {
            finish();
        });
        customAppBarBinding.titleText.setText(getString(R.string.music));

        songsDetailsBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SongsDetailsAdapter(songs, this);
        songsDetailsBinding.recyclerView.setAdapter(adapter);

        if (getIntent().hasExtra("moveFrom"))
            moveFrom = getIntent().getStringExtra("moveFrom");

        if (moveFrom != null && moveFrom.equals("Shared")) {
            songsDetailsBinding.addMusic.setVisibility(View.GONE);
            songsDetailsBinding.shareMusic.setVisibility(View.GONE);
        }
        if (getIntent().hasExtra("playlist")) {
            playlist = (Playlist) getIntent().getSerializableExtra("playlist");
            songsDetailsBinding.playListTitle.setText(playlist.getName());
            if (!playlist.getDocumentId().isEmpty()) {
                if (playlist.getSongs() != null && !playlist.getSongs().isEmpty()) {
                    songsDetailsBinding.recyclerView.setVisibility(View.VISIBLE);
                    songsDetailsBinding.noSongs.setVisibility(View.GONE);
                    getSongs();
                } else {
                    songsDetailsBinding.recyclerView.setVisibility(View.GONE);
                    songsDetailsBinding.noSongs.setVisibility(View.VISIBLE);
                    songsDetailsBinding.shareMusic.setVisibility(View.GONE);
                }
            }
        }
    }

    public void onShareMusic(View view) {
        DialogUtils.showCustomAlertDialog(this, "Enter Email", "Please enter user email address", playlist.getDocumentId(), playlist.getName());
    }

    public void onAddMusic(View view) {
        Intent intent = new Intent(this, AddSongActivity.class);
        intent.putExtra("playListDocumentId", playlist.getDocumentId());
        intent.putExtra("playListName", playlist.getName());
        startActivity(intent);
    }

    private void getSongs() {
        ViewModel.getInstance().getSongs(this, playlist.getDocumentId(), songs -> {
            if (songs != null && !songs.isEmpty()) {
                songsDetailsBinding.recyclerView.setVisibility(View.VISIBLE);
                songsDetailsBinding.noSongs.setVisibility(View.GONE);
                if (moveFrom != null && moveFrom.equals("Shared"))
                    songsDetailsBinding.shareMusic.setVisibility(View.GONE);
                else songsDetailsBinding.shareMusic.setVisibility(View.VISIBLE);

                this.songs.clear();
                this.songs.addAll(songs);
                songsDetailsBinding.numberOfSongs.setText(this.songs.size() + " Songs");
                adapter.notifyDataSetChanged();
            } else {
                songsDetailsBinding.recyclerView.setVisibility(View.GONE);
                songsDetailsBinding.noSongs.setVisibility(View.VISIBLE);
                songsDetailsBinding.shareMusic.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(Song song) {
        musicViewModel.setMusicDetails(song);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSongs();
        if (musicViewModel.getIsPlayerActive().getValue() != null)
            resumeVideo();
    }
}

