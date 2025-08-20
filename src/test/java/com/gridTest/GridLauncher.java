package com.gridTest;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.BASEPATH;

public class GridLauncher {
    static String jarPath = "src/test/resources/driver/selenium-server-4.25.0.jar";
    static String appiumMainJs = "C:/Users/Abhilasha/AppData/Roaming/npm/node_modules/appium/build/lib/main.js";
    static String nodeConfigPath = "src/test/resources/config/node-config.toml";
    static AppiumDriverLocalService service = null;

    public static void main(String[] args) throws IOException, InterruptedException {
        startSeleniumHub();
        waitForSelenium();
        startAppiumServer();
        waitForAppium();
        registerAppiumNode(nodeConfigPath);
    }

    public static void startSeleniumHub() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "standalone");
        processBuilder.inheritIO();
        processBuilder.start();
        System.out.println(" Selenium Standalone Server launched.");
    }

    public static void waitForSelenium() throws InterruptedException {
        while (!isServiceUp("http://localhost:4444/status")){
            System.out.println("Waiting for Selenium Grid...");
            Thread.sleep(1000);
        }
        System.out.println("Selenium Grid is up.");
    }

    public static void startAppiumServer() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .withAppiumJS(new File(appiumMainJs))
                .withArgument(BASEPATH, "/wd/hub");
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        System.out.println(" Appium server started at: " + service.getUrl());
    }

    public static void waitForAppium() throws InterruptedException {
        while (!isServiceUp("http://127.0.0.1:4723/wd/hub/status")) {
            System.out.println(" Waiting for Appium server...");
            Thread.sleep(1000);
        }
        System.out.println("Appium server is ready.");
    }

    public static void registerAppiumNode(String configPath) throws IOException {
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            throw new IOException(" Node config file not found: " + configPath);
        }
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", jarPath, "node", "--config", configFile.getAbsolutePath());
        builder.inheritIO();
        builder.start();
        System.out.println("Appium node registered with Selenium Grid.");
    }

    public static boolean isServiceUp(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }
}