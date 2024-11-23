package com.org.tunestream.player;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.org.tunestream.models.Song;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MusicViewModel {

    private String title = "";
    private String artist = "";
    private String minute = "0";
    private String second = "0";
    private String songId = "";
    private Song song;

    public static MusicViewModel instance;

    public static MusicViewModel getInstance() {
        if (instance == null)
            instance = new MusicViewModel();
        return instance;
    }

    public void setMusicDetails(Song song) {
        this.song = song;
        this.title = song.getTitle();
        this.artist = song.getArtist();
        this.songId = song.getSongId();
    }

    public String getImageUrl() {
        return "https://img.youtube.com/vi/" + songId + "/0.jpg";
    }

    public void downloadImage(ImageDownloadCallback callback) {
        if (song == null || song.getSongId().isEmpty()) {
            callback.onComplete(null);
            return;
        }

        String path = "https://img.youtube.com/vi/" + song.getSongId() + "/0.jpg";

        new Thread(() -> {
            try {
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                callback.onComplete(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onComplete(null);
            }
        }).start();
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Song getSong() {
        return song;
    }

    public interface ImageDownloadCallback {
        void onComplete(Bitmap image);
    }
}

