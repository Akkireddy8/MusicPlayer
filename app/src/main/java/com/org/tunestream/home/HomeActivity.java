package com.org.tunestream.home;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.org.tunestream.R;
import com.org.tunestream.adapters.PlaylistAdapter;
import com.org.tunestream.databinding.ActivityHomeBinding;
import com.org.tunestream.firebase_manager.UserDefaultsManager;
import com.org.tunestream.interfaces.OnClickInterface;
import com.org.tunestream.models.Playlist;
import com.org.tunestream.player.PlayerBaseActivity;
import com.org.tunestream.playlist.views.CreatePlaylistActivity;
import com.org.tunestream.playlist.views.SharedPlaylistActivity;
import com.org.tunestream.playlist.views.SongsDetailsActivity;
import com.org.tunestream.viewmodels.ViewModel;


import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends PlayerBaseActivity implements OnClickInterface {

    private ActivityHomeBinding homeBinding;
    private PlaylistAdapter playlistAdapter;
    private List<Playlist> playlist = new ArrayList<>();
    private List<Playlist> originalPlaylist = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        ViewGroup viewGroup = findViewById(R.id.activity_content);
        viewGroup.addView(homeBinding.getRoot());

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

    private void getPlayList() {
        ViewModel.getInstance().getMyPlayList(this, playList -> {
            if (playList != null && !playList.isEmpty()) {
                homeBinding.recyclerView.setVisibility(View.VISIBLE);
                homeBinding.txtNoPlaylist.setVisibility(View.GONE);

                this.playlist.clear();
                originalPlaylist.clear();
                this.playlist.addAll(playList);
                originalPlaylist.addAll(playList);
                playlistAdapter.notifyDataSetChanged();
            } else {
                homeBinding.recyclerView.setVisibility(View.GONE);
                homeBinding.txtNoPlaylist.setVisibility(View.VISIBLE);
            }
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
                ViewModel.getInstance().deletePlayList(HomeActivity.this, playlist.get(position), value ->
                        getPlayList()
                );
            } else if (direction == ItemTouchHelper.RIGHT) {
                Intent intent = new Intent(HomeActivity.this, CreatePlaylistActivity.class);
                intent.putExtra("moveFrom", "Edit");
                intent.putExtra("playlist", playlist.get(position));
                startActivity(intent);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            View itemView = viewHolder.itemView;

// Get itemView dimensions
            final int itemViewHeight = itemView.getHeight();
            final int itemViewTop = itemView.getTop();
            final int itemViewBottom = itemView.getBottom();
            final int itemViewLeft = itemView.getLeft();
            final int itemViewRight = itemView.getRight();
            int backgroundCornerOffset = 20; // Adjust for rounded corners if needed

// Define text size and color
            float textSize = 16 * itemView.getResources().getDisplayMetrics().density; // Text size in sp
            Paint textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(textSize);
            textPaint.setColor(Color.WHITE); // Text color

            if (dX > 0) { // Swiping to the right
                // Set background color for right swipe
                background.setColor(Color.BLUE);
                background.setBounds(itemViewLeft, itemViewTop,
                        itemViewLeft + ((int) dX) + backgroundCornerOffset, itemViewBottom);
                background.draw(c);

                // Draw "Edit" text
                String text = "Edit";
                float textWidth = textPaint.measureText(text);
                float textMargin = (itemViewHeight - textSize) / 2;

                float textLeft = itemViewLeft + textMargin;
                float textBottom = itemViewBottom - textMargin;

                c.drawText(text, textLeft, textBottom, textPaint);
            } else if (dX < 0) { // Swiping to the left
                // Set background color for left swipe
                background.setColor(Color.RED);
                background.setBounds(itemViewRight + ((int) dX) - backgroundCornerOffset,
                        itemViewTop, itemViewRight, itemViewBottom);
                background.draw(c);

                // Draw "Delete" text
                String text = "Delete";
                float textWidth = textPaint.measureText(text);
                float textMargin = (itemViewHeight - textSize) / 2;

                float textRight = itemViewRight - textMargin;
                float textLeft = textRight - textWidth;
                float textBottom = itemViewBottom - textMargin;

                c.drawText(text, textLeft, textBottom, textPaint);
            } else { // View is unswiped
                background.setBounds(0, 0, 0, 0);
            }

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

    @Override
    protected void onResume() {
        super.onResume();
        homeBinding.welcomeMessage.setText("Welcome " + UserDefaultsManager.getInstance(HomeActivity.this).getName());
        getPlayList();
        if (mYouTubePlayer == null)
            mYouTubePlayer = musicViewModel.getYouTubePlayer();
    }
}

