package demo.com;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import static io.appium.java_client.remote.AndroidMobileCapabilityType.APP_ACTIVITY;
import static io.appium.java_client.remote.AndroidMobileCapabilityType.APP_PACKAGE;

public class TimeoutDemo {
    public static void main(String[] args) throws Exception {
        DesiredCapabilities caps = getDesiredCapabilities();
        AppiumDriver driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);

        System.out.println("Session started. Waiting for 35 seconds...");
        Thread.sleep(35000); // Wait longer than the timeout

        //  This command will fail if session expired
        try {
            driver.getPageSource();
        } catch (Exception e) {
            System.out.println("Session expired due to inactivity: " + e.getMessage());
        }

        driver.quit();
    }

    private static DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        caps.setCapability(APP_PACKAGE, "com.google.android.calculator");
        caps.setCapability(APP_ACTIVITY, "com.android.calculator2.Calculator");

        // ⏱ Set timeout to 30 seconds
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 30);
        return caps;
    }
}