package com.appiumGridTest;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.IOException;
import java.net.URL;


public class GridTest extends GridLauncher {

    @BeforeMethod
    public void test() throws IOException, InterruptedException {
        startSeleniumHub();
        getLocalHostAddress();
        getPort();
        waitForSelenium();
        startAppiumServer();
        waitForAppium();
        registerAppiumNode(nodeConfigPath);
    }

    @Test
        public void test2() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "emulator-5554");
        caps.setCapability("automationName", "UIAutomator2");
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("chromedriverExecutable", "C:\\Users\\Abhilasha\\Documents\\DOCUMENT\\StudyDocumentFolder\\IDE\\APPIUMSetUp\\drivers\\chromedriver_74\\chromedriver.exe");
        caps.setCapability("noReset", true);
        // Point to Selenium Grid hub, which routes to Appium node
        URL gridUrl = new URL("http://192.168.1.3:4444/wd/hub");

        AndroidDriver driver = new AndroidDriver(gridUrl, caps);
       // Your test logic here
        System.out.println("Driver initialized successfully: " + driver.getCapabilities().getBrowserName());
        driver.get("https://www.google.com");
        driver.findElement(By.name("q")).sendKeys("Automation");
        driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
        driver.quit();
        }



        @AfterMethod
         public void tearDown() throws IOException {
            killExistingJavaProcesses();
    }


}




