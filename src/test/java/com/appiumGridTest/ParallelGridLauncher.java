package com.appiumGridTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelGridLauncher {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4); // Adjust thread count as needed

        executor.submit(() -> {
            try {
                GridLauncher.startSeleniumHub();
            } catch (Exception e) {
                System.err.println("Error starting Selenium Hub: " + e.getMessage());
            }
        });

        executor.submit(() -> {
            try {
                GridLauncher.startAppiumServer();
            } catch (Exception e) {
                System.err.println("Error starting Appium Server: " + e.getMessage());
            }
        });

        executor.submit(() -> {
            try {
                GridLauncher.startSeleniumNode();
            } catch (Exception e) {
                System.err.println("Error starting Selenium Node: " + e.getMessage());
            }
        });

        executor.submit(() -> {
            try {
                GridLauncher.registerAppiumNode(GridLauncher.ConfigPath);
            } catch (Exception e) {
                System.err.println("Error registering Appium Node: " + e.getMessage());
            }
        });

        executor.shutdown(); // Optional: wait for all tasks to complete
    }
}