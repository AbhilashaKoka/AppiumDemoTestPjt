package org.example.utility.locatorUtility;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class MobileElementLocatorExample {

    public static void main(String[] args) {
        // Set up Desired Capabilities and AppiumDriver (example)
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("app", "/path/to/your/app.apk");

        WebDriver driver = new AppiumDriver<>(capabilities);

        try {
            // Locate elements using the utility class
            MobileElement loginButton = (MobileElement) driver.findElement(
                MobileElementLocatorUtility.byAccessibilityId("loginButton")
            );
            MobileElement usernameField = (MobileElement) driver.findElement(
                MobileElementLocatorUtility.byResourceId("com.example:id/username")
            );
            MobileElement submitButton = (MobileElement) driver.findElement(
                MobileElementLocatorUtility.byAndroidText("Submit")
            );

            // Perform actions
            usernameField.sendKeys("testuser");
            loginButton.click();

            System.out.println("Test completed successfully!");
        } finally {
            // Quit the driver
            driver.quit();
        }
    }
}