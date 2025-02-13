package com.gridTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.*;

import io.appium.java_client.AppiumDriver;



public class GridTest {
    public static void main(String[] args) {
//    @Test
//    public void test(){
        System.out.println("Hello!!");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "10");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"Moto g(7)");
        capabilities.setCapability(MobileCapabilityType.UDID, "ZF6224BG9B");
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
        try {
            AndroidDriver<MobileElement> driver = new AndroidDriver<>(new URL("http://"+getLocalHostAddress()+":"+getPort()+"/wd/hub"), capabilities);
            driver.get("https://www.google.com");
            System.out.println("Page Title: " + driver.getTitle());
            driver.quit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static String  getLocalHostAddress(){
        String str=null;
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            System.out.println("Local IP Address: " + localAddress.getHostAddress());
            str= localAddress.getHostAddress();

        } catch (UnknownHostException e) {
            System.err.println("Could not get IP address: " + e.getMessage());
        }
        return str;
    }


    public static int getPort(){
        int port = 0;
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            // Get the port number
            port = serverSocket.getLocalPort();
            // Print the  port number
            System.out.println("Local Port: " + port);
        } catch (IOException e) {
            System.err.println("Could not get port: " + e.getMessage());
        }
        return port;
    }

}




