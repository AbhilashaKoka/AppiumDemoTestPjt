package com.nativemobtest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class NativeBaseSetUp {
     AndroidDriver<MobileElement> driver ;
    // AppiumDriver<MobileElement> driver;
     static AppiumDriverLocalService service = null;


    @BeforeSuite
    public AndroidDriver<MobileElement> getMobileDriver() throws  MalformedURLException {
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
        startServer();
        try {
            DesiredCapabilities capabilities = getDesiredCapabilities();
            driver = new AndroidDriver<>(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }  return driver;
    }





 private static DesiredCapabilities getDesiredCapabilities() {
  DesiredCapabilities capabilities = new DesiredCapabilities();
     capabilities.setCapability("deviceName", "emulator-5554");
     capabilities.setCapability("platformName", "Android");
     capabilities.setCapability("platformVersion", "15");
     capabilities.setCapability("appPackage", "com.google.android.calculator");
     capabilities.setCapability("appActivity", "com.android.calculator2.Calculator");
     capabilities.setCapability("noReset","true");
     return capabilities;
 }





    public static void execKill(long minutes) throws InterruptedException {
        try {
            Thread.sleep(minutes * 60L * 1000L);
            Runtime.getRuntime().exec("cmd /c TASKKILL /F /IM node.exe");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

   // "C:\ApplicationPath\appium\appium.cmd"

    public void startServer() {
        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("C:\\ApplicationPath\\Node\\node.exe"))
                .withAppiumJS(new File("C:\\ApplicationPath\\appium\\node_modules\\appium"))
                .withIPAddress("0.0.0.0")
                .usingPort(4723)
                .withLogFile(new File("AppiumLog.txt"))
                .withArgument(GeneralServerFlag.LOCAL_TIMEZONE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "WARN")
        );
            service.start();
            System.out.println("Appium server started successfully.");
            // Your test code here

    }

    public void stopServer() {
        if (service != null) {
            service.stop();
        }
    }

}

