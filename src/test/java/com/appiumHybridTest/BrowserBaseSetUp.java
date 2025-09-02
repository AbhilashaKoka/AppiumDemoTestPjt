package com.appiumHybridTest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.BASEPATH;

public class BrowserBaseSetUp {

    static AppiumDriverLocalService service = null;
    AndroidDriver driver;
    static String appiumMainJs = "C:/Users/Abhilasha/AppData/Roaming/npm/node_modules/appium/build/lib/main.js";


   @BeforeClass
    public void startServer() throws  MalformedURLException {
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

    public  AndroidDriver createAndroidDriver() throws MalformedURLException {
        startServer();
        try {
            DesiredCapabilities capabilities = setCapabilitiesForAndroid();
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return driver;
    }

@AfterClass
    public static void stopAppiumServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium server stopped.");



        }
    }


    private DesiredCapabilities setCapabilitiesForAndroid() {
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "android");
            caps.setCapability("deviceName", "emulator-5554");
            caps.setCapability("automationName", "UIAutomator2");
            caps.setCapability("platformVersion", "14");
            caps.setCapability("browserName", "chrome");
            caps.setCapability("chromedriverExecutable", "C:\\Users\\Abhilasha\\Documents\\DOCUMENT\\StudyDocumentFolder\\IDE\\APPIUMSetUp\\drivers\\chromedriver_113.0.5672.63\\chromedriver.exe");
            caps.setCapability("noReset", true);
            return caps;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Browser isn't supported.");
        }
    }

    public boolean isChromeInstalled(String deviceId) throws IOException {
        ProcessBuilder builder = new ProcessBuilder("adb", "-s", deviceId, "shell", "pm", "list", "packages");
        Process process = builder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            return reader.lines().anyMatch(line -> line.contains("com.android.chrome"));
        }
    }
    public void checkVersionOfBrowser() throws IOException {
        ProcessBuilder builder=new ProcessBuilder("adb","-s","emulator-5554","shell","dumpsys package","com.android.chrome"," |"," findstr versionName");
         Process process = builder.start();
         BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        try {
            while ((line=reader.readLine())!=null)
            {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}