package com.vdq.autogpm.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private WebDriver driver;
    private WebDriverWait wait;

    public WaitUtils(WebDriver driver, long timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    // Chờ một phần tử có the click được
    public WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForUrlContains(String fraction) {
        wait.until(ExpectedConditions.urlContains(fraction));
    }

    public boolean isElementVisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
        // Chờ một phần tử nào đó đến khi tìm thấy
    public boolean waitForElementVisible(By locator, long timeoutInSeconds) {
        try {
            WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            localWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Chờ đợi trong mấy giây
    public void sleepSecond(long miliSecond) {
        try {
            Thread.sleep(miliSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Phương thức tiện ích để gửi ký tự chậm hơn
    public void sendKeysSlowly(WebElement element, String text, long delayInMillis) {
        for (char ch : text.toCharArray()) {
            element.sendKeys(String.valueOf(ch));
            try {
                Thread.sleep(delayInMillis);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }
}
