package com.WindowAppTest;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CalculatorTest extends DesktopBaseSetUp {


    @Test
    public void Addition()
    {
        CalculatorSession.findElement(AppiumBy.name("One")).click();
        CalculatorSession.findElement(AppiumBy.name("Plus")).click();
        CalculatorSession.findElement(AppiumBy.name("Seven")).click();
        CalculatorSession.findElement(AppiumBy.name("Equals")).click();
        Assert.assertEquals("8", _GetCalculatorResultText());
    }

    @Test
    public void Combination()
    {
        CalculatorSession.findElement(AppiumBy.name("Seven")).click();
        CalculatorSession.findElement(AppiumBy.name("Multiply by")).click();
        CalculatorSession.findElement(AppiumBy.name("Nine")).click();
        CalculatorSession.findElement(AppiumBy.name("Plus")).click();
        CalculatorSession.findElement(AppiumBy.name("One")).click();
        CalculatorSession.findElement(AppiumBy.name("Equals")).click();
        CalculatorSession.findElement(AppiumBy.name("Divide by")).click();
        CalculatorSession.findElement(AppiumBy.name("Eight")).click();
        CalculatorSession.findElement(AppiumBy.name("Equals")).click();
        Assert.assertEquals("8", _GetCalculatorResultText());
    }

    @Test
    public void Division()
    {
        CalculatorSession.findElement(AppiumBy.name("Eight")).click();
        CalculatorSession.findElement(AppiumBy.name("Eight")).click();
        CalculatorSession.findElement(AppiumBy.name("Divide by")).click();
        CalculatorSession.findElement(AppiumBy.name("One")).click();
        CalculatorSession.findElement(AppiumBy.name("One")).click();
        CalculatorSession.findElement(AppiumBy.name("Equals")).click();
        Assert.assertEquals("8", _GetCalculatorResultText());
    }

    @Test
    public void Multiplication()
    {
        CalculatorSession.findElement(AppiumBy.name("Nine")).click();
        CalculatorSession.findElement(AppiumBy.name("Multiply by")).click();
        CalculatorSession.findElement(AppiumBy.name("Nine")).click();
        CalculatorSession.findElement(AppiumBy.name("Equals")).click();
        Assert.assertEquals("81", _GetCalculatorResultText());
    }

    @Test
    public void Subtraction()
    {
        CalculatorSession.findElement(AppiumBy.name("Nine")).click();
        CalculatorSession.findElement(AppiumBy.name("Minus")).click();
        CalculatorSession.findElement(AppiumBy.name("One")).click();
        CalculatorSession.findElement(AppiumBy.name("Equals")).click();
        Assert.assertEquals("8", _GetCalculatorResultText());
    }


}
