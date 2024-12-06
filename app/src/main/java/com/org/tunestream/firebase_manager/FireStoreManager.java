package com.org.tunestream.firebase_manager;

import android.content.Context;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.org.tunestream.models.NotificationModel;
import com.org.tunestream.models.Playlist;
import com.org.tunestream.models.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreManager {
    public static final FireStoreManager shared = new FireStoreManager();
    private List<String> hospital = new ArrayList<>();
    private List<ListenerRegistration> notificationListeners = new ArrayList<>();

    private FirebaseFirestore db;
    private CollectionReference dbRef;
    private CollectionReference lastMessages;
    private CollectionReference playListRef;
    private CollectionReference notificationCollection;
    private List<String> messageArray = new ArrayList<>();

    public FireStoreManager() {
        db = FirebaseFirestore.getInstance();
        dbRef = db.collection("Users");
        playListRef = db.collection("PlayList");
        notificationCollection = db.collection("Notifications");
    }

    public void getPlaylists(String email, final FireStoreCallback<List<Playlist>> callback) {
        //IndicatorHud.show();

        playListRef.whereEqualTo("createdBy", email.toLowerCase())
                .get()
                .addOnCompleteListener(task -> {
                    //IndicatorHud.dismiss();

                    if (task.isSuccessful()) {
                        List<Playlist> playlists = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            try {
                                Playlist playlist = document.toObject(Playlist.class);
                                playlist.setDocumentId(document.getId());
                                playlists.add(playlist);
                            } catch (Exception e) {
                                System.out.println("Error decoding playlist: " + e.getMessage());
                            }
                        }
                        callback.onCallback(playlists);
                    } else {
                        System.out.println("Error getting playlists: " + task.getException().getMessage());
                        callback.onCallback(new ArrayList<>());
                    }
                });
    }

    public void deletePlaylist(String documentID, final FireStoreCallback<Boolean> callback) {
        //IndicatorHud.show();

        playListRef.document(documentID).delete()
                .addOnCompleteListener(task -> {
                    //IndicatorHud.dismiss();

                    if (task.isSuccessful()) {
                        System.out.println("Playlist deleted successfully");
                        callback.onCallback(true);
                    } else {
                        System.out.println("Error deleting playlist: " + task.getException().getMessage());
                        callback.onCallback(false);
                    }
                });
    }

    public void clearNotifications(Context context) {
        String email = getEmail(context).toLowerCase();
        CollectionReference notificationCollectionPath = notificationCollection.document(email).collection("Notification");

        notificationCollectionPath.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            notificationCollectionPath.document(document.getId()).delete();
                        }
                    } else {
                        System.out.println("Error getting documents: " + task.getException().getMessage());
                    }
                });

        FireStoreManager.shared.messageArray.clear();
    }

    public void getNotificationsList(String email, FireStoreCallback<List<NotificationModel>> callback) {
        ListenerRegistration notificationListener = notificationCollection.document(email).collection("Notification").
                addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        System.out.println("Error listening for notifications: " + error.getMessage());
                        callback.onCallback(new ArrayList<>());
                        return;
                    }
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        List<NotificationModel> notifications = new ArrayList<>();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            notifications.add(new NotificationModel(document.getString("message"),
                                    document.getTimestamp("timestamp"),
                                    document.getString("userEmail")));
                        }
                        callback.onCallback(notifications);
                    }
                });
        notificationListeners.add(notificationListener);
    }

    public void getSongList(String documentID, final FireStoreCallback<List<Song>> callback) {
        //IndicatorHud.show();

        ListenerRegistration playListListener = playListRef.document(documentID)
                .addSnapshotListener((snapshot, error) -> {
                    //IndicatorHud.dismiss();

                    if (error != null) {
                        System.out.println("Error listening for playlist changes: " + error.getMessage());
                        callback.onCallback(new ArrayList<>());
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Map<String, Object> playlistData = snapshot.getData();
                        if (playlistData != null && playlistData.get("songs") != null) {
                            List<Map<String, Object>> songsData = (List<Map<String, Object>>) playlistData.get("songs");

                            List<Song> songs = new ArrayList<>();
                            if (songsData != null) {
                                for (Map<String, Object> item : songsData) {
                                    String songId = (String) item.get("songId");
                                    String title = (String) item.get("title");
                                    String artist = (String) item.get("artist");
                                    songs.add(new Song(songId, title, artist));
                                }

                                callback.onCallback(songs);
                            }
                        }
                    } else {
                        System.out.println("Playlist does not exist.");
                        callback.onCallback(new ArrayList<>());
                    }
                });

        notificationListeners.add(playListListener);
    }

    public void editPlaylist(String name, String description, String email, String documentId, final FireStoreCallback<Void> callback) {
        Map<String, Object> playlistData = new HashMap<>();
        playlistData.put("name", name.toLowerCase());
        playlistData.put("description", description);

        playListRef.document(documentId).update(playlistData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Document successfully updated");
                        callback.onCallback(null);
                    } else {
                        System.out.println("Error updating document: " + task.getException().getMessage());
                    }
                });
    }

    public void addPlaylist(String name, String description, String email, final FireStoreCallback<Void> callback) {
        //IndicatorHud.show();

        playListRef.whereEqualTo("name", name.toLowerCase())
                .whereEqualTo("createdBy", email.toLowerCase())
                .get()
                .addOnCompleteListener(task -> {
                    //IndicatorHud.dismiss();

                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        System.out.println("Playlist with the same name already exists for the user.");
                    } else {
                        Map<String, Object> playlistData = new HashMap<>();
                        playlistData.put("name", name.toLowerCase());
                        playlistData.put("description", description);
                        playlistData.put("timestamp", FieldValue.serverTimestamp());
                        playlistData.put("createdBy", email.toLowerCase());
                        playlistData.put("sharedWith", new ArrayList<>(List.of(email.toLowerCase())));

                        //IndicatorHud.show();

                        playListRef.add(playlistData)
                                .addOnCompleteListener(addTask -> {
                                    //IndicatorHud.dismiss();

                                    if (addTask.isSuccessful()) {
                                        System.out.println("Playlist added successfully");
                                        callback.onCallback(null);
                                    } else {
                                        System.out.println("Error adding playlist: " + addTask.getException().getMessage());
                                    }
                                });
                    }
                });
    }

    public void addSongToPlaylist(String playListName, String documentId, String songId, String songTitle, String artist, final FireStoreCallback<Void> callback) {
        Map<String, Object> songData = new HashMap<>();
        songData.put("songId", songId);
        songData.put("title", songTitle);
        songData.put("artist", artist);

        //IndicatorHud.show();

        playListRef.document(documentId).update("songs", FieldValue.arrayUnion(songData))
                .addOnCompleteListener(task -> {
                    //IndicatorHud.dismiss();

                    if (task.isSuccessful()) {
                        System.out.println("Song added to playlist successfully");
                        callback.onCallback(null);

                        getPlaylistSubscribers(documentId, subscribers -> {
                            for (String subscriber : subscribers) {
                                notifyUser(subscriber, "Playlist " + playListName + " updated: " + songTitle + " by " + artist);
                            }
                        });
                    } else {
                        System.out.println("Error adding song to playlist: " + task.getException().getMessage());
                    }
                });
    }

    private String getEmail(Context context) {
        // Implement your method to get the email
        return UserDefaultsManager.getInstance(context).getEmail();
    }

    public void notifyUser(String userEmail, String message) {
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("userEmail", userEmail.toLowerCase());
        notificationData.put("message", message);
        notificationData.put("timestamp", FieldValue.serverTimestamp());
        notificationCollection.document(userEmail.toLowerCase()).collection("Notification").add(notificationData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Notification added successfully");
            } else {
                System.out.println("Error adding notification: " + task.getException().getMessage());
            }
        });
    }

    public void setupNotificationListener(String userEmail, FireStoreCallback<List<String>> messageCallback) {
        CollectionReference notificationCollectionPath = notificationCollection.document(userEmail.toLowerCase()).collection("Notification");
        ListenerRegistration listener = notificationCollectionPath.addSnapshotListener((querySnapshot, error) -> {
            if (error != null) {
                System.out.println("Error listening for notifications: " + error.getMessage());
                return;
            }
            for (DocumentChange documentChange : querySnapshot.getDocumentChanges()) {
                switch (documentChange.getType()) {
                    case ADDED:
                        String message = documentChange.getDocument().getString("message");
                        System.out.println("Added Notification: " + message);
                        messageArray.add(message);
                        messageCallback.onCallback(messageArray);
                        break;
                    case MODIFIED:
                        System.out.println("Modified Notification");
                        break;
                    case REMOVED:
                        System.out.println("Removed Notification");
                        break;
                }
            }
        });
        notificationListeners.add(listener);
    }

    public void getPlaylistSubscribers(String documentId, FireStoreCallback<List<String>> callback) {
        playListRef.document(documentId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<String> subscribers = (List<String>) document.get("sharedWith");
                    callback.onCallback(subscribers);
                } else {
                    callback.onCallback(new ArrayList<>());
                }
            } else {
                System.out.println("Error getting playlist subscribers: " + task.getException().getMessage());
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    public void sharePlaylist(String playListId, String shareToEmail) {
        playListRef.document(playListId).update("sharedWith", FieldValue.arrayUnion(shareToEmail.toLowerCase())).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Playlist shared successfully");
            } else {
                System.out.println("Error sharing playlist: " + task.getException().getMessage());
            }
        });
    }

    public void getSharedPlaylists(String email, FireStoreCallback<List<Playlist>> callback) {
        //IndicatorHud.show();
        playListRef.whereArrayContains("sharedWith", email.toLowerCase()).get().addOnCompleteListener(task -> {
            //IndicatorHud.dismiss();
            if (task.isSuccessful()) {
                List<Playlist> playlists = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    try {
                        Playlist playlist = document.toObject(Playlist.class);
                        playlist.setDocumentId(document.getId());
                        playlists.add(playlist);
                    } catch (Exception e) {
                        System.out.println("Error decoding playlist: " + e.getMessage());
                    }
                }
                callback.onCallback(playlists);
            } else {
                System.out.println("Error getting shared playlists: " + task.getException().getMessage());
                callback.onCallback(new ArrayList<>());
            }
        });
    }

    public void getPlaylistDetails(String playlistId, FireStoreCallback<Playlist> callback) {
        playListRef.document(playlistId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    try {
                        Playlist playlist = document.toObject(Playlist.class);
                        callback.onCallback(playlist);
                    } catch (Exception e) {
                        System.out.println("Error decoding playlist details: " + e.getMessage());
                        callback.onCallback(null);
                    }
                } else {
                    System.out.println("Playlist document does not exist");
                    callback.onCallback(null);
                }
            } else {
                System.out.println("Error getting playlist details: " + task.getException().getMessage());
                callback.onCallback(null);
            }
        });
    }

    public void signUp(String email, String name, String password, FireStoreCallback<Boolean> callback) {
        checkAlreadyExistAndSignup(name, email, password, callback);
    }

    public void login(Context context, String email, String password, FireStoreCallback<Boolean> callback) {
        db.collection("Users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String docId = document.getId();
                    UserDefaultsManager.getInstance(context).setDocumentId(docId);
                    //UserDefaults.standard.setValue(docId, "documentId");
                    String pwd = document.getString("password");
                    if (pwd != null && pwd.equals(password)) {
                        String name = document.getString("name");
                        String emailValue = document.getString("email");
                        String passwordValue = document.getString("password");
                        UserDefaultsManager.getInstance(context).saveData(name, emailValue, passwordValue);
                        callback.onCallback(true);
                    } else {
                        System.out.println("Password doesn't match");
                    }
                }
            } else {
                System.out.println("Email not found!!");
            }
        });
    }

    public void getPassword(Context context, String email, String password, FireStoreCallback<String> callback) {
        db.collection("Users").whereEqualTo("email", email.toLowerCase()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String docId = document.getId();
                    UserDefaultsManager.getInstance(context).setDocumentId(docId);
                    //UserDefaults.standard.setValue(docId, "documentId");
                    String pwd = document.getString("password");
                    if (pwd != null) {
                        callback.onCallback(pwd);
                    } else {
                        System.out.println("Something went wrong!!");
                    }
                }
            } else {
                System.out.println("Email id not found!!");
            }
        });
    }

    private void checkAlreadyExistAndSignup(String name, String email, String password, FireStoreCallback<Boolean> callback) {
        getQueryFromFirestore("email", email, querySnapshot -> {
            if (!querySnapshot.isEmpty()) {
                System.out.println("This Email is Already Registered!!");
            } else {
                Map<String, Object> data = new HashMap<>();
                data.put("name", name);
                data.put("email", email);
                data.put("password", password);
                addDataToFireStore(data, result -> {
                    callback.onCallback(true);
                    /*showOkAlertAnyWhereWithCallBack("Registration Success!!", () -> {
                        // Implement your method to handle successful registration
                    });*/
                });
            }
        });
    }

    private void getQueryFromFirestore(String field, String compareValue, FireStoreCallback<QuerySnapshot> callback) {
        dbRef.whereEqualTo(field, compareValue).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onCallback(task.getResult());
            } else {
                System.out.println("Error getting documents: " + task.getException().getMessage());
            }
        });
    }

    private void addDataToFireStore(Map<String, Object> data, FireStoreCallback<Void> callback) {
        dbRef.add(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onCallback(null);
            } else {
                System.out.println("Error adding data: " + task.getException().getMessage());
            }
        });
    }

    public void getProfile(String email, FireStoreCallback<QuerySnapshot> callback) {
        db.collection("Users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onCallback(task.getResult());
            } else {
                System.out.println("Something went wrong!!");
            }
        });
    }

    public void updateProfile(String documentId, Map<String, Object> userData, FireStoreCallback<Boolean> callback) {
        db.collection("Users").document(documentId).update(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Profile data updated successfully");
                callback.onCallback(true);
            } else {
                System.out.println("Error updating Firestore data: " + task.getException().getMessage());
                callback.onCallback(false);
            }
        });
    }

    public void removeAllListeners() {
        for (ListenerRegistration listener : notificationListeners) {
            listener.remove();
        }
        notificationListeners.clear();
    }

    public interface FireStoreCallback<T> {
        void onCallback(T result);
    }

}


