package com.org.tunestream.playlist.views;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.org.tunestream.R;
import com.org.tunestream.databinding.ActivityAddSongBinding;
import com.org.tunestream.databinding.CustomAppBarBinding;
import com.org.tunestream.firebase_manager.FireStoreManager;

public class AddSongActivity extends AppCompatActivity {

    private ActivityAddSongBinding addSongBinding;
    private CustomAppBarBinding customAppBarBinding;
    private String playListDocumentId = "";
    private String playListName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSongBinding = ActivityAddSongBinding.inflate(getLayoutInflater());
        setContentView(addSongBinding.getRoot());
        customAppBarBinding = addSongBinding.customAppBar;
        customAppBarBinding.backImage.setOnClickListener(v -> {
            finish();
        });
        customAppBarBinding.titleText.setText(getString(R.string.add_song));

        if (getIntent().hasExtra("playListDocumentId")) {
            playListDocumentId = getIntent().getStringExtra("playListDocumentId");
        }

        if (getIntent().hasExtra("playListName")) {
            playListName = getIntent().getStringExtra("playListName");
        }
    }

    public void onAddSongClick(View view) {
        if (addSongBinding.songTitle.getText().toString().isEmpty()) {
            showAlertOnTop("Song Title Required");
            return;
        }

        if (addSongBinding.songId.getText().toString().isEmpty()) {
            showAlertOnTop("Song ID Required");
            return;
        }

        /*if (addSongBinding.songId.getText().toString().contains("-")) {
            showAlertOnTop("Song ID Not Supported, please try another song");
            return;
        }*/

        String artist = addSongBinding.songArtist.getText().toString();
        if (artist.isEmpty()) {
            artist = "Unknown";
        }

        FireStoreManager.shared.addSongToPlaylist(playListName, playListDocumentId, addSongBinding.songId.getText().toString(), addSongBinding.songTitle.getText().toString(), artist, new FireStoreManager.FireStoreCallback<Void>() {
            @Override
            public void onCallback(Void result) {
                AddSongActivity.this.finish();
            }
        });
    }

    private void showAlertOnTop(String message) {
        // Implement your alert logic here
    }
}


