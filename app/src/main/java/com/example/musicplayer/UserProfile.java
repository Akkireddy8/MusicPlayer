package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

public class UserProfile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileName, profileEmail;
    private Button btnEditProfile, btnLogout;
    private static final int REQUEST_EDIT_PROFILE = 1; // Request code for EditProfileActivity

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

        // Set click listener for Edit Profile
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfile.this, EditProfileActivity.class);
            intent.putExtra("name", profileName.getText().toString());
            intent.putExtra("email", profileEmail.getText().toString());
            startActivityForResult(intent, REQUEST_EDIT_PROFILE);
        });

        // Set click listener for Log Out
        btnLogout.setOnClickListener(v -> {
            // Handle log out and redirect to login screen
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK && data != null) {
            String updatedName = data.getStringExtra("name");
            String updatedEmail = data.getStringExtra("email");

            // Update the profile information
            profileName.setText(updatedName);
            profileEmail.setText(updatedEmail);
        }
    }
}
