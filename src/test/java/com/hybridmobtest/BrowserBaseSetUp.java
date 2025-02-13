package com.hybridmobtest;
import java.io.IOException;
import java.util.Random;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class BrowserBaseSetUp {
	 static AndroidDriver<MobileElement> driver;
	 static AppiumDriverLocalService service=null;
	Random ran=new Random();


@BeforeSuite
	public AndroidDriver<MobileElement> getMobileDriver()
	{
		try {
			if(driver == null) driver = createAndroidDriver();
		}
		catch(Exception ex) {
			ex.printStackTrace();

		}
		return driver;
	}


	private AndroidDriver<MobileElement> createAndroidDriver()
	{

		DesiredCapabilities capabilities = setCapabilitiesForAndroidNativeapp();
		try {
			if (service.isRunning() == true) {
				execKill(1L);
				service.start();
			}
			driver = new AndroidDriver<>(service, capabilities);
		}
		catch (Exception ex)
		{
			ex.getStackTrace();
		}
		return driver;
	}


	private DesiredCapabilities setCapabilitiesForAndroidNativeapp() {
		try {
			service=AppiumDriverLocalService.buildDefaultService();
			DesiredCapabilities cap=new DesiredCapabilities();
			cap.setCapability("deviceName", "emulator-5554");
			cap.setCapability("platformName", "Android");
			cap.setCapability("platformVersion", "15");
			cap.setCapability("language" ,"en");
			cap.setCapability("locale" ,"IN");
			cap.setCapability("nativeWebScreenshot", true);
			cap.setCapability("chromedriverUseSystemExecutable", true);
			cap.setCapability("autoGrantPermissions", true);
			cap.setCapability("autoLaunch","true");
			cap.setCapability ("appPackage","com.google.android.calculator");
			cap.setCapability("appActivity","com.android.calculator2.Calculator");
			cap.setCapability("automationName","UIAutomator2");
			cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 100);
			cap.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);
			cap.setCapability(MobileCapabilityType.FORCE_MJSONWP, true);
			cap.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
			cap.setCapability(AndroidMobileCapabilityType.NO_SIGN, true);
			cap.setCapability(AndroidMobileCapabilityType.SYSTEM_PORT, ran.nextInt(99)+8200);
			return cap;
		}
		catch(Exception ex)
		{

			throw new IllegalArgumentException("Browser isn't supported.");
		}
	}


	private DesiredCapabilities setCapabilitiesForAndroid()
	{
		try {
			service=AppiumDriverLocalService.buildDefaultService();
			DesiredCapabilities cap=new DesiredCapabilities();
		    cap.setCapability("platformName", "Android");
			cap.setCapability("platformVersion", "10");
			cap.setCapability("deviceName","Moto g(7)");
			cap.setCapability("udid", "ZF6224BG9B");
			cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 100);
			cap.setCapability("browserName", "CHROME");
			cap.setCapability("appium:ChromeOptions", ImmutableMap.of("w3c",false));
			ChromeOptions chromeOptions = new ChromeOptions();
		//	chromeOptions.setExperimentalOption("w3c", false);
			cap.merge(cap);
			return cap;
		}
		catch(Exception ex)
		{

			throw new IllegalArgumentException("Browser isn't supported.");
		}

	}


@AfterSuite
	public void closeAndroidDriver()
	{

		try {
			if(service.isRunning()==true)
			{
				execKill(1L);
				service.stop();
			}
		}
		catch(Exception ex)
		{

			ex.getStackTrace();
		}
	}


public static void execKill(long minutes) throws InterruptedException{
		try{
			Thread.sleep(minutes*60L*1000L);
			Runtime.getRuntime().exec(" cmd /c TASKKILL /F /IM node.exe");
		}
		catch(IOException io)
		{
			io.printStackTrace();
		}
	}
}