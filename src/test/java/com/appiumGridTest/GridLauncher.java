package com.appiumGridTest;
import org.testng.annotations.AfterMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


public class GridLauncher {
    static String SeleniumGridJar = "src/test/resources/driver/selenium-server-4.25.0.jar";
    static String nodePath = "C:/Users/Abhilasha/AppData/Roaming/npm/node_modules/appium/build/lib/main.js";
    static String device2Config = "src/test/resources/config/node-2.toml";
    static String device1Config ="src/test/resources/config/node-1.toml";
    static String appiumPath=  "C:\\Users\\Abhilasha\\AppData\\Roaming\\npm\\appium";
    //  " C:\Users\Abhilasha\AppData\Roaming\npm\appium.cmd";

    public static void startSeleniumHub() throws IOException, InterruptedException {
        ProcessBuilder HubStarter = new ProcessBuilder("java","-jar", SeleniumGridJar,"hub" );
        Process HubProcess = HubStarter.start();
        logServerOutput(HubProcess);
        int HubexitCode = HubProcess.waitFor();
        System.out.println("Process exited with code: " + HubexitCode);
        System.out.println("Hub started.");
    }

    public static void startAppiumServer() throws InterruptedException, IOException {
       ProcessBuilder AppiumStarter = new ProcessBuilder("node", nodePath , "--base-path", "/wd/hub");
        //ProcessBuilder AppiumStarter = new ProcessBuilder("cmd.exe", "/c", appiumPath, "server", "--address","127.0.0.1", "--port 4723", "--base-path", "/wd/hub");
        AppiumStarter.redirectErrorStream(true);
        Process AppiumProcess = AppiumStarter.start();
        logServerOutput(AppiumProcess);
        int AppiumexitCode = AppiumProcess.waitFor();
        System.out.println("Process exited with code: " + AppiumexitCode);
        System.out.println("Appium started.");
}

    public static void startSeleniumNode() throws IOException, InterruptedException {
        ProcessBuilder NodeStarter = new ProcessBuilder("java","-jar", SeleniumGridJar,"node" ,"--config", device1Config);
        NodeStarter.redirectErrorStream(true);
        Process NodeProcess = NodeStarter.start();
        logServerOutput(NodeProcess);
        int NodexitCode = NodeProcess.waitFor();
        System.out.println("Process exited with code: " + NodexitCode);
        System.out.println("Node started.");
    }

    public static void waitForHub() throws InterruptedException, IOException {
        while (!isServiceUp("http://localhost:4444/status")){
            System.out.println("Waiting for Hub...");
            Thread.sleep(1000);
                   }
        System.out.println("Hub is up.");
    }

    public static void waitForAppium() throws InterruptedException {
        while (!isServiceUp("http://0.0.0.0:4723/wd/hub/status")) {
            System.out.println(" Waiting for Appium.......");
            Thread.sleep(1000);
        }
        System.out.println("Appium is ready.");
    }

    public static void waitForNode() throws InterruptedException, IOException {
        while (!isServiceUp("http://localhost:5555/status")){
            System.out.println("Waiting for Node...");
            Thread.sleep(1000);
        }
        System.out.println("Node is up.");
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


    @AfterMethod
    static void killExistingJavaProcesses() throws IOException {
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

    public static void preLaunchCleanUpSetUp() throws IOException, InterruptedException {
        String[] packages = {
                "io.appium.uiautomator2.server",
                "io.appium.uiautomator2.server.test"
        };

        for (String pkg : packages) {
            ProcessBuilder pb = new ProcessBuilder("adb", "-s", "emulator-5554", "uninstall", pkg);
            pb.inheritIO().start().waitFor();
        }
    }


    public static void  checkPluginInstalled() throws IOException, InterruptedException {
        ProcessBuilder processBuilder4 = new ProcessBuilder("appium", "plugin", "list");
        processBuilder4.redirectErrorStream(true);
        Process process4 = processBuilder4.start();
        logServerOutput(process4);
        int exitCode = process4.waitFor();
        System.out.println("Process exited with code: " + exitCode);
    }

    public static void installPlugin(String pluginName) throws IOException, InterruptedException {
        ProcessBuilder processBuilder5 = new ProcessBuilder("appium", "plugin", "install", pluginName);
        processBuilder5.redirectErrorStream(true);
        Process process5 = processBuilder5.start();
        logServerOutput(process5);
        int exitCode = process5.waitFor();
        System.out.println("Process exited with code: " + exitCode);
    }


}