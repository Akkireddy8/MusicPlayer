package com.org.tunestream.player;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.org.tunestream.databinding.ActivityMusicBottomSheetBinding;

public class MusicBottomSheetController extends AppCompatActivity {

    private ActivityMusicBottomSheetBinding binding;
    private MusicViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicBottomSheetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.closeImage.setOnClickListener(v -> didTapClose());

        if (viewModel != null) {
            binding.songTitle.setText(viewModel.getTitle());
            binding.artistName.setText(viewModel.getArtist());

            viewModel.downloadImage(image -> runOnUiThread(() -> binding.songImage.setImageBitmap(image)));
        }
    }

    private void didTapClose() {
        finish();
    }
}

