package com.org.tunestream.firebase_manager;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class ForgotPasswordManager {

    public static void sendEmail(String emailTo, String body, EmailCallback callback) {
        String myAppPassword = "aydhrrahmbvkmmeg";
        String urlString = "https://us-central1-online-diagnosis-9e50c.cloudfunctions.net/sendEmail";

        Map<String, String> data = new HashMap<>();
        data.put("subject", "Here Is your login Password For your Music App");
        data.put("loginMail", "saidireddy2307@gmail.com");
        data.put("emailFrom", "saidireddy2307@gmail.com");
        data.put("emailTo", emailTo);
        data.put("appPassword", myAppPassword);
        data.put("body", body);

        JSONObject jsonData = new JSONObject(data);

        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonData.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode >= 200 && responseCode < 300) {
                    System.out.println("Email sent successfully");
                    callback.onComplete(true);
                } else {
                    System.out.println("Error sending email: Invalid response");
                    callback.onComplete(false);
                }
            } catch (Exception e) {
                System.out.println("Error sending email: " + e.getMessage());
                callback.onComplete(false);
            }
        }).start();
    }

    public interface EmailCallback {
        void onComplete(boolean success);
    }
}

