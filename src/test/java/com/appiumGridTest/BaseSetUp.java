package com.appiumGridTest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.Capabilities;
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

import static io.appium.java_client.service.local.flags.GeneralServerFlag.BASEPATH;

public class BaseSetUp {
    public static Process appiumProcess;
    public static WebDriver driver;
    public static AndroidDriver androidDriver;
    static AppiumDriverLocalService service = null;
    static DesiredCapabilities capabilities;
    public static String jarPath = "src/test/resource/driver/selenium-server-4.25.0.jar";
    public static String servername = "standalone";
    public static String appPackage="com.google.android.calculator";
    public static  String appActivity="com.android.calculator2.Calculator";
    public static String gridUrl = "http://" + getLocalHostAddress() + ":4723";
    public static String appiumMainJs = "C:\\Users\\Abhilasha\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js";


    @BeforeSuite
    public WebDriver setup(String browser) throws IOException, InterruptedException {
            startSeleniumGridServer(jarPath, servername);
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
            case "hybrid":
                driver=createAndroidDriver("hybrid");
                return androidDriver;
            case "native":
               driver= createAndroidDriver("native");
                return androidDriver;
            default:
                throw new Error("Browser configuration is not defined!!");
        }
    }
    @AfterSuite
    public static void tearDown() throws InterruptedException {
        if (driver != null) driver.quit();
        if (androidDriver != null) androidDriver.quit();
        stopAppiumServer();
    }


    private WebDriver createWebDriver(String gridUrl, Capabilities options) throws MalformedURLException {
        return new RemoteWebDriver(new URL(gridUrl), options);
    }


    public  AndroidDriver createAndroidDriver( String cap) throws IOException {
        startAppiumServer();
        switch (cap.toLowerCase()) {
            case "hybrid":
               capabilities = setCapabilitiesForAndroidHybridApp();
                driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
                return androidDriver;
        case "native":
             capabilities = getDesiredCapabilitiesforAndroidMativeapp();
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        return androidDriver;
            default:
                throw new IllegalArgumentException("Invalid capability type: " + cap);
    }
    }

    private DesiredCapabilities setCapabilitiesForAndroidHybridApp() {
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "Android");
            caps.setCapability("deviceName", "emulator-5554");
            caps.setCapability("automationName", "UIAutomator2");
            caps.setCapability("browserName", "Chrome");
            caps.setCapability("chromedriverExecutable", "C:\\Users\\Abhilasha\\Documents\\DOCUMENT\\StudyDocumentFolder\\IDE\\APPIUMSetUp\\drivers\\chromedriver_74\\chromedriver.exe");
            caps.setCapability("noReset", true);
            return caps;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Browser isn't supported.");
        }
    }
    private static DesiredCapabilities getDesiredCapabilitiesforAndroidMativeapp() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "10");
        capabilities.setCapability("automationName", "UIAutomator2");
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);
        return capabilities;
    }



    // --- Appium server management ---
    public static void startAppiumServer() throws IOException {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1"); // Or use .usingAnyFreePort()
        builder.usingPort(4723); // Or use .usingAnyFreePort()
        builder.withAppiumJS(new File(appiumMainJs)); // Replace with your Appium path
        builder.withArgument(BASEPATH, "/wd/hub"); // Standard base path
        // Add other server arguments as needed, e.g., builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        service = AppiumDriverLocalService.buildService(builder);
        if(!service.isRunning()) {
            service.start();
            System.out.println("Appium server started at: " + service.getUrl());
        }
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

    static void logServerOutput(Process process) {
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