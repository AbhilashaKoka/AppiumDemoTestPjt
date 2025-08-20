package com.gridTest;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import java.io.IOException;


public class GridTest extends BaseSetUp {
    private static final long serialVersionUID = 1L;
    AndroidDriver driver;

    public GridTest() throws IOException, InterruptedException {
          driver = (AndroidDriver) setup("hybrid");
    }

    @Test
    public void searchgooglepage() {
        driver.get("https://www.google.com");
        driver.findElement(By.name("q")).sendKeys("Automation");
        driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
        System.out.println("Test Completed...........");
    }


}




