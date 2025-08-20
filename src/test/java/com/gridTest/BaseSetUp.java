package com.gridTest;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class BaseSetUp {
    static String jarPath = "src/test/resource/driver/selenium-server-4.25.0.jar";
    public static WebDriver driver;
    public static AndroidDriver androidDriver;
    static String servername = "standalone";
    static Process appiumProcess;

    @BeforeSuite
    public WebDriver setup(String browser) throws IOException, InterruptedException {
        try {
            startSeleniumGridServer(jarPath, servername);
            startAppiumServer();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String gridUrl = "http://" + getLocalHostAddress() + ":4444";
        switch (browser.toLowerCase()) {
            case "chrome":
                driver = createWebDriver(gridUrl, new ChromeOptions());
                return driver;
            case "firefox":
                driver = createWebDriver(gridUrl, new FirefoxOptions());
                return driver;
            case "edge":
                driver = createWebDriver(gridUrl, new EdgeOptions());
                return driver;
            case "android":
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
                caps.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
                caps.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
                caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
                androidDriver = new AndroidDriver(new URL(gridUrl + "/wd/hub"), caps);
                androidDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                return androidDriver;
            default:
                throw new Error("Browser configuration is not defined!!");
        }
    }

    private WebDriver createWebDriver(String gridUrl, org.openqa.selenium.Capabilities options) throws MalformedURLException {
        return new RemoteWebDriver(new URL(gridUrl), options);
    }
    @AfterSuite
    public static void tearDown() throws InterruptedException {
        if (driver != null) driver.quit();
        if (androidDriver != null) androidDriver.quit();
        stopAppiumServer();
    }

    // --- Appium server management ---
    public static void startAppiumServer() throws IOException {
        // Adjust the path to your Appium main.js and Node.js as needed
        String nodePath = "C:\\Program Files\\nodejs\\node.exe";
        String appiumMainJs = "C:\\Users\\Abhilasha\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js";
        ProcessBuilder pb = new ProcessBuilder(nodePath, appiumMainJs, "--port", "4723", "--base-path", "/wd/hub", "--log-level", "info", "--allow-insecure", "chromedriver_autodownload", "--use-plugins", "element-wait");
        pb.redirectErrorStream(true);
        appiumProcess = pb.start();
        logServerOutput(appiumProcess);
        System.out.println("Appium server started.");
        // Wait a bit for Appium to be ready
        try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
    }

    public static void stopAppiumServer() {
        if (appiumProcess != null && appiumProcess.isAlive()) {
            appiumProcess.destroy();
            System.out.println("Appium server stopped.");
        }
    }

    // --- Selenium Grid code below (unchanged except for package fix) ---

    public static void startSeleniumGridServer(String jarPath, String serverType) throws IOException, InterruptedException {
        killExistingJavaProcesses();
        switch (serverType) {
            case "standalone":
                startStandaloneServer(jarPath);
                break;
            case "node":
                startNodeServer(jarPath);
                break;
            case "distributed":
                startDistributedServer(jarPath);
                break;
            default:
                System.out.println("Invalid server type: " + serverType);
                break;
        }
    }

    private static Process launchSeleniumStandalone(String jarPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "standalone");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium Standalone Server started.");
        return process;
    }

    private static Process launchSeleniumHubServer(String jarPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "hub");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium Hub Server started.");
        return process;
    }

    private static Process launchSeleniumNodeProcess(String jarPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "node", "--log", "node.log");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium Node Server started.");
        return process;
    }

    private static Process launchEventBusProcess(String jarPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "event-bus", "--publish-events", "tcp://" + getLocalHostAddress() + ":4442", "--subscribe-events", "tcp://" + getLocalHostAddress() + ":4443 --port 5557");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium Event Bus started.");
        return process;
    }

    private static Process launchSessionQueueProcess(String jarPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "sessionqueue", "--port 5559");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium SessionQueue started.");
        return process;
    }

    private static Process launchSessionMappingProcess(String jarPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "sessions", "--publish-events", "tcp://" + getLocalHostAddress() + ":4442", "--subscribe-events", "tcp://" + getLocalHostAddress() + ":4443", "--port 5556");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium Session Mapping started.");
        return process;
    }

    private static Process launchDistributorService(String jarPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "distributor", "--publish-events", "tcp://" + getLocalHostAddress() + ":4442", "--subscribe-events", "tcp://" + getLocalHostAddress() + ":4443", "--sessions", "http://" + getLocalHostAddress() + ":5556", "--sessionqueue", "http://" + getLocalHostAddress() + ":5559", "--port 5553", "--bind-bus false");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium DistributorService started.");
        return process;
    }

    private static Process launchRouterService(String jarPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "router --sessions", "http://" + getLocalHostAddress() + ":5556", "--distributor", "http://" + getLocalHostAddress() + ":5553", "--sessionqueue", "http://" + getLocalHostAddress() + ":5559", "--port 4444");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium RouterService started.");
        return process;
    }

    private static Process launchBrowserNode(String jarPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "node", "--publish-events", "tcp://" + getLocalHostAddress() + ":4442", "--subscribe-events", "tcp://" + getLocalHostAddress() + ":4443");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium Node Server started.");
        return process;
    }

    public static void startDistributedServer(String jarPath) throws IOException, InterruptedException {
        Process eventBus = launchEventBusProcess(jarPath);
        logServerOutput(eventBus);
        int exitCode3 = eventBus.waitFor();
        System.out.println("Process exited with code:" + exitCode3);

        Process routerServer = launchRouterService(jarPath);
        logServerOutput(routerServer);
        int exitCode7 = routerServer.waitFor();
        System.out.println("Process exited with code:" + exitCode7);

        Process sessionQueue = launchSessionQueueProcess(jarPath);
        logServerOutput(sessionQueue);
        int exitCode4 = sessionQueue.waitFor();
        System.out.println("Process exited with code:" + exitCode4);

        Process distributorServer = launchDistributorService(jarPath);
        logServerOutput(distributorServer);
        int exitCode6 = distributorServer.waitFor();
        System.out.println("Process exited with code:" + exitCode6);

        Process Node12 = launchBrowserNode(jarPath);
        logServerOutput(Node12);
        int exitCode8 = Node12.waitFor();
        System.out.println("Process exited with code:" + exitCode8);

        Process SessionMap = launchSessionMappingProcess(jarPath);
        logServerOutput(SessionMap);
        int exitCode5 = SessionMap.waitFor();
        System.out.println("Process exited with code:" + exitCode5);
    }

    public static void startNodeServer(String jarPath) throws IOException, InterruptedException {
        Process hubprocess = launchSeleniumHubServer(jarPath);
        logServerOutput(hubprocess);
        int exitCode1 = hubprocess.waitFor();
        System.out.println("Process exited with code: " + exitCode1);
        Process nodeprocess = launchSeleniumNodeProcess(jarPath);
        logServerOutput(nodeprocess);
        int exitCode2 = nodeprocess.waitFor();
        System.out.println("Process exited with code: " + exitCode2);
    }

    public static void startStandaloneServer(String jarPath) throws IOException, InterruptedException {
        Process standaloneprocess = launchSeleniumStandalone(jarPath);
        logServerOutput(standaloneprocess);
        int exitCode = standaloneprocess.waitFor();
        System.out.println("Process exited with code: " + exitCode);
    }

    private static void killExistingJavaProcesses() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("tasklist");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("java.exe")) {
                    String[] parts = line.split("\\s+");
                    String pid = parts[1];
                    new ProcessBuilder("taskkill", "/PID", pid).start();
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

    public static String getLocalHostAddress() {
        String str = null;
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            System.out.println("Local IP Address: " + localAddress.getHostAddress());
            str = localAddress.getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("Could not get IP address: " + e.getMessage());
        }
        return str;
    }

    public static int getPort() {
        int port = 0;
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            port = serverSocket.getLocalPort();
            System.out.println("Local Port: " + port);
        } catch (IOException e) {
            System.err.println("Could not get port: " + e.getMessage());
        }
        return port;
    }
}