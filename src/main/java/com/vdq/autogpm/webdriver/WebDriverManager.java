package com.vdq.autogpm.webdriver;

import com.vdq.autogpm.api.Profile;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class WebDriverManager {

    public WebDriver initializeDriver(Profile profile) {
        System.out.println("Path " + profile.getDriver_path());
        ChromeDriverService service = new ChromeDriverService.Builder().usingDriverExecutable(new File(profile.getDriver_path())).usingAnyFreePort()
                .build();
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", profile.getRemote_debugging_address());
        try {
            service.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ChromeDriver(service,options);
    }
}
