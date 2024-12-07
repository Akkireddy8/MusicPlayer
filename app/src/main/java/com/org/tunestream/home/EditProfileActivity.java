package com.org.tunestream.home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.firestore.DocumentSnapshot;
import com.org.tunestream.DialogUtils;
import com.org.tunestream.R;
import com.org.tunestream.auth.views.LoginActivity;
import com.org.tunestream.databinding.ActivityEditProfileBinding;
import com.org.tunestream.databinding.CustomAppBarBinding;
import com.org.tunestream.firebase_manager.FireStoreManager;
import com.org.tunestream.firebase_manager.UserDefaultsManager;
import com.org.tunestream.models.UserModel;
import com.org.tunestream.player.PlayerBaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends PlayerBaseActivity {

    private Calendar calendar;
    private String gender = "";
    private String password = "";
    private String documentId = "";

    private ActivityEditProfileBinding binding;
    private CustomAppBarBinding customAppBarBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        ViewGroup viewGroup = findViewById(R.id.activity_content);
        viewGroup.addView(binding.getRoot());
        customAppBarBinding = binding.customAppBar;
        customAppBarBinding.actionImage.setVisibility(View.VISIBLE);
        customAppBarBinding.actionImage.setOnClickListener(v -> {
            onLogout();
        });
        customAppBarBinding.backImage.setOnClickListener(v -> {
            finish();
        });
        customAppBarBinding.titleText.setText(getString(R.string.edit_details));

        getProfileData();

        calendar = Calendar.getInstance();
        binding.maleRadioButton.setOnClickListener(v -> onMaleFemaleClick(binding.maleRadioButton));
        binding.femaleRadioButton.setOnClickListener(v -> onMaleFemaleClick(binding.femaleRadioButton));
    }

    private void onMaleFemaleClick(View view) {
        if (view == binding.maleRadioButton) {
            binding.maleRadioButton.setChecked(true);
            binding.femaleRadioButton.setChecked(false);
            gender = "male";
        } else {
            binding.maleRadioButton.setChecked(false);
            binding.femaleRadioButton.setChecked(true);
            gender = "female";
        }
    }

    private boolean validate() {
        if (binding.txtName.getText().toString().isEmpty()) {
            showAlertOnTop("Please enter name.");
            return false;
        }

        if (gender.isEmpty()) {
            showAlertOnTop("Please select gender");
            return false;
        }

        if (binding.txtDOB.getText().toString().isEmpty()) {
            showAlertOnTop("Please enter binding.txtDOB.");
            return false;
        }
        return true;
    }

    public void onLogout() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    FireStoreManager.shared.removeAllListeners();
                    UserDefaultsManager.getInstance(this).clearUserDefaults();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void onUpdate(View view) {
        if (validate()) {
            Map<String, Object> profileData = new HashMap<>();
            profileData.put("name", binding.txtName.getText().toString());
            profileData.put("gender", gender);
            profileData.put("password", password);
            profileData.put("dob", binding.txtDOB.getText().toString());
            profileData.put("email", binding.txtEmail.getText().toString());

            FireStoreManager.shared.updateProfile(this, documentId, profileData, (success) -> {
                if (success) {
                    DialogUtils.showMessageDialog(this, "Success", "Profile updated successfully", (callback) -> {
                        if (callback)
                            finish();
                    });
                }
            });
        }
    }

    private void getProfileData() {
        FireStoreManager.shared.getProfile(UserDefaultsManager.getInstance(this).getEmail(), querySnapshot -> {
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                UserModel item = document.toObject(UserModel.class);
                documentId = document.getId();
                binding.txtName.setText(item.getName());
                binding.txtEmail.setText(item.getEmail());
                binding.txtDOB.setText(item.getDob());
                gender = item.getGender();
                password = item.getPassword();

                if (gender != null) {
                    if (gender.equals("male")) {
                        binding.maleRadioButton.setChecked(true);
                        binding.femaleRadioButton.setChecked(false);
                    } else {
                        binding.maleRadioButton.setChecked(false);
                        binding.femaleRadioButton.setChecked(true);
                    }
                }
            }
        });
    }

    public void showDatePicker(View mView) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        binding.txtDOB.setText(sdf.format(calendar.getTime()));
    }

    private void showAlertOnTop(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

