package com.hybridmobtest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.BASEPATH;

//import static io.appium.java_client.service.local.flags.GeneralServerFlag.BASEPATH;

public class BrowserBaseSetUp {
    AndroidDriver driver;
    static AppiumDriverLocalService service = null;


    @BeforeMethod
    public AndroidDriver getMobileDriver() throws  MalformedURLException {
        driver = createAndroidDriver();
        return driver;
    }

    @AfterMethod
    public static void stopAppiumServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium server stopped.");
        }
    }

    private AndroidDriver createAndroidDriver() throws MalformedURLException {
        startServer();
        DesiredCapabilities capabilities = setCapabilitiesForAndroid();
        driver = new AndroidDriver(new URL(service.getUrl().toString()), capabilities);
        System.out.println("initialised driver");
        return driver;
    }

    private DesiredCapabilities setCapabilitiesForAndroid() {
        try {
                DesiredCapabilities caps = new DesiredCapabilities();
                caps.setCapability("platformName", "Android");
                caps.setCapability("deviceName", "emulator-5554");
                caps.setCapability("automationName", "UIAutomator2");
                caps.setCapability("browserName", "Chrome");
                caps.setCapability("noReset", true);
                caps.setCapability("chromedriverAutodownload", true);
                 ChromeOptions chromeOptions = new ChromeOptions();
                  chromeOptions.setExperimentalOption("w3c", false);
                  caps.setCapability("appium:chromeOptions", chromeOptions);
            return caps;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Browser isn't supported.");
        }
    }

    public void startServer() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1"); // Or use .usingAnyFreePort()
        builder.usingPort(4723); // Or use .usingAnyFreePort()
        builder.withAppiumJS(new File("C:\\Users\\Abhilasha\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js")); // Replace with your Appium path
        builder.withArgument(BASEPATH, "/wd/hub"); // Standard base path
        // Add other server arguments as needed, e.g., builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        System.out.println("Appium server started at: " + service.getUrl());   service = AppiumDriverLocalService.buildService(builder);
                service.start();
                System.out.println("Appium server started at: " + service.getUrl());

    }


}