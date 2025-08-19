package com.hybridmobtest;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.BASEPATH;

public class BrowserBaseSetUp {

    static AppiumDriverLocalService service = null;


    @BeforeMethod
    public AndroidDriver getMobileDriver() throws  MalformedURLException {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress("127.0.0.1"); // Or use .usingAnyFreePort()
        builder.usingPort(4723); // Or use .usingAnyFreePort()
        builder.withAppiumJS(new File("C:\\Users\\Abhilasha\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js")); // Replace with your Appium path
        builder.withArgument(BASEPATH, "/wd/hub"); // Standard base path
        // Add other server arguments as needed, e.g., builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        service = AppiumDriverLocalService.buildService(builder);
        if(!service.isRunning()) {
            service.start();
            System.out.println("Appium server started at: " + service.getUrl());
        }
        DesiredCapabilities capabilities = setCapabilitiesForAndroid();
        AndroidDriver driver = new AndroidDriver(new URL(service.getUrl().toString()), capabilities);
        System.out.println("initialised driver");
        return driver;
    }

    @AfterMethod
    public static void stopAppiumServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium server stopped.");
        }
    }


    private DesiredCapabilities setCapabilitiesForAndroid() {
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "Android");
            caps.setCapability("deviceName", "emulator-5554");
            caps.setCapability("automationName", "UIAutomator2");
            caps.setCapability("browserName", "Chrome");
            caps.setCapability("chromedriverExecutable", "C:\\ApplicationPath\\drivers\\chromedriver_74\\chromedriver.exe");
            caps.setCapability("noReset", true);
            return caps;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Browser isn't supported.");
        }
    }



}