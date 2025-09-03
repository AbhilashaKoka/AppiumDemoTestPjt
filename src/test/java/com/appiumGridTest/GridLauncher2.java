package com.appiumGridTest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    public class GridLauncher2 {
        static String SeleniumGridJar = "src/test/resources/driver/selenium-server-4.25.0.jar";
        static String nodePath = "C:/Users/Abhilasha/AppData/Roaming/npm/node_modules/appium/build/lib/main.js";
        static String device2Config = "src/test/resources/config/node-2.toml";
        static String device1Config ="src/test/resources/config/node-1.toml";
       static String appiumConfig= "src/test/resources/config/appium.yml";
       static String appiumPath=  "C:\\Users\\Abhilasha\\AppData\\Roaming\\npm\\appium";
     //  " C:\Users\Abhilasha\AppData\Roaming\npm\appium.cmd";

        public List<List<String>> buildCommandList(String jarPath, String nodeServerPath, String nodeDeviceConfig) {
            List<List<String>> commands = new ArrayList<>();
            // Command to start Selenium Grid hub
            commands.add(Arrays.asList("java", "-jar", jarPath, "hub","--host","192.168.1.3","--port", "4444"));
            // Command to start Appium node with config
           //commands.add(Arrays.asList("node", nodeServerPath, "--base-path", "/wd/hub"));
           // commands.add(Arrays.asList(appiumPath, "appium",  "--config", appiumConfig));
          //  commands.add(Arrays.asList("cmd.exe", "/c", appiumPath, "server", "--address","127.0.0.1", "--port 4723", "--base-path", "/wd/hub"));
          // Command to start Selenium Grid node
            commands.add(Arrays.asList("java", "-jar", jarPath, "node", "--config", nodeDeviceConfig));
            return commands;
        }


        public void launchProcesses() {
            List<List<String>> commandList= buildCommandList(SeleniumGridJar, nodePath, device1Config);
            System.out.println("Starting processes...");
            startServer();
            for (List<String> command : commandList) {
                try {
                    ProcessBuilder builder = new ProcessBuilder(command);
                    builder.redirectErrorStream(true); // Merge stderr into stdout
                    Process process = builder.start();
                    // Optional: log output asynchronously
                    logProcessOutput(process, String.join(" ", command));
                    // Optional: wait for process or implement health check
                    // process.waitFor(); // Blocking call if needed
                } catch (IOException e) {
                    System.err.println("Failed to start process: " + String.join(" ", command));
                    e.printStackTrace();
                }

            }

        }
        private void logProcessOutput(Process process, String label) {
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[" + label + "] " + line);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading output for: " + label);
                    e.printStackTrace();
                }
            }).start();
        }


        public static void startServer() {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("cmd.exe /c start cmd.exe /k \"appium -a 127.0.0.1 -p 4723\"");
                Thread.sleep(8000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
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
    }


