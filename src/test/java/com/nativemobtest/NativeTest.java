package com.nativemobtest;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.testng.annotations.Test;


public class  NativeTest extends NativeBaseSetUp {

    @Test
    public void test1(){
        MobileElement two=driver.findElement(By.id("com.google.android.calculator:id/digit_2"));
        MobileElement add=driver.findElement(By.id("com.google.android.calculator:id/op_add"));
        MobileElement five=driver.findElement(By.id("com.google.android.calculator:id/digit_5"));
        MobileElement equal=driver.findElement(By.id("com.google.android.calculator:id/eq"));
        MobileElement space=driver.findElement(By.className("android.widget.TextView"));
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
            MobileElement digit2 = driver.findElementById("com.android.calculator2:id/digit_2");
            digit2.click();
            MobileElement plus = driver.findElementById("com.android.calculator2:id/op_add");
            plus.click();
            MobileElement digit3 = driver.findElementById("com.android.calculator2:id/digit_3");
            digit3.click();
            MobileElement equals = driver.findElementById("com.android.calculator2:id/eq");
            equals.click();
            MobileElement result = driver.findElementById("com.android.calculator2:id/result");
            System.out.println("Result: " + result.getText());
            }
    }
}
