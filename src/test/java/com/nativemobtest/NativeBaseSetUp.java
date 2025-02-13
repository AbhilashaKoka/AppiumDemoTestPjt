package com.nativemobtest;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.net.MalformedURLException;
import java.net.URL;


public class NativeBaseSetUp {
     AndroidDriver<MobileElement> driver ;

    @BeforeSuite
    public void setup()
    {
         try {
            DesiredCapabilities capabilities = getDesiredCapabilities();
            driver = new AndroidDriver<>(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
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



@AfterSuite
    public void tearDown(){
      driver.quit();
}

}

