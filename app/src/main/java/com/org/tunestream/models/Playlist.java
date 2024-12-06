package com.org.tunestream.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;

public class Playlist implements Serializable {
    private String name;
    private String description;
    private String createdBy;
    private transient Timestamp timestamp;
    private List<Song> songs;
    private List<String> sharedWith;
    private String documentId;

    public Playlist() {
        // Default constructor required for calls to DataSnapshot.getValue(Playlist.class)
    }

    public Playlist(String name, String description, String createdBy, Timestamp timestamp, List<Song> songs, List<String> sharedWith) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.timestamp = timestamp;
        this.songs = songs;
        this.sharedWith = sharedWith;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public List<String> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(List<String> sharedWith) {
        this.sharedWith = sharedWith;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}

