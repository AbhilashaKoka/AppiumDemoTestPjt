package org.example.utility.locatorUtility;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebElementLocatorExample {

    public static void main(String[] args) {
        // Set up Desired Capabilities and AppiumDriver (example)
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("app", "/path/to/your/app.apk");

        WebDriver driver = new AppiumDriver(capabilities);

        try {
            // Locate elements using the utility class
            WebElement loginButton = (WebElement) driver.findElement(
                AppiumBy.accessibilityId("loginButton")
            );
            WebElement usernameField = (WebElement) driver.findElement(
                    AppiumBy.id("com.example:id/username")
            );
            WebElement submitButton = (WebElement) driver.findElement(
                    AppiumBy.id("Submit")
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