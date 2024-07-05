package com.vdq.autogpm.util;

import com.sun.tools.javac.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SeleniumUtils {

    /**
     * Tìm một phần tử bằng XPath.
     *
     * @param driver Đối tượng WebDriver
     * @param xpath  XPath của phần tử cần tìm
     * @return WebElement tìm được
     */
    public static WebElement findElementByXPath(WebDriver driver, String xpath) {
        return driver.findElement(By.xpath(xpath));
    }

    /**
     * Tìm nhiều phần tử bằng XPath.
     *
     * @param driver Đối tượng WebDriver
     * @param xpath  XPath của các phần tử cần tìm
     * @return Danh sách các WebElement tìm được
     */
    public static List<WebElement> findElementsByXPath(WebDriver driver, String xpath) {
        return driver.findElements(By.xpath(xpath));
    }

    /**
     * Tạo XPath động từ một phần cơ bản và một phần động.
     *
     * @param baseXPath   Phần XPath cơ bản
     * @param dynamicPart Phần động của XPath
     * @return XPath hoàn chỉnh
     */
    public static String constructXPath(String baseXPath, String dynamicPart) {
        return String.format(baseXPath, dynamicPart);
    }

    /**
     * Tìm một phần tử trong Shadow DOM bằng JavaScript.
     *
     * @param driver Đối tượng WebDriver
     * @param script Mã JavaScript để truy cập Shadow DOM
     * @return WebElement tìm được trong Shadow DOM
     */
    public static WebElement findElementInShadowDOM(WebDriver driver, String script) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (WebElement) js.executeScript(script);
    }

    /**
     * Thực thi một đoạn mã JavaScript và trả về kết quả.
     *
     * @param driver Đối tượng WebDriver
     * @param script Mã JavaScript cần thực thi
     * @return Kết quả của việc thực thi mã JavaScript
     */
    public static Object executeScript(WebDriver driver, String script) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script);
    }

    /**
     * Mở một tab mới.
     *
     * @param driver Đối tượng WebDriver
     */
    public static void openNewTab(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.open();");
    }

    /**
     * Chuyển đổi giữa các tab.
     *
     * @param driver   Đối tượng WebDriver
     * @param tabIndex Chỉ số của tab muốn chuyển đến (bắt đầu từ 0)
     */
    public static void switchToTab(WebDriver driver, int tabIndex) {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabIndex));
    }

    /**
     * Lấy danh sách các tab hiện tại.
     *
     * @param driver Đối tượng WebDriver
     * @return Danh sách các handle của các tab
     */
    public static List<String> getAllTabs(WebDriver driver) {
        return new ArrayList<>(driver.getWindowHandles());
    }

    /**
     * Chuyển vào một iframe.
     *
     * @param driver        Đối tượng WebDriver
     * @param iframeElement WebElement của iframe
     */
    public static void switchToIframe(WebDriver driver, WebElement iframeElement) {
        driver.switchTo().frame(iframeElement);
    }

    /**
     * Chuyển ra khỏi iframe về nội dung chính.
     *
     * @param driver Đối tượng WebDriver
     */
    public static void switchToDefaultContent(WebDriver driver) {
        driver.switchTo().defaultContent();
    }

    /**
     * Kiểm tra sự tồn tại của một phần tử bằng XPath.
     *
     * @param driver Đối tượng WebDriver
     * @param xpath  XPath của phần tử cần kiểm tra
     * @return true nếu phần tử tồn tại, ngược lại false
     */
    public static boolean isElementPresent(WebDriver driver, String xpath) {
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        return !elements.isEmpty();
    }

    /**
     * Tìm một phần tử trong Shadow DOM bằng JavaScript.
     *
     * @param driver                Đối tượng WebDriver
     * @param shadowHostSelector    CSS selector của phần tử chứa Shadow DOM (Shadow host)
     * @param shadowElementSelector CSS selector của phần tử bên trong Shadow DOM cần tìm
     * @return WebElement tìm được trong Shadow DOM
     */
    public static WebElement findElementInShadowDOM(WebDriver driver, String shadowHostSelector, String shadowElementSelector) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "return document.querySelector(\"" + shadowHostSelector + "\").shadowRoot.querySelector(\"" + shadowElementSelector + "\");";
        return (WebElement) js.executeScript(script);
    }

    /**
     * Kiểm tra xem một phần tử trong Shadow DOM có tồn tại không.
     *
     * @param driver                Đối tượng WebDriver
     * @param shadowHostSelector    CSS selector của phần tử chứa Shadow DOM (Shadow host)
     * @param shadowElementSelector CSS selector của phần tử bên trong Shadow DOM cần kiểm tra
     * @return true nếu phần tử tồn tại, ngược lại false
     */
    public static boolean isElementInShadowDOMPresent(WebDriver driver, String shadowHostSelector, String shadowElementSelector) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "return document.querySelector(\"" + shadowHostSelector + "\").shadowRoot.querySelector(\"" + shadowElementSelector + "\") !== null;";
        return (Boolean) js.executeScript(script);
    }

    /**
     * Tải lại trang web hiện tại.
     *
     * @param driver Đối tượng WebDriver
     */
    public static void reloadPage(WebDriver driver) {
        driver.navigate().refresh();
    }


    public static String getProperty() {
        Properties properties = new Properties();

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return "";
            }

            // Load a properties file from class path, inside static method
            properties.load(input);

            // Get the property value and print it out
            String encryptedPassword = properties.getProperty("db.password");
            return encryptedPassword;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
