package com.org.tunestream.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.tunestream.databinding.ItemPlaylistBinding;
import com.org.tunestream.interfaces.OnClickInterface;
import com.org.tunestream.models.Playlist;

import java.util.List;


public class SharedPlaylistAdapter extends RecyclerView.Adapter<SharedPlaylistAdapter.ItemViewHolder> {
    private final List<Playlist> itemList;
    private final OnClickInterface onClickInterface;

    public SharedPlaylistAdapter(List<Playlist> itemList, OnClickInterface onClickInterface) {
        this.itemList = itemList;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaylistBinding binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Playlist item = itemList.get(position);
        holder.binding.playlistName.setText(item.getName());
        holder.binding.playlistDescription.setText(item.getDescription());
        holder.itemView.setOnClickListener(v -> {
            onClickInterface.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemPlaylistBinding binding;

        public ItemViewHolder(ItemPlaylistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
