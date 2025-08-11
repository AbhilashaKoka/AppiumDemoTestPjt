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

import java.io.File;
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
		startServer("local");
		DesiredCapabilities capabilities = setCapabilitiesForAndroid();
		driver = new AndroidDriver<>(new URL(service.getUrl().toString()), capabilities); // Use the correct URL format
		return driver;
	}


    private DesiredCapabilities setCapabilitiesForAndroid() {
        try {
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("deviceName", "emulator-5554");
            cap.setCapability("platformName", "Android");
            cap.setCapability("platformVersion", "10");
            cap.setCapability("automationName", "UIAutomator2");
            cap.setCapability("browserName", "Chromium");
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setExperimentalOption("w3c", false);
            cap.setCapability("appium:chromeOptions", chromeOptions);
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


    public void startServer(String serviceName) {
        switch (serviceName) {
            case "grid":
                service = new AppiumServiceBuilder()
                .withIPAddress("0.0.0.0")
                .usingPort(4723)
                .withArgument(() -> "--use-drivers", "uiautomator2,chromium")
                .withArgument(() -> "--use-plugins", "execute-driver")
                .build();
                break;
            case "local":
                service = new AppiumServiceBuilder()
                        .usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
                    .withAppiumJS(new File("C:\\Users\\Abhilasha\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
                    .withIPAddress("127.0.0.1")
                    .usingPort(4723)
                    .withArgument(() -> "--use-drivers", "uiautomator2,chromium")
                    .withArgument(() -> "--use-plugins", "execute-driver")
                    .withLogFile(new File("AppiumLog.txt"))
                    .build();
                service.start();
            // Add cases for other services if needed
            default:
                throw new IllegalArgumentException("Unsupported service: " + serviceName);
        }


    }

	public void stopServer() {
		if (service != null) {
			service.stop();
		}
	}


}
