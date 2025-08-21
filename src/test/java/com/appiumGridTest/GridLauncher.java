package com.appiumGridTest;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.AfterMethod;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.BASEPATH;

public class GridLauncher {
    static String jarPath = "src/test/resources/driver/selenium-server-4.25.0.jar";
    static String appiumMainJs = "C:/Users/Abhilasha/AppData/Roaming/npm/node_modules/appium/build/lib/main.js";
    static String nodeConfigPath = "src/test/resources/config/node-config.toml";
    static AppiumDriverLocalService service = null;


    public static void startSeleniumHub() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "standalone");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        logServerOutput(process);
        int exitCode = process.waitFor();
        System.out.println("Process exited with code: " + exitCode);
        System.out.println(" Selenium Standalone Server launched.");
    }

    public static void waitForSelenium() throws InterruptedException, IOException {
        while (isServiceUp("http://localhost:4444/status")){
            System.out.println("Waiting for Selenium Grid...");
            Thread.sleep(1000);
            stopSeleniumHub();
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
        while (isServiceUp("http://127.0.0.1:4723/wd/hub/status")) {
            System.out.println(" Waiting for Appium server...");
            Thread.sleep(1000);
        }
        System.out.println("Appium server is ready.");
    }

    public static void registerAppiumNode(String configPath) throws IOException, InterruptedException {
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            throw new IOException(" Node config file not found: " + configPath);
        }
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", jarPath, "node", "--config", configFile.getAbsolutePath());
        builder.redirectErrorStream(true);
        Process process = builder.start();
        logServerOutput(process);
        int exitCode = process.waitFor();
        System.out.println("Process exited with code: " + exitCode);
        System.out.println("Appium node registered with Selenium Grid.");
    }

    public static boolean isServiceUp(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            return connection.getResponseCode() != 200;
        } catch (IOException e) {
            return true;
        }
    }

    public static void stopAppiumServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium server stopped.");
        }
    }
    public static void stopSeleniumHub() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "stop");
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        process.waitFor();
        System.out.println("Selenium Hub stopped.");
    }
    @Override
    protected void finalize() throws Throwable {
        stopAppiumServer();
        stopSeleniumHub();
        super.finalize();
    }

    public static void shutdown() {
        stopAppiumServer();
        try {
            stopSeleniumHub();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterMethod
    static void killExistingJavaProcesses() throws IOException {
        shutdown();
        ProcessBuilder processBuilder = new ProcessBuilder("tasklist");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("java.exe")) {
                    String[] parts = line.split("\\s+");
                    String pid = parts[1];
                    new ProcessBuilder("taskkill","/PID", pid).start();
                    System.out.println("Killed process with PID: " + pid);
                }
            }
        }
    }

    private static void logServerOutput(Process process) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}