package com.org.tunestream.playlist.views;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.org.tunestream.DialogUtils;
import com.org.tunestream.R;
import com.org.tunestream.databinding.ActivityCreatePlaylistBinding;
import com.org.tunestream.databinding.CustomAppBarBinding;
import com.org.tunestream.models.Playlist;
import com.org.tunestream.player.PlayerBaseActivity;
import com.org.tunestream.viewmodels.ViewModel;

public class CreatePlaylistActivity extends PlayerBaseActivity {

    private ActivityCreatePlaylistBinding createPlaylistBinding;
    private CustomAppBarBinding customAppBarBinding;
    private String moveFrom;
    private Playlist playlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPlaylistBinding = ActivityCreatePlaylistBinding.inflate(getLayoutInflater());
        ViewGroup viewGroup = findViewById(R.id.activity_content);
        viewGroup.addView(createPlaylistBinding.getRoot());
        customAppBarBinding = createPlaylistBinding.customAppBar;
        customAppBarBinding.backImage.setOnClickListener(v -> {
            finish();
        });
        customAppBarBinding.titleText.setText(getString(R.string.create_playlist));

        if (getIntent().hasExtra("moveFrom")) {
            moveFrom = getIntent().getStringExtra("moveFrom");
        }

        if (moveFrom != null && moveFrom.equals("Edit")) {
            playlist = (Playlist) getIntent().getSerializableExtra("playlist");
            createPlaylistBinding.playlistName.setText(playlist.getName());
            createPlaylistBinding.playlistDescription.setText(playlist.getDescription());
            createPlaylistBinding.createPlaylistButton.setText("Update");
        }
    }

    public void onCreatePlaylistTapped(View view) {
        if (createPlaylistBinding.playlistName.getText().toString().isEmpty()) {
            showAlertOnTop("Please add playlist name");
            return;
        }

        if (createPlaylistBinding.playlistDescription.getText().toString().isEmpty()) {
            showAlertOnTop("Please add description");
            return;
        }

        if (moveFrom != null && moveFrom.equals("Edit"))
            ViewModel.getInstance().editPlayList(this, createPlaylistBinding.playlistName.getText().toString(), createPlaylistBinding.playlistDescription.getText().toString(), playlist.getDocumentId(), new ViewModel.FireStoreCallback<Void>() {
                @Override
                public void onCallback(Void result) {
                    DialogUtils.showMessageDialog(CreatePlaylistActivity.this, "Message", "Playlist updated successfully", response -> {
                        CreatePlaylistActivity.this.finish();
                    });
                }
            });
        else
            ViewModel.getInstance().addPlayList(this, createPlaylistBinding.playlistName.getText().toString(), createPlaylistBinding.playlistDescription.getText().toString(), new ViewModel.FireStoreCallback<Void>() {
                @Override
                public void onCallback(Void result) {
                    DialogUtils.showMessageDialog(CreatePlaylistActivity.this, "Message", "Playlist created successfully", response -> {
                        CreatePlaylistActivity.this.finish();
                    });
                }
            });
    }

    private void showAlertOnTop(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

