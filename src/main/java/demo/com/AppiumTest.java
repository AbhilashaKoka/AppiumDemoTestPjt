package demo.com;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static io.appium.java_client.remote.AndroidMobileCapabilityType.APP_ACTIVITY;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.APP_PACKAGE;

//Post Method to Appium Server to start a session
public class AppiumTest {

    static String sessionId;
   // Replace with actual session ID
    static String baseURL = "http://localhost:4723"; // Appium server URL
    static String serverUrl = baseURL+"/session";
    static String endpoint = "/wd/hub/session/" + sessionId + "/source";
    static String serverUrl2 = baseURL+"/wd/hub/session/" + sessionId;
    public static void appiumStartup() throws IOException, InterruptedException {
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
         sessionId = root.path("value").path("sessionId").asText();
        System.out.println("Extracted Session ID: " + sessionId);
    }

    public static void appiumSrcFetch() throws Exception {
        URL url = new URL(baseURL + endpoint);
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

    public static void sessionTerminate() throws Exception {


        URL url = new URL(serverUrl2);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        int responseCode = conn.getResponseCode();
        System.out.println("Session terminated. Response code: " + responseCode);
    }
    public static void startServer() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("cmd.exe /c start cmd.exe /k \"appium -a 127.0.0.1 -p 4723\"");
            Thread.sleep(8000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Runtime runtime2 = Runtime.getRuntime();
        try {
            // Corrected exec usage for starting Selenium server
            runtime2.exec(new String[] {
                    "java",
                    "-jar",
                    "src/test/resources/driver/selenium-server-4.25.0.jar",
                    "standalone",
                    "--config",
                    "src/test/resources/config/node2.toml"
            });
            Thread.sleep(8000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("taskkill /F /IM node.exe");
            runtime.exec("taskkill /F /IM cmd.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        caps.setCapability(APP_PACKAGE, "com.google.android.calculator");
        caps.setCapability(APP_ACTIVITY, "com.android.calculator2.Calculator");
        // Inject settings if supported by plugin
        Map<String, Object> settings = new HashMap<>();
        settings.put("ignoreUnimportantViews", true);
        settings.put("waitForIdleTimeout", 0);
        caps.setCapability("appium:settings", settings);

        // ⏱ Set timeout to 30 seconds
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 30);
        return caps;
    }


    }
