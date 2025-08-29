package demo.com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppiumSourceFetcher {
    public static void main(String[] args) throws Exception {
        String sessionId = "your-session-id-here"; // Replace with actual session ID
        String serverUrl = "http://localhost:4723"; // Appium server URL
        String endpoint = "/wd/hub/session/" + sessionId + "/source";

        URL url = new URL(serverUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Page Source:\n" + response.toString());
    }
}