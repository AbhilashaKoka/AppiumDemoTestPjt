package com.hybridmobtest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;



public class BrowserTest extends BrowserBaseSetUp {


	@Test
	     public void searchgooglepage() {
	       driver.get("https://www.google.com");
	       driver.findElement(By.name("q")).sendKeys("Automation");
		   driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
		   System.out.println("Test Completed...........");
	}


	      @Test
	       public void loginTest() {
		    driver.get("https://opensource-demo.orangehrmlive.com/");
		    driver.findElement(By.xpath("//div[@id='divUsername']/span")).click();
		    driver.findElement(By.id("txtUsername")).clear();
		    driver.findElement(By.id("txtUsername")).sendKeys("Admin");
		    driver.findElement(By.id("txtPassword")).clear();
		    driver.findElement(By.id("txtPassword")).sendKeys("admin123");
		    driver.findElement(By.id("btnLogin")).click();


	}
}
