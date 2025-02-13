package com.hybridmobtest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import io.appium.java_client.service.local.AppiumServiceBuilder;



public class BrowserBaseSetUp {
	  AndroidDriver<MobileElement> driver;
	 static AppiumDriverLocalService service=null;
	 Random ran=new Random();




@BeforeSuite
	public AndroidDriver<MobileElement> getMobileDriver() throws MalformedURLException {
		driver = createAndroidDriver();
		return driver;
	}



	@AfterSuite
	public void closeAndroidDriver(){
	stopServer();
	}



	private AndroidDriver<MobileElement> createAndroidDriver() throws MalformedURLException {
		startServer();
			DesiredCapabilities capabilities = setCapabilitiesForAndroid();
			driver = new AndroidDriver<>(service.getUrl(), capabilities);
		return driver;
	}


	private DesiredCapabilities setCapabilitiesForAndroid() {
		try {
			service=AppiumDriverLocalService.buildDefaultService();
			DesiredCapabilities cap=new DesiredCapabilities();
			cap.setCapability("deviceName", "emulator-5554");
			cap.setCapability("platformName", "Android");
			cap.setCapability("platformVersion", "15");
			cap.setCapability("automationName","UIAutomator2");
			cap.setCapability("browserName", "CHROME");
			cap.setCapability("appium:ChromeOptions", ImmutableMap.of("w3c",false));
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setExperimentalOption("w3c", false);
			cap.merge(cap);
			return cap;
		}
		catch(Exception ex)
		{

			throw new IllegalArgumentException("Browser isn't supported.");
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


		public void startServer() {
			service = new AppiumServiceBuilder().usingAnyFreePort().build();
			service.start();
		}

		public void stopServer() {
			if (service != null) {
				service.stop();
			}
		}



	}

