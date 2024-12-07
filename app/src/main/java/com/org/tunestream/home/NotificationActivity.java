package com.org.tunestream.home;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.org.tunestream.R;
import com.org.tunestream.adapters.NotificationAdapter;
import com.org.tunestream.databinding.ActivityNotificationBinding;
import com.org.tunestream.databinding.CustomAppBarBinding;
import com.org.tunestream.firebase_manager.FireStoreManager;
import com.org.tunestream.firebase_manager.UserDefaultsManager;
import com.org.tunestream.models.NotificationModel;
import com.org.tunestream.player.PlayerBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends PlayerBaseActivity {

    private ActivityNotificationBinding notificationBinding;
    private CustomAppBarBinding customAppBarBinding;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList = new ArrayList<NotificationModel>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationBinding = ActivityNotificationBinding.inflate(getLayoutInflater());
        ViewGroup viewGroup = findViewById(R.id.activity_content);
        viewGroup.addView(notificationBinding.getRoot());
        customAppBarBinding = notificationBinding.customAppBar;
        customAppBarBinding.backImage.setOnClickListener(v -> {
            finish();
        });
        customAppBarBinding.actionImage.setImageResource(R.drawable.ic_delete);
        customAppBarBinding.actionImage.setVisibility(View.VISIBLE);
        customAppBarBinding.actionImage.setOnClickListener(v -> {
            onClearNotification();
        });
        customAppBarBinding.titleText.setText(getString(R.string.notification));
        adapter = new NotificationAdapter(notificationList);
        notificationBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationBinding.recyclerView.setAdapter(adapter);

        reloadData();
    }

    private void reloadData() {
        String email = UserDefaultsManager.getInstance(this).getEmail();
        FireStoreManager.shared.getNotificationsList(this, email, notifications -> {
            if (notifications != null && !notifications.isEmpty()) {
                System.out.println("Notifications: Not Empty");
                notificationBinding.recyclerView.setVisibility(View.VISIBLE);
                notificationBinding.txtNoData.setVisibility(View.GONE);

                notificationList.clear();
                notificationList.addAll(notifications);
                adapter.notifyDataSetChanged();
            } else {
                System.out.println("Notifications: Empty");
                notificationBinding.recyclerView.setVisibility(View.GONE);
                notificationBinding.txtNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    public void onClearNotification() {
        FireStoreManager.shared.clearNotifications(this, result -> {
            if (result) {
                showAlertOnTop("Notification List cleared");
                finish();
            }
        });
    }

    private void showAlertOnTop(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
