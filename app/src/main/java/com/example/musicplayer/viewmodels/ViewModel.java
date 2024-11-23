package com.org.tunestream.viewmodels;

import android.content.Context;

import com.org.tunestream.firebase_manager.FireStoreManager;
import com.org.tunestream.firebase_manager.UserDefaultsManager;
import com.org.tunestream.models.Playlist;
import com.org.tunestream.models.Song;

import java.util.List;

public class ViewModel {
    private static final ViewModel shared = new ViewModel();

    public static ViewModel getInstance() {
        return shared;
    }

    public void getMyPlayList(Context context, FireStoreCallback<List<Playlist>> callback) {
        FireStoreManager.shared.getPlaylists(getEmail(context), callback::onCallback);
    }

    public void getSharedPlayList(Context context, FireStoreCallback<List<Playlist>> callback) {
        FireStoreManager.shared.getSharedPlaylists(getEmail(context), callback::onCallback);
    }

    public void getSongs(String documentID, FireStoreCallback<List<Song>> callback) {
        FireStoreManager.shared.getSongList(documentID, callback::onCallback);
    }

    public void deletePlayList(Playlist playList, FireStoreCallback<Boolean> callback) {
        FireStoreManager.shared.deletePlaylist(playList.getDocumentId(), success -> {
            if (success) {
                System.out.println("Playlist deleted successfully");
            } else {
                System.out.println("Error deleting playlist");
            }
            callback.onCallback(success);
        });
    }

    public void addPlayList(Context context, String name, String description, FireStoreCallback<Void> callback) {
        FireStoreManager.shared.addPlaylist(name, description, getEmail(context), callback::onCallback);
    }

    public void editPlayList(Context context, String name, String description, String documentId, FireStoreCallback<Void> callback) {
        FireStoreManager.shared.editPlaylist(name, description, getEmail(context), documentId, callback::onCallback);
    }

    private String getEmail(Context context) {
        // Implement your method to get the email
        return UserDefaultsManager.getInstance(context).getEmail();
    }

    public interface FireStoreCallback<T> {
        void onCallback(T result);
    }
}

