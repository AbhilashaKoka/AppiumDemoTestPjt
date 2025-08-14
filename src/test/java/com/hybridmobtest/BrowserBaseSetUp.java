package com.hybridmobtest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class BrowserBaseSetUp {
    AndroidDriver<MobileElement> driver;
    static AppiumDriverLocalService service = null;

    @BeforeSuite
    public AndroidDriver<MobileElement> getMobileDriver() throws MalformedURLException {
        driver = createAndroidDriver();
        return driver;
    }

    @AfterSuite
    public void closeAndroidDriver() {
        stopServer();
        if (driver != null) {
            driver.quit();
        }
    }

    private AndroidDriver<MobileElement> createAndroidDriver() throws MalformedURLException {
        //execKill(1L);
        startServer("local");
        DesiredCapabilities capabilities = setCapabilitiesForAndroid();
        driver = new AndroidDriver<>(new URL(service.getUrl().toString()), capabilities);
        System.out.println("initialised driver");
        return driver;
    }

    private DesiredCapabilities setCapabilitiesForAndroid() {
        try {
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("deviceName", "emulator-5554");
            cap.setCapability("platformName", "Android");
            cap.setCapability("platformVersion", "10");
            cap.setCapability("automationName", "UIAutomator2");
            cap.setCapability("browserName", "Chromium");
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setExperimentalOption("w3c", false);
            cap.setCapability("appium:chromeOptions", chromeOptions);
            return cap;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Browser isn't supported.");
        }
    }

    public static void execKill(long minutes) throws InterruptedException {
        try {
            Thread.sleep(minutes * 60L * 1000L);
            Runtime.getRuntime().exec("cmd /c TASKKILL /F /IM node.exe");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void startServer(String serviceName) {
        switch (serviceName) {
            case "grid":
                AppiumServiceBuilder builder1  = new AppiumServiceBuilder();
                builder1.withIPAddress("0.0.0.0");
                builder1.usingPort(4723);
                builder1.withArgument(() -> "--use-drivers", "uiautomator2,chromium");
                builder1.withArgument(() -> "--use-plugins", "execute-driver");
                service = builder1.build();
                service.start();
                break;
            case "local":
                AppiumServiceBuilder builder2  = new AppiumServiceBuilder();
                builder2.usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"));
                builder2.withAppiumJS(new File("C:\\Users\\Abhilasha\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"));
                builder2.withIPAddress("127.0.0.1");
                builder2.usingPort(4723);
                builder2.withArgument(() -> "--use-drivers", "uiautomator2,chromium");
                builder2.withArgument(() -> "--use-plugins", "execute-driver");
                builder2.withLogFile(new File("AppiumLog.txt"));
                service = builder2.build();
                service.start();
                break;
            default:
                throw new IllegalArgumentException("Unsupported service: " + serviceName);
        }
    }

    public void stopServer() {
        if (service != null) {
            service.stop();
        }
    }
}