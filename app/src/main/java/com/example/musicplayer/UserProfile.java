package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

public class UserProfile extends AppCompatActivity {

    // Existing declarations
    private ImageView profileImage;
    private TextView profileName, profileEmail;
    private Button btnEditProfile, btnLogout;
    private static final int REQUEST_EDIT_PROFILE = 1; // Request code for EditProfileActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize toolbar and enable back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize other views as before
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnLogout = findViewById(R.id.btn_logout);

        // Set click listener for Edit Profile
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfile.this, EditProfileActivity.class);
            intent.putExtra("name", profileName.getText().toString());
            intent.putExtra("email", profileEmail.getText().toString());
            startActivityForResult(intent, REQUEST_EDIT_PROFILE);
        });

        // Set click listener for Log Out
        btnLogout.setOnClickListener(v -> {
            finish(); // End the activity, which returns to the previous screen
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();  // Finish current activity and go back to MainActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
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