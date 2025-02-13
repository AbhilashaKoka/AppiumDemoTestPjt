package com.gridTest;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class BaseSetUp {
  public  static String jarPath = "src/test/resources/driver/selenium-server-4.25.0.jar";
    public static WebDriver driver;

    public static String nodeConfigPath="src/test/resources/config/nodeConfig.json";

    //configuration file
    static String servername = "node";


    @BeforeSuite
    public static void setup(String browser) throws Exception {
//    public static void main(String[] args) throws MalformedURLException {
//       String browser="chrome";

        try{

            startSeleniumGridServer(jarPath,servername);
           // startappiumServer(nodeConfigPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions chromeOptions = new ChromeOptions();
            driver = (new RemoteWebDriver(new URL("http://"+getLocalHostAddress()+":4444"), chromeOptions));
        }
        else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            driver = (new RemoteWebDriver(new URL("http://"+getLocalHostAddress()+":4444"), firefoxOptions));

        } else if (browser.equalsIgnoreCase("edge")) {
            EdgeOptions edgeOptions = new EdgeOptions();
            driver = (new RemoteWebDriver(new URL("http://"+getLocalHostAddress()+":4444"), edgeOptions));
        }
        else {
            throw new Error("Browser configuration is not defined!!");
        }

    }




    @AfterSuite
    public static void tearDown() throws InterruptedException {
        driver.quit();
    }


    public static void startSeleniumGridServer(String jarPath,String serverType) throws IOException, InterruptedException {
        killExistingJavaProcesses();
        switch(serverType) {
            case "node":
                startNodeServer(jarPath);
                break;
            default:
                System.out.println("Invalid server type: " + serverType);
                break;
        }
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


    public static void startappiumServer(String jarPath) throws IOException, InterruptedException {
        Process appiumprocess = launchAppium(jarPath);
        logServerOutput(appiumprocess);
        int exitCode = appiumprocess.waitFor();
        System.out.println("Process exited with code: " + exitCode);
    }



    private static Process launchSeleniumHubServer(String jarPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "hub");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium Hub Server started.");
        return process;
    }




    private static Process launchSeleniumNodeProcess(String jarPath) throws IOException {
       // ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "node", "--log", "node.log");
          ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarPath, "node", "--hub" , "http://"+getLocalHostAddress()+":"+getPort());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium Node Server started.");
        return process;
    }



    private static Process launchAppium(String config) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder( "appium", "--nodeconfig", config);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        System.out.println("Selenium Standalone Server started.");
        return process;
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
                    new ProcessBuilder("taskkill","/PID", pid).start();
                    System.out.println("Killed process with PID: " + pid);
                }
            }
        }
    }
}
