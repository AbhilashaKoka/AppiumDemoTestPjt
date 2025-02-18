package com.hybridmobtest;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class BrowserBaseSetUp {
	AndroidDriver<MobileElement> driver;
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
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Abhilasha\\node_modules\\appium-chromedriver");
		DesiredCapabilities capabilities = setCapabilitiesForAndroid();
		driver = new AndroidDriver<>(new URL(service.getUrl().toString()), capabilities); // Use the correct URL format
		return driver;
	}

	private DesiredCapabilities setCapabilitiesForAndroid() {
		try {
			service = AppiumDriverLocalService.buildDefaultService();
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("deviceName", "emulator-5554");
			cap.setCapability("platformName", "Android");
			cap.setCapability("platformVersion", "15");
			cap.setCapability("automationName", "UIAutomator2");
			cap.setCapability("browserName", "CHROME");
			cap.setCapability("appium:ChromeOptions", ImmutableMap.of("w3c", false));
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setExperimentalOption("w3c", false);
			cap.merge(cap);
			return cap;
		} catch (Exception ex) {
			throw new IllegalArgumentException("Browser isn't supported.");
		}
	}

	public static void execKill(long minutes) throws InterruptedException {
		try {
			Thread.sleep(minutes * 60L * 1000L);
			Runtime.getRuntime().exec("cmd /c TASKKILL /F /IM node.exe");
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	public void startServer() {
		service = new AppiumServiceBuilder()
				.withArgument(() -> "--use-drivers", "uiautomator2,chromedriver")
				.withArgument(() -> "--use-plugins", "execute-driver") // Custom argument for plugins
//				.usingAnyFreePort()
				.build();
		service.start();
	}

	public void stopServer() {
		if (service != null) {
			service.stop();
		}
	}
}
