package demo.com;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

import static demo.com.AppiumTest.*;


public class Demo {
    public static void main(String[] args) {
        try {
            startServer();
            appiumStartup();
            appiumSrcFetch();
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
            sessionTerminate();
            stopServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}