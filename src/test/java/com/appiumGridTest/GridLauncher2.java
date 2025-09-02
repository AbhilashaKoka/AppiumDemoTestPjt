package com.appiumGridTest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    public class GridLauncher2 {
        static String jarPath = "src/test/resources/driver/selenium-server-4.25.0.jar";
        static String appiumMainJs = "C:/Users/Abhilasha/AppData/Roaming/npm/node_modules/appium/build/lib/main.js";
        static String device2Config = "src/test/resources/config/node-2.toml";
        static String device1Config ="src/test/resources/config/node-1.toml";
        static String ConfigPath ="C:\\Users\\Abhilasha\\Documents\\DOCUMENT\\StudyDocumentFolder\\IDE\\IdeaProjects\\mobdemoprjt\\src\\test\\resources\\config\\nodeConfig.json";


        public List<List<String>> buildCommandList(String jarPath, String appiumMainJs, String configPath, String node2Path) {
            List<List<String>> commands = new ArrayList<>();
            // Command to start Selenium Grid hub
            commands.add(Arrays.asList("java", "-jar", jarPath, "hub"));
            // Command to start Appium node with config
            commands.add(Arrays.asList("node", appiumMainJs, "--nodeconfig", configPath));
            // Command to start Selenium Grid node
            commands.add(Arrays.asList("java", "-jar", jarPath, "node", "--config", node2Path));
            return commands;
        }


        public void launchProcesses() {
            List<List<String>> commandList= buildCommandList(jarPath, appiumMainJs, ConfigPath, device1Config);
            System.out.println("Starting processes...");
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


