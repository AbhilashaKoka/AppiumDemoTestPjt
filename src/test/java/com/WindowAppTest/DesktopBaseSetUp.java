package com.WindowAppTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.*;
import java.net.URL;
import java.time.Duration;

public class DesktopBaseSetUp {

   WindowsDriver CalculatorSession = null;
   WebElement CalculatorResult = null;

//WAD(WinApp driver required) required

    @BeforeSuite
    public void setup() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "Microsoft.WindowsCalculator_8wekyb3d8bbwe!App");
            CalculatorSession = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
            CalculatorSession.manage().timeouts().implicitlyWait(Duration.ofDays(100));
            CalculatorResult = CalculatorSession.findElement(AppiumBy.accessibilityId("CalculatorResults"));
            Assert.assertNotNull(CalculatorResult);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @BeforeSuite
    public void Clear()
    {
        CalculatorSession.findElement(AppiumBy.name("Clear")).click();
        Assert.assertEquals("0", _GetCalculatorResultText());
    }

    @AfterSuite
    public  void TearDown()
    {
        CalculatorResult = null;
        if (CalculatorSession != null) {
            CalculatorSession.quit();
        }
        CalculatorSession = null;
    }

    protected String _GetCalculatorResultText()
    {
        return CalculatorResult.getText().replace("Display is", "").trim();
    }

}