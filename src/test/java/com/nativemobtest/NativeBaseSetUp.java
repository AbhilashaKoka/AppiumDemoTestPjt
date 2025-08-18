package com.nativemobtest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;




public class NativeBaseSetUp {
    static AndroidDriver driver ;
     private static AppiumDriverLocalService service;


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


    private  static  AndroidDriver createAndroidDriver() throws MalformedURLException {
        startServer();
        try {
            DesiredCapabilities capabilities = getDesiredCapabilities();
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
       return driver;
    }


    private static void startServer() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1"); // Or use .usingAnyFreePort()
        builder.usingPort(4723); // Or use .usingAnyFreePort()
        builder.withAppiumJS(new File("C:\\Users\\Abhilasha\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js")); // Replace with your Appium path
        // builder.withArgument(BASEPATH, "/wd/hub"); // Standard base path
        // Add other server arguments as needed, e.g., builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        System.out.println("Appium server started at: " + service.getUrl());
    }


    private static DesiredCapabilities getDesiredCapabilities() {
    DesiredCapabilities capabilities = new DesiredCapabilities();
     capabilities.setCapability("deviceName", "emulator-5554");
     capabilities.setCapability("platformName", "Android");
     capabilities.setCapability("platformVersion", "10");
     capabilities.setCapability("appPackage", "com.google.android.calculator");
     capabilities.setCapability("appActivity", "com.android.calculator2.Calculator");
     return capabilities;
 }


}

