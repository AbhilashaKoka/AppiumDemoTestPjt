package demo.com;

import java.net.HttpURLConnection;
import java.net.URL;

public class SessionTerminator {
    public static void main(String[] args) throws Exception {
        String sessionId = "your-session-id"; // Replace with actual session ID
        String serverUrl = "http://localhost:4723/wd/hub/session/" + sessionId;

        URL url = new URL(serverUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        System.out.println("Session terminated. Response code: " + responseCode);
    }
}