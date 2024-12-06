package com.org.tunestream.playlist.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.org.tunestream.R;
import com.org.tunestream.adapters.SongsDetailsAdapter;
import com.org.tunestream.databinding.ActivitySongsDetailsBinding;
import com.org.tunestream.databinding.CustomAppBarBinding;
import com.org.tunestream.firebase_manager.FireStoreManager;
import com.org.tunestream.interfaces.OnSongClickInterface;
import com.org.tunestream.models.Playlist;
import com.org.tunestream.models.Song;
import com.org.tunestream.player.MusicViewModel;
import com.org.tunestream.player.PlayerActivity;
import com.org.tunestream.player.PlayerController;
import com.org.tunestream.viewmodels.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SongsDetailsActivity extends AppCompatActivity implements OnSongClickInterface {

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
        setContentView(songsDetailsBinding.getRoot());
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
                }
            }
        }
    }

    public void onShareMusic(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Email");
        builder.setMessage("Please enter user email address");

        final EditText input = new EditText(this);
        input.setHint("Email");
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String email = input.getText().toString().toLowerCase();
            if (!email.isEmpty()) {
                FireStoreManager.shared.sharePlaylist(playlist.getDocumentId(), email);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public void onAddMusic(View view) {
        Intent intent = new Intent(this, AddSongActivity.class);
        intent.putExtra("playListDocumentId", playlist.getDocumentId());
        intent.putExtra("playListName", playlist.getName());
        startActivity(intent);
    }

    private void getSongs() {
        ViewModel.getInstance().getSongs(playlist.getDocumentId(), songs -> {
            if (songs != null && !songs.isEmpty()) {
                songsDetailsBinding.recyclerView.setVisibility(View.VISIBLE);
                songsDetailsBinding.noSongs.setVisibility(View.GONE);
                this.songs.clear();
                this.songs.addAll(songs);
                songsDetailsBinding.numberOfSongs.setText(this.songs.size() + " Songs");
                adapter.notifyDataSetChanged();
            } else {
                songsDetailsBinding.recyclerView.setVisibility(View.GONE);
                songsDetailsBinding.noSongs.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(Song song) {
        MusicViewModel musicViewModel = MusicViewModel.getInstance();
        musicViewModel.setMusicDetails(song);
        PlayerController.getInstance(this, musicViewModel.getSong());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSongs();
    }
}

