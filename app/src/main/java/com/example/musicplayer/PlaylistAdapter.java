package com.example.musicplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<Playlist> playlists;
    private OnSongClickListener onSongClickListener;

    public PlaylistAdapter(List<Playlist> playlists, OnSongClickListener onSongClickListener) {
        this.playlists = playlists;
        this.onSongClickListener = onSongClickListener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.playlistName.setText(playlist.getName());
        holder.playlistDescription.setText(playlist.getDescription());

        // Handle click event for each song
        holder.itemView.setOnClickListener(v -> onSongClickListener.onSongClick(playlist));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ImageView playlistImage;
        TextView playlistName, playlistDescription;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            playlistImage = itemView.findViewById(R.id.playlist_image);
            playlistName = itemView.findViewById(R.id.playlist_name);
            playlistDescription = itemView.findViewById(R.id.playlist_description);
        }
    }

    // Interface for handling click events
    public interface OnSongClickListener {
        void onSongClick(Playlist playlist);
    }
}

