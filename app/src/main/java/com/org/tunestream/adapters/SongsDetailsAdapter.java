package com.org.tunestream.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.tunestream.databinding.ItemSongsDetailsBinding;
import com.org.tunestream.interfaces.OnSongClickInterface;
import com.org.tunestream.models.Song;

import java.util.List;


public class SongsDetailsAdapter extends RecyclerView.Adapter<SongsDetailsAdapter.ItemViewHolder> {
    private final List<Song> itemList;
    private final OnSongClickInterface onSongClickInterface;

    public SongsDetailsAdapter(List<Song> itemList, OnSongClickInterface onSongClickInterface) {
        this.itemList = itemList;
        this.onSongClickInterface = onSongClickInterface;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongsDetailsBinding binding = ItemSongsDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Song item = itemList.get(position);
        holder.binding.songTitle.setText(item.getTitle());
        holder.binding.artist.setText(item.getArtist());
        holder.itemView.setOnClickListener(v -> {
            onSongClickInterface.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemSongsDetailsBinding binding;

        public ItemViewHolder(ItemSongsDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
