package com.org.tunestream.player;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.org.tunestream.interfaces.ActionCallback;
import com.org.tunestream.models.Song;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MusicViewModel extends AndroidViewModel {

    // LiveData for player and song details
    private YouTubePlayer youTubePlayer = null;
    private String lastSongId = "";
    private final MutableLiveData<String> songId = new MutableLiveData<>("");
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> artist = new MutableLiveData<>("");
    private final MutableLiveData<Integer> currentDuration = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalDuration = new MutableLiveData<>(0);
    private final MutableLiveData<Boolean> isPlayerActive = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);

    public MusicViewModel(@NonNull Application application) {
        super(application);
    }

    // Initialize YouTube player
    public void initializePlayer(YouTubePlayer player) {
        this.youTubePlayer = player;
    }

    public YouTubePlayer getYouTubePlayer() {
        return youTubePlayer;
    }

    // Control video playback
    public void controlPlayback(boolean play, boolean stop) {
        if (stop) {
            resetPlayer();
        } else {
            isPlaying.postValue(play);
            isPlayerActive.postValue(true);
        }
    }

    // Stop and reset player
    private void resetPlayer() {
        isPlaying.postValue(false);
        isPlayerActive.postValue(false);
        currentDuration.postValue(0);
        totalDuration.postValue(0);
        songId.postValue("");
        title.postValue("");
        artist.postValue("");
    }

    public String getLastSongId() {
        return lastSongId;
    }

    // LiveData Getters
    public LiveData<String> getSongId() {
        return songId;
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<String> getArtist() {
        return artist;
    }

    public LiveData<Integer> getCurrentDuration() {
        return currentDuration;
    }

    public LiveData<Integer> getTotalDuration() {
        return totalDuration;
    }

    public LiveData<Boolean> getIsPlayerActive() {
        return isPlayerActive;
    }

    public LiveData<Boolean> getIsPlaying() {
        return isPlaying;
    }

    // Update song details
    public void setMusicDetails(Song song) {
        // Use postValue for thread safety if invoked from background threads
        lastSongId = songId.getValue();
        songId.postValue(song.getSongId());
        title.postValue(song.getTitle());
        artist.postValue(song.getArtist());

        // Activate the player and reset playback state
        isPlayerActive.postValue(true);
        isPlaying.postValue(true); // Initially, do not assume playback has started

        // Reset duration properties
        currentDuration.postValue(0);
    }

    public void setCurrentDuration(int duration) {
        currentDuration.setValue(duration);
    }

    public void setTotalDuration(int duration) {
        totalDuration.setValue(duration);
    }

    // Get thumbnail image URL
    public String getImageUrl() {
        String id = songId.getValue();
        return (id == null || id.isEmpty()) ? null : "https://img.youtube.com/vi/" + id + "/0.jpg";
    }

    // Download image asynchronously
    public void downloadImage(ActionCallback<Bitmap> callback) {
        String id = songId.getValue();
        if (id == null || id.isEmpty()) {
            callback.onCallback(null);
            return;
        }

        String path = "https://img.youtube.com/vi/" + id + "/0.jpg";

        new Thread(() -> {
            try {
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);

                // Post to the main thread
                new Handler(Looper.getMainLooper()).post(() -> callback.onCallback(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> callback.onCallback(null));
            }
        }).start();
    }

    // Cleanup resources
    @Override
    protected void onCleared() {
        super.onCleared();
        youTubePlayer = null;
    }
}