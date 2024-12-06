package com.org.tunestream.models;

import java.io.Serializable;

public class Song implements Serializable {
    private String songId;
    private String title;
    private String artist;

    public Song() {
        // Default constructor required for calls to DataSnapshot.getValue(Song.class)
    }

    public Song(String songId, String title, String artist) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
