package com.example.musicplayer;

public class Playlist {
    private String name;
    private String description;

    public Playlist(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
