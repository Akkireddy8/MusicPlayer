package com.org.tunestream.home;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.org.tunestream.R;
import com.org.tunestream.adapters.NotificationAdapter;
import com.org.tunestream.databinding.ActivityNotificationBinding;
import com.org.tunestream.databinding.CustomAppBarBinding;
import com.org.tunestream.firebase_manager.FireStoreManager;
import com.org.tunestream.firebase_manager.UserDefaultsManager;
import com.org.tunestream.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private ActivityNotificationBinding notificationBinding;
    private CustomAppBarBinding customAppBarBinding;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList = new ArrayList<NotificationModel>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationBinding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(notificationBinding.getRoot());
        customAppBarBinding = notificationBinding.customAppBar;
        customAppBarBinding.backImage.setOnClickListener(v -> {
            finish();
        });
        customAppBarBinding.titleText.setText(getString(R.string.notification));
        adapter = new NotificationAdapter(notificationList);
        notificationBinding.recyclerView.setHasFixedSize(true);
        notificationBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationBinding.recyclerView.setAdapter(adapter);

        reloadData();
    }

    private void reloadData() {
        notificationList.clear();
        String email = UserDefaultsManager.getInstance(this).getEmail();
        FireStoreManager.shared.getNotificationsList(email, notifications -> {
            notificationList.addAll(notifications);
        });
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onClear(View view) {
        FireStoreManager.shared.clearNotifications(this);
        showAlertOnTop("Notification List cleared");
        finish();
    }

    public void backButton(View view) {
        finish();
    }

    private void showAlertOnTop(String message) {
        // Implement your alert logic here
    }
}
