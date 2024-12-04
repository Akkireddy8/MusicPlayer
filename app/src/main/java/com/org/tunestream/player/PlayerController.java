package com.org.tunestream.player;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.org.tunestream.R;
import com.org.tunestream.models.Song;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class PlayerController extends BottomSheetDialog {

    private static YouTubePlayer mYouTubePlayer;
    private static YouTubePlayerView youTubePlayerView;
    private static AbstractYouTubePlayerListener listener;
    public static Boolean isPlaying = false;
    public static PlayerController instance;

    private PlayerController(@NonNull Context context, Song song) {
        super(context);
        showPlayer(context, song);
    }

    public static void showPlayer(Context context, Song song) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(R.layout.activity_music_bottom_sheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        TextView songTitle = dialog.findViewById(R.id.songTitle);
        songTitle.setText(song.getTitle());
        TextView artistName = dialog.findViewById(R.id.artistName);
        artistName.setText(song.getArtist());
        youTubePlayerView = dialog.findViewById(R.id.youTubePlayerView);
        ImageView closeImage = dialog.findViewById(R.id.closeImage);
        closeImage.setOnClickListener(v -> {
            if (mYouTubePlayer != null)
                mYouTubePlayer.removeListener(listener);
            youTubePlayerView.release();
            instance = null;
            isPlaying = false;
            dialog.dismiss();
        });
        ImageView songImage = dialog.findViewById(R.id.songImage);
        MusicViewModel viewModel = MusicViewModel.getInstance();
        viewModel.downloadImage(image -> {
            if (image == null)
                return;
            songImage.setImageBitmap(image);

        });
        ImageView playPauseImage = dialog.findViewById(R.id.playPauseImage);
        playPauseImage.setOnClickListener(v -> {
            if (isPlaying) {
                isPlaying = false;
                playPauseImage.setImageResource(R.drawable.ic_play);
                mYouTubePlayer.pause();
            } else {
                isPlaying = true;
                playPauseImage.setImageResource(R.drawable.ic_pause);
                mYouTubePlayer.play();
            }

        });
        AppCompatSeekBar seekBar = dialog.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mYouTubePlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                String videoId = "VAZxSoKb65o";
                mYouTubePlayer = youTubePlayer;
                mYouTubePlayer.loadVideo(videoId, 0);
                isPlaying = true;
            }

            @Override
            public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                seekBar.setProgress((int) second);

            }

            @Override
            public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float duration) {
                super.onVideoDuration(youTubePlayer, duration);
                seekBar.setMax((int) duration);
            }
        };

        youTubePlayerView.addYouTubePlayerListener(listener);
        dialog.show();
    }

    public static PlayerController getInstance(Context context, Song song) {
        if (instance == null)
            instance = new PlayerController(context, song);
        else showPlayer(context, song);
        return instance;
    }
}
