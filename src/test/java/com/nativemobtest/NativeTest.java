package com.nativemobtest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;


public class  NativeTest extends NativeBaseSetUp {

    @Test
    public void testLogin() {
        WebElement usernameField = driver.findElement(AppiumBy.accessibilityId("username"));
        WebElement passwordField = driver.findElement(AppiumBy.accessibilityId("password"));
        WebElement loginButton   = driver.findElement(AppiumBy.accessibilityId("login"));

        usernameField.sendKeys("testuser");
        passwordField.sendKeys("password123");
        loginButton.click();
    }

    @Test
    public void test1(){
        WebElement two=driver.findElement(AppiumBy.id("com.google.android.calculator:id/digit_2"));
        WebElement add=driver.findElement(AppiumBy.id("com.google.android.calculator:id/op_add"));
        WebElement five=driver.findElement(AppiumBy.id("com.google.android.calculator:id/digit_5"));
        WebElement equal=driver.findElement(AppiumBy.id("com.google.android.calculator:id/eq"));
        WebElement space=driver.findElement(AppiumBy.className("android.widget.TextView"));
        two.click();
        add.click();
        five.click();
        equal.click();
        String result=space.getText();
        System.out.println("Number sum result is : " + result);

  }

    @Test
    public void Sum(){
        driver.findElements(By.xpath("//android.widget.Button")).get(0).click();
        driver.findElement(By.name("2")).click();
        driver.findElement(By.name("+")).click();
        driver.findElement(By.name("5")).click();
        driver.findElement(By.name("=")).click();
        String result = driver.findElement(By.className("android.widget.EditText")).getText();
        System.out.println("Number sum result is : " + result);

    }


    @Test
    public void Test4() {
        if (driver != null) {
            WebElement digit2 = driver.findElement(AppiumBy.id("com.android.calculator2:id/digit_2"));
            digit2.click();
            WebElement plus = driver.findElement(AppiumBy.id("com.android.calculator2:id/op_add"));
            plus.click();
            WebElement digit3 = driver.findElement(AppiumBy.id("com.android.calculator2:id/digit_3"));
            digit3.click();
            WebElement equals = driver.findElement(AppiumBy.id("com.android.calculator2:id/eq"));
            equals.click();
            WebElement result = driver.findElement(AppiumBy.id("com.android.calculator2:id/result"));
            System.out.println("Result: " + result.getText());
            }
    }
}
