package com.org.tunestream.home;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.org.tunestream.R;
import com.org.tunestream.adapters.PlaylistAdapter;
import com.org.tunestream.databinding.ActivityHomeBinding;
import com.org.tunestream.firebase_manager.UserDefaultsManager;
import com.org.tunestream.interfaces.OnClickInterface;
import com.org.tunestream.models.Playlist;
import com.org.tunestream.player.MusicViewModel;
import com.org.tunestream.player.PlayerController;
import com.org.tunestream.playlist.views.CreatePlaylistActivity;
import com.org.tunestream.playlist.views.SharedPlaylistActivity;
import com.org.tunestream.playlist.views.SongsDetailsActivity;
import com.org.tunestream.viewmodels.ViewModel;


import android.content.Intent;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.PointerIcon;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnClickInterface {

    private ActivityHomeBinding homeBinding;
    private PlaylistAdapter playlistAdapter;
    private List<Playlist> playlist = new ArrayList<>();
    private List<Playlist> originalPlaylist = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());
        homeBinding.welcomeMessage.setText("Welcome " + UserDefaultsManager.getInstance(this).getName());

        homeBinding.layoutPlaylist.setVisibility(View.GONE);
        homeBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        playlistAdapter = new PlaylistAdapter(playlist, this);
        homeBinding.recyclerView.setAdapter(playlistAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(homeBinding.recyclerView);

        homeBinding.searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch();
            }
        });

        homeBinding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getPlayList();
    }

    private void onSearch() {
        String searchText = homeBinding.searchEditText.getText().toString().toLowerCase();
        if (searchText.isEmpty()) {
            playlist.clear();
            playlist.addAll(originalPlaylist);
        } else {
            List<Playlist> filteredPlaylist = new ArrayList<>();
            for (Playlist p : originalPlaylist) {
                if (p.getName().toLowerCase().contains(searchText) || p.getDescription().toLowerCase().contains(searchText)) {
                    filteredPlaylist.add(p);
                }
            }
            playlist.clear();
            playlist.addAll(filteredPlaylist);
        }
        playlistAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPlayList();
        if (PlayerController.instance != null && PlayerController.isPlaying) {
            MusicViewModel musicViewModel = MusicViewModel.getInstance();
            PlayerController.getInstance(this, musicViewModel.getSong());
        }
    }

    private void getPlayList() {
        ViewModel.getInstance().getMyPlayList(this, playList -> {
            if (playList.isEmpty()) {
                homeBinding.layoutPlaylist.setVisibility(View.GONE);
            } else {
                homeBinding.layoutPlaylist.setVisibility(View.VISIBLE);
            }
            playlist.clear();
            originalPlaylist.clear();
            playlist.addAll(playList);
            originalPlaylist.addAll(playList);
            playlistAdapter.notifyDataSetChanged();
        });
    }

    public void onCreatePlaylistClicked(View view) {
        Intent intent = new Intent(this, CreatePlaylistActivity.class);
        startActivity(intent);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        //private Drawable  icon = ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_delete);
        private ColorDrawable background = new ColorDrawable();

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                ViewModel.getInstance().deletePlayList(playlist.get(position), value ->
                        getPlayList()
                );
            } else if (direction == ItemTouchHelper.RIGHT) {
                Intent intent = new Intent(HomeActivity.this, CreatePlaylistActivity.class);
                intent.putExtra("moveFrom", "Edit");
                intent.putExtra("playlist", (Serializable) playlist.get(position));
                startActivity(intent);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            View itemView = viewHolder.itemView;
            int backgroundCornerOffset = 20;

            if (dX > 0) { // Swiping to the right
                background.setColor(Color.BLUE);
                background.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                        itemView.getBottom());
            } else if (dX < 0) { // Swiping to the left
                background.setColor(Color.RED);
                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
            } else { // view is unSwiped
                background.setBounds(0, 0, 0, 0);
            }
            background.draw(c);
        }
    };

    public void onSharedPlaylistClicked(View view) {
        startActivity(new Intent(this, SharedPlaylistActivity.class));
    }

    public void onEditProfileClicked(View view) {
        startActivity(new Intent(this, EditProfileActivity.class));
    }

    public void onSearchPlaylistClicked(View view) {
        homeBinding.layoutSearch.setVisibility(View.VISIBLE);
        homeBinding.title.setVisibility(View.GONE);
        homeBinding.searchEditText.setText("");
        onSearch();
    }

    public void onPlaylistClicked(View view) {
        homeBinding.layoutSearch.setVisibility(View.GONE);
        homeBinding.title.setVisibility(View.VISIBLE);
        homeBinding.searchEditText.setText("");
        onSearch();
    }

    public void onNotificationClicked(View view) {
        startActivity(new Intent(this, NotificationActivity.class));
    }

    @Override
    public void onItemClick(Playlist playlist) {
        Intent intent = new Intent(this, SongsDetailsActivity.class);
        intent.putExtra("playlist", playlist);
        startActivity(intent);
    }
}

