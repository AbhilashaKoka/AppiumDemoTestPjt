package demo.com;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
//Post Method to Appium Server to start a session
public class AppiumSessionStarter {
    public static void main(String[] args) throws Exception {
        String serverUrl = "http://localhost:4723/session";
        String jsonPayload = """
        {
          "capabilities": {
            "alwaysMatch": {
              "platformName": "Android",
              "appium:deviceName": "emulator-5554",
              "appium:automationName": "UiAutomator2",
              "appium:appPackage": "com.google.android.calculator",
              "appium:appActivity": "com.android.calculator2.Calculator",
              "appium:noReset": true
            },
            "firstMatch": [{}]
          }
        }
        """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
        // Parse JSON and extract sessionId
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        String sessionId = root.path("value").path("sessionId").asText();

        System.out.println("Extracted Session ID: " + sessionId);
    }
}