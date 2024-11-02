package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName, editEmail;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        // Initialize views
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        btnSave = findViewById(R.id.btn_save);

        // Retrieve current profile data
        Intent intent = getIntent();
        editName.setText(intent.getStringExtra("name"));
        editEmail.setText(intent.getStringExtra("email"));

        // Set click listener for Save button
        btnSave.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", editName.getText().toString());
            resultIntent.putExtra("email", editEmail.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish(); // Close EditProfileActivity
        });
    }
}
