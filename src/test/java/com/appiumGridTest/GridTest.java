package com.appiumGridTest;
import org.testng.annotations.Test;
import java.io.IOException;


public class GridTest extends GridLauncher {


@Test
    public void test2() throws IOException, InterruptedException {
        startSeleniumHub();
        waitForSelenium();
        startAppiumServer();
        waitForAppium();
        registerAppiumNode(nodeConfigPath);
        killExistingJavaProcesses();
    }


}




