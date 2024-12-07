package com.org.tunestream.playlist.views;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.org.tunestream.DialogUtils;
import com.org.tunestream.R;
import com.org.tunestream.databinding.ActivityAddSongBinding;
import com.org.tunestream.databinding.CustomAppBarBinding;
import com.org.tunestream.firebase_manager.FireStoreManager;
import com.org.tunestream.player.PlayerBaseActivity;

public class AddSongActivity extends PlayerBaseActivity {

    private ActivityAddSongBinding addSongBinding;
    private CustomAppBarBinding customAppBarBinding;
    private String playListDocumentId = "";
    private String playListName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSongBinding = ActivityAddSongBinding.inflate(getLayoutInflater());
        ViewGroup viewGroup = findViewById(R.id.activity_content);
        viewGroup.addView(addSongBinding.getRoot());
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

        if (addSongBinding.songArtist.getText().toString().isEmpty()) {
            showAlertOnTop("Artist Name Required");
            return;
        }

        FireStoreManager.shared.addSongToPlaylist(this, playListName, playListDocumentId, addSongBinding.songId.getText().toString(), addSongBinding.songTitle.getText().toString(), addSongBinding.songArtist.getText().toString(), new FireStoreManager.FireStoreCallback<Void>() {
            @Override
            public void onCallback(Void result) {
                DialogUtils.showMessageDialog(AddSongActivity.this, "Message", "Song added successfully", result1 -> {
                    if (result1) {
                        finish();
                    }
                });
            }
        });
    }

    private void showAlertOnTop(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}


