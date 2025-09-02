package com.appiumGridTest;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;
import java.io.IOException;
import java.net.URL;


public class GridTest extends GridLauncher2 {

    @BeforeTest
    public void test() throws IOException, InterruptedException {
    /*   startSeleniumHub();
        startAppiumServer();
        startSeleniumNode();*/

    launchProcesses();
     killExistingJavaProcesses();
    }


    @Test
        public void test2() throws Exception {
//        UiAutomator2Options capabilites=new UiAutomator2Options();
//        capabilites.setPlatformName("android");
//        capabilites.setUdid("emulator-5554");
//        capabilites.setAutomationName("UIAutomator2");
//        capabilites.setPlatformVersion("10");
//
//        capabilites.setChromedriverExecutable("C:\\Users\\Abhilasha\\Documents\\DOCUMENT\\StudyDocumentFolder\\IDE\\APPIUMSetUp\\drivers\\chromedriver_74\\chromedriver.exe");
//        capabilites.setNoReset(true);
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "android");
        caps.setCapability("deviceName", "emulator-5554");
        caps.setCapability("automationName", "UIAutomator2");
        caps.setCapability("platformVersion", "14");
        caps.setCapability("browserName", "chrome");
        caps.setCapability("chromedriverExecutable", "C:\\Users\\Abhilasha\\Documents\\DOCUMENT\\StudyDocumentFolder\\IDE\\APPIUMSetUp\\drivers\\chromedriver_74\\chromedriver.exe");
        caps.setCapability("noReset", true);
        caps.setCapability("newCommandTimeout", 300);


        // Point to Selenium Grid hub, which routes to Appium node
        URL gridUrl = new URL("http://192.168.1.3:4444");
        AndroidDriver driver = new AndroidDriver(gridUrl, caps);

        System.out.println("Driver initialized successfully: " + driver.getCapabilities().getBrowserName());
        driver.get("https://www.google.com");
        driver.findElement(By.name("q")).sendKeys("Automation");
        driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
        driver.quit();
        }



        @AfterTest
         public void tearDown() throws IOException {
//            killExistingJavaProcesses();
            System.out.println("Test completed.");
    }


}




