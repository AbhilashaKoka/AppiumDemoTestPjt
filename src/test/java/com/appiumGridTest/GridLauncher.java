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
    static String node1Path = "src/test/resources/config/node-relay.toml";
    static String node2Path ="src/test/resources/config/node2.toml";
    static String ConfigPath ="C:\\Users\\Abhilasha\\Documents\\DOCUMENT\\StudyDocumentFolder\\IDE\\IdeaProjects\\mobdemoprjt\\src\\test\\resources\\config\\nodeConfig.json";

    public static void startAppiumServer() throws InterruptedException, IOException {
        ProcessBuilder processBuilder3 = new ProcessBuilder(
                "node", appiumMainJs, "--nodeconfig", ConfigPath, "--base-path", "/wd/hub" );
//        ProcessBuilder processBuilder3 = new ProcessBuilder(
//                "appium", "-a", "0.0.0.0", "-p", "4723" );
        processBuilder3.redirectErrorStream(true);
        Process process3 = processBuilder3.start();
        logServerOutput(process3);
        int exitCode3= process3.waitFor();
        System.out.println("Process exited with code: " + exitCode3);
        System.out.println("Appium Server started.");
//        //  ProcessBuilder processBuilder2 = new ProcessBuilder("java", "-jar", jarPath, "node", "--detect-drivers", "--publish-events", "tcp://10.0.75.1:4442", "--subscribe-events", "tcp://10.0.75.1:4443");
//        ProcessBuilder processBuilder2 = new ProcessBuilder("appium", "--port", "4723", nodeConfigPath2 );
//        processBuilder2.redirectErrorStream(true);
//        Process process2 = processBuilder2.start();
//        logServerOutput(process2);
//        int exitCode2 = process2.waitFor();
//        System.out.println("Process exited with code: " + exitCode2);
//        System.out.println("Selenium Node Server started.");
    }

    public static void startSeleniumHub() throws IOException, InterruptedException {
        ProcessBuilder processBuilder1 = new ProcessBuilder("java","-jar", jarPath,"standalone" ,"--config", node2Path);
        processBuilder1.redirectErrorStream(true);
        Process process1 = processBuilder1.start();
        logServerOutput(process1);
        int exitCode1 = process1.waitFor();
        System.out.println("Process exited with code: " + exitCode1);
        System.out.println("Selenium Server started.");

    }

    public static void waitForSelenium() throws InterruptedException, IOException {
        while (isServiceUp("http://localhost:4444/status")){
            System.out.println("Waiting for Selenium server...");
            Thread.sleep(1000);
            stopSeleniumHub();
        }
        System.out.println("Selenium Grid is up.");
    }





    public static void waitForAppium() throws InterruptedException {
        while (isServiceUp("http://0.0.0.0:4723/wd/hub/status")) {
            System.out.println(" Waiting for Appium server...");
            Thread.sleep(1000);
        }
        System.out.println("Appium server is ready.");
    }
//By Using Relay Server-Standalone or node with relay server
    public static void registerAppiumNode(String configPath) throws IOException, InterruptedException {
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            throw new IOException(" Node config file not found: " + configPath);
        }
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", jarPath, "node", "--config", "src/test/resources/config/nodeConfig.json");
        builder.redirectErrorStream(true);
        Process process4 = builder.start();
        logServerOutput(process4);
        int exitCode4 = process4.waitFor();
        System.out.println("Process exited with code: " + exitCode4);
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
        ProcessBuilder processBuilder = new ProcessBuilder( "java",
                "-jar", jarPath,
                 "stop");
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