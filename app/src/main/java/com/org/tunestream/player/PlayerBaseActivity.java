package com.org.tunestream.player;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.org.tunestream.R;
import com.org.tunestream.databinding.ActivityPlayerBaseBinding;
import com.org.tunestream.databinding.LayoutMusicControllerBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

public class PlayerBaseActivity extends AppCompatActivity {

    private ActivityPlayerBaseBinding binding;
    private LayoutMusicControllerBinding musicControllerBinding;
    protected YouTubePlayer mYouTubePlayer;
    protected MusicViewModel musicViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        musicControllerBinding = binding.musicControllerLayout;
        musicViewModel = new ViewModelProvider(
                (ViewModelStoreOwner) getApplication(),
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(MusicViewModel.class);

        if (mYouTubePlayer == null) {
            if (musicViewModel.getYouTubePlayer() == null) setupYouTubePlayer();
            else mYouTubePlayer = musicViewModel.getYouTubePlayer();

            setupUIListeners();
            observeLiveData();
        }
    }

    private void setupYouTubePlayer() {
        musicControllerBinding.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                musicViewModel.initializePlayer(youTubePlayer);
                mYouTubePlayer = musicViewModel.getYouTubePlayer();

            }

            @Override
            public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
                musicViewModel.setCurrentDuration((int) second);
            }

            @Override
            public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float duration) {
                musicViewModel.setTotalDuration((int) duration);
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                if (state.equals(PlayerConstants.PlayerState.ENDED)) {
                    musicViewModel.controlPlayback(false, false);
                    musicViewModel.setCurrentDuration(0);
                    resumeVideo();
                }
            }
        });
    }

    private void setupUIListeners() {
        // Close button listener
        musicControllerBinding.closeImage.setOnClickListener(v -> {
            if (mYouTubePlayer != null) {
                mYouTubePlayer.pause();
                mYouTubePlayer = null;
                mYouTubePlayer = musicViewModel.getYouTubePlayer();
                musicControllerBinding.seekBar.setProgress(0);
                musicViewModel.controlPlayback(false, true);
            }
        });

        // Play/Pause button listener
        musicControllerBinding.playPauseImage.setOnClickListener(v -> {
            musicViewModel.controlPlayback(!musicViewModel.getIsPlaying().getValue(), false);
        });

        // SeekBar listener
        musicControllerBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mYouTubePlayer != null) {
                    mYouTubePlayer.seekTo((float) progress);
                    musicViewModel.setCurrentDuration(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void observeLiveData() {
        musicViewModel.getSongId().observe(this, songId -> {
            if (songId != null && !songId.isEmpty()) {
                // Check if the song has changed
                if (hasSongChanged(songId)) {
                    musicControllerBinding.seekBar.setProgress(0);
                    loadVideo(0);
                } else loadVideo(musicViewModel.getCurrentDuration().getValue());
            }
        });

        musicViewModel.getTitle().observe(this, title -> {
            if (title != null && !title.isEmpty()) musicControllerBinding.songTitle.setText(title);
        });

        musicViewModel.getArtist().observe(this, artist -> {
            if (artist != null && !artist.isEmpty())
                musicControllerBinding.artistName.setText(artist);
        });

        musicViewModel.getCurrentDuration().observe(this, currentDuration -> {
            if (currentDuration != null)
                musicControllerBinding.seekBar.setProgress(currentDuration);
        });

        musicViewModel.getTotalDuration().observe(this, totalDuration -> {
            if (totalDuration != null) musicControllerBinding.seekBar.setMax(totalDuration);
        });

        musicViewModel.getIsPlaying().observe(this, isPlaying -> {
            int icon = isPlaying ? R.drawable.ic_pause : R.drawable.ic_play;
            musicControllerBinding.playPauseImage.setImageResource(icon);
            if (mYouTubePlayer != null) {
                if (isPlaying) mYouTubePlayer.play();
                else mYouTubePlayer.pause();
            }
        });

        musicViewModel.getIsPlayerActive().observe(this, isPlayerActive -> {
            if (isPlayerActive) {
                binding.musicControllerContainer.setVisibility(View.VISIBLE);
            } else {
                binding.musicControllerContainer.setVisibility(View.GONE);
            }
        });
    }

    public boolean hasSongChanged(String newSongId) {
        String lastSongId = musicViewModel.getLastSongId();
        if (lastSongId != null && !lastSongId.isEmpty())
            return !lastSongId.equals(newSongId);
        else return false;
    }

    protected void loadVideo(int currentDuration) {
        // Download image asynchronously
        musicViewModel.downloadImage(bitmap -> {
            if (bitmap != null) {
                musicControllerBinding.songImage.setImageBitmap(bitmap);
            }
        });

        if (mYouTubePlayer != null) {
            String songId = musicViewModel.getSongId().getValue();
            mYouTubePlayer.loadVideo(songId, (float) currentDuration);
        }
    }

    public void resumeVideo() {
        if (mYouTubePlayer != null) {
            mYouTubePlayer = musicViewModel.getYouTubePlayer();
            if (musicViewModel.getSongId().getValue() != null) {
                mYouTubePlayer.loadVideo(
                        musicViewModel.getSongId().getValue(),
                        musicViewModel.getCurrentDuration().getValue()
                );
                if (musicViewModel.getIsPlaying().getValue())
                    mYouTubePlayer.play();
                else mYouTubePlayer.pause();
            }
        }
    }
}
