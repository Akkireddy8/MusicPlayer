package com.org.tunestream;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.org.tunestream.databinding.DialogShareMusicBinding;
import com.org.tunestream.databinding.DialogSuccessMessageBinding;
import com.org.tunestream.firebase_manager.FireStoreManager;
import com.org.tunestream.interfaces.ActionCallback;

public class DialogUtils {

    private static Dialog dialog;

    public static void showCustomAlertDialog(Context context, String title, String message, String playlistId, String playlistName) {
        DialogShareMusicBinding dialogBinding = DialogShareMusicBinding.inflate(LayoutInflater.from(context));
        dialogBinding.tvTitle.setText(title);
        dialogBinding.tvMessage.setText(message);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogBinding.getRoot())
                .create();
        dialog.setCancelable(false);

        dialogBinding.btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialogBinding.btnOk.setOnClickListener(v -> {
            String email = dialogBinding.etEmail.getText().toString().toLowerCase();
            if (!TextUtils.isEmpty(email)) {
                FireStoreManager.shared.sharePlaylist(context, playlistId, playlistName, email);
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public static void showMessageDialog(Context context, String title, String message, ActionCallback<Boolean> callback) {
        DialogSuccessMessageBinding dialogBinding = DialogSuccessMessageBinding.inflate(LayoutInflater.from(context));
        dialogBinding.tvTitle.setText(title);
        dialogBinding.tvMessage.setText(message);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogBinding.getRoot())
                .create();
        dialog.setCancelable(false);

        dialogBinding.btnOk.setOnClickListener(v -> {
            callback.onCallback(true);
            dialog.dismiss();
        });

        dialog.show();
    }

    public static Dialog showProgress(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_loader);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.green_200), PorterDuff.Mode.MULTIPLY);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static void dismissProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static Boolean isDialogShowing() {
        return dialog != null && dialog.isShowing();
    }
}

