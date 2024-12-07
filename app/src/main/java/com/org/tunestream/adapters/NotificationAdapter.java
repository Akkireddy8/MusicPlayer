package com.org.tunestream.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.tunestream.databinding.ItemNotificationBinding;
import com.org.tunestream.models.NotificationModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ItemViewHolder> {
    private final List<NotificationModel> itemList;

    public NotificationAdapter(List<NotificationModel> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        NotificationModel item = itemList.get(position);
        if (item.getTitle() != null)
            holder.binding.title.setText(item.getTitle());

        holder.binding.message.setText(item.getMessage());
        long milliseconds = (item.getTimestamp().getSeconds()) * 1000 + (item.getTimestamp().getNanoseconds()) / 1000000; // Create an Instant object from milliseconds
        Date date = new Date(milliseconds);
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy");
        holder.binding.time.setText(outputFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ItemNotificationBinding binding;

        public ItemViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
