package com.appiumGridTest;
import org.testng.annotations.AfterMethod;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


public class GridLauncher {
    static String jarPath = "src/test/resources/driver/selenium-server-4.25.0.jar";
    static String appiumMainJs = "C:/Users/Abhilasha/AppData/Roaming/npm/node_modules/appium/build/lib/main.js";
    static String nodeConfigPath = "src/test/resources/config/node-config.toml";
    static String gridConfigPath="src/test/resources/config/grid-config.toml";
   static String nodeConfigPath2="C:\\Users\\Abhilasha\\Documents\\DOCUMENT\\StudyDocumentFolder\\IDE\\IdeaProjects\\mobdemoprjt\\src\\test\\resources\\config\\nodeConfig.json";

    public static void startSeleniumHub() throws IOException, InterruptedException {
     //  ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "standalone","-role","hub","--config", gridConfigPath, "--selenium-manager", "true" );
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "standalone","--selenium-manager");
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



        public static void startAppiumServer() throws InterruptedException, IOException {
            ProcessBuilder processBuilder2 = new ProcessBuilder(
                    "node", appiumMainJs, "--nodeconfig", nodeConfigPath2, "--base-path", "/wd/hub"
            );
            processBuilder2.redirectErrorStream(true);
            Process process2 = processBuilder2.start();
            logServerOutput(process2);
            int exitCode2 = process2.waitFor();
            System.out.println("Process exited with code: " + exitCode2);
            System.out.println(" Configure Appium Node JSON file and connect it to the Selenium Grid Hub");
        }

    public static void waitForAppium() throws InterruptedException {
        while (isServiceUp("http://192.168.1.3:5555/wd/hub/status")) {
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
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", jarPath, "node", "--config", "src/test/resources/config/nodeConfig-android.json");
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
                  System.out.println("Appium server stopped.");
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
    public static String  getLocalHostAddress(){
        String str=null;
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            System.out.println("Local IP Address: " + localAddress.getHostAddress());
            str= localAddress.getHostAddress();

        } catch (UnknownHostException e) {
            System.err.println("Could not get IP address: " + e.getMessage());
        }
        return str;
    }


    public static int getPort(){
        int port = 0;
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            // Get the port number
            port = serverSocket.getLocalPort();
            // Print the  port number
            System.out.println("Local Port: " + port);
        } catch (IOException e) {
            System.err.println("Could not get port: " + e.getMessage());
        }
        return port;
    }

}