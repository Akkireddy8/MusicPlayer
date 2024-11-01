package com.example.musicplayer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

public class UserProfile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileName, profileEmail;
    private Button btnEditProfile, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize views
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnLogout = findViewById(R.id.btn_logout);

        // Static profile data for now
        profileName.setText("John Doe");
        profileEmail.setText("john.doe@example.com");

        // Set click listener for Edit Profile (implement EditProfileActivity if needed)
        btnEditProfile.setOnClickListener(v -> {
            // Handle edit profile logic (e.g., start a new activity)
        });

        // Set click listener for Log Out
        btnLogout.setOnClickListener(v -> {
            // Handle log out and redirect to login screen
            finish(); // For now, just close the activity
        });
    }
}
