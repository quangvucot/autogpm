package com.vdq.autogpm.util;

import com.sun.tools.javac.Main;
import com.vdq.autogpm.controller.ProfileController;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class SeleniumUtils {
    private static final Logger logger = Logger.getLogger(SeleniumUtils.class.getName());
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
                logger.info("Sorry, unable to find config.properties");
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

    public static void waitForClickabilityAndClick(WebDriver driver, String xpath, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

            // Cuộn phần tử vào trong tầm nhìn nếu cần thiết
            scrollElementIntoView(driver,element);

            // Click vào phần tử
            element.click();
        } catch (Exception e) {
            System.out.println("Element not clickable within the timeout period: " + xpath);
            e.printStackTrace();
        }
    }

    public static void waitForClickabilityAndClickInPopup(WebDriver driver, WebElement element, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            wait.until(ExpectedConditions.elementToBeClickable(element));

            // Cuộn phần tử vào trong tầm nhìn nếu cần thiết
            scrollElementIntoView(driver,element);
            Thread.sleep(1000);
            // Click vào phần tử
            element.click();
        } catch (Exception e) {
            System.out.println("Element not clickable within the timeout period");
            e.printStackTrace();
        }
    }

    private static boolean isElementInViewport(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (Boolean) js.executeScript(
                "var rect = arguments[0].getBoundingClientRect();" +
                        "return (" +
                        "rect.top >= 0 && " +
                        "rect.left >= 0 && " +
                        "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
                        "rect.right <= (window.innerWidth || document.documentElement.clientWidth)" +
                        ");", element);
    }
    private static void scrollElementIntoView(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);

        // Kiểm tra lại xem phần tử đã nằm trong viewport chưa, nếu chưa thì thử cuộn thêm một lần nữa
        if (!isElementInViewport(driver,element)) {
            js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);
        }
    }

    public static void zoomOut(WebDriver driver) {
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.SUBTRACT).keyUp(Keys.CONTROL).perform();
    }

}
