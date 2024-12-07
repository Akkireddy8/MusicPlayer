package com.org.tunestream.playlist.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.org.tunestream.R;
import com.org.tunestream.adapters.SharedPlaylistAdapter;
import com.org.tunestream.databinding.ActivitySharedPlaylistBinding;
import com.org.tunestream.databinding.CustomAppBarBinding;
import com.org.tunestream.player.PlayerBaseActivity;
import com.org.tunestream.interfaces.OnClickInterface;
import com.org.tunestream.models.Playlist;
import com.org.tunestream.viewmodels.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedPlaylistActivity extends PlayerBaseActivity implements OnClickInterface {

    private ActivitySharedPlaylistBinding sharedPlaylistBinding;
    private CustomAppBarBinding customAppBarBinding;
    private SharedPlaylistAdapter adapter;
    private List<Playlist> playlist = new ArrayList<>();
    private List<Playlist> originalPlaylist = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPlaylistBinding = ActivitySharedPlaylistBinding.inflate(getLayoutInflater());
        ViewGroup viewGroup = findViewById(R.id.activity_content);
        viewGroup.addView(sharedPlaylistBinding.getRoot());
        customAppBarBinding = sharedPlaylistBinding.customAppBar;
        customAppBarBinding.backImage.setOnClickListener(v -> {
            finish();
        });
        customAppBarBinding.titleText.setText(getString(R.string.shared_playlist));

        sharedPlaylistBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SharedPlaylistAdapter(playlist, this);
        sharedPlaylistBinding.recyclerView.setAdapter(adapter);

        sharedPlaylistBinding.searchImage.setOnClickListener(v -> {
            onSearch(sharedPlaylistBinding.searchTF.getText().toString());
        });

        sharedPlaylistBinding.searchTF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        getSharedPlayList();
    }

    private void getSharedPlayList() {
        ViewModel.getInstance().getSharedPlayList(SharedPlaylistActivity.this, playList -> {
            if (playList.isEmpty()) {
                sharedPlaylistBinding.recyclerView.setVisibility(View.GONE);
                sharedPlaylistBinding.txtNoData.setVisibility(View.VISIBLE);
            } else {
                sharedPlaylistBinding.recyclerView.setVisibility(View.VISIBLE);
                sharedPlaylistBinding.txtNoData.setVisibility(View.GONE);
            }
            playlist.clear();
            playlist.addAll(playList);
            originalPlaylist.clear();
            originalPlaylist.addAll(playList);
            adapter.notifyDataSetChanged();
        });
    }

    private void onSearch(String searchText) {
        if (searchText.isEmpty()) {
            playlist.clear();
            playlist.addAll(originalPlaylist);
        } else {
            List<Playlist> filteredPlaylist = new ArrayList<>();
            for (Playlist pl : originalPlaylist) {
                if (pl.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                        pl.getDescription().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredPlaylist.add(pl);
                }
            }
            playlist.clear();
            playlist.addAll(filteredPlaylist);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Playlist playlist) {
        Intent intent = new Intent(this, SongsDetailsActivity.class);
        intent.putExtra("playlist", playlist);
        intent.putExtra("moveFrom", "Shared");
        startActivity(intent);
    }
}

