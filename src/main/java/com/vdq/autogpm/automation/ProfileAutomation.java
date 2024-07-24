package com.vdq.autogpm.automation;

import com.vdq.autogpm.api.Profile;
import com.vdq.autogpm.service.ProfileService;
import com.vdq.autogpm.util.LoggerUtil;
import com.vdq.autogpm.util.SeleniumUtils;
import com.vdq.autogpm.util.Utils;
import com.vdq.autogpm.util.WaitUtils;
import com.vdq.autogpm.webdriver.WebDriverManager;
import okhttp3.internal.Util;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ProfileAutomation {

    private static Map<String, List<String>> coinSwaps = new LinkedHashMap<>();

    String urlHomeBeraChain = "https://bartio.bex.berachain.com/";
    //    XPath mật khẩu OKX
    private final String okxPasswordXpath = "//input[@type='password' or @id='password']";
    // Mật khẩu OKX (Chưa mã hóa)
    private final String yourPassowrd = "";
    // Xpath nút kết nối ví
    private final String connectWalletButtonXpath = "//button[@data-testid='ConnectButton']";
    // Xpath Trạng thái kết  nối ví lỗi
    private final String wroingConnectWalletButtonXpath = "//button[text()='Wrong network']";
    // Xpath Shadow Popup
    private final String shadowHostSelector = "div[data-testid='dynamic-modal-shadow']";
    // Xpath địa chỉ của ví OKX
    private final String buttonAdressWalletXpath = "(//nav//button[@aria-haspopup='dialog'])[1]";
    // Xpath của các coin trong danh sách
    private final String shadowElementSelector = "button[data-testid='ListTile']";
    //Xpath chọn kết nối ví Bera Chain
    private final String selectBeraChainNetwork = "button[data-testid='SelectNetworkButton']";
    // Xpath Nút chọn chức năng Swap Token
    private final String swapTokensXpath = "//button[contains(text(),'Swap Token')]";
    // Xpath Coin nhận
    private final String coinNameReceiverXpath = "(//button[contains(@class, 'items-center') and contains(@class, 'transition-duration-300')]//span[contains(@class,'truncate')])[2]";
    // Xpath Coin chuyển
    private final String coinNameTranferXpath = "(//button[contains(@class, 'items-center') and contains(@class, 'transition-duration-300')]//span[contains(@class,'truncate')])[1]";

    // Xpath Coin Name
    private final String listCoinNameXpath = "//div[@role='dialog']//div[contains(@class,'max-h-')]//button//div[@class='flex flex-col']//span";
    // Xpath nhập số tiền chuyển
    private final String priceFromXpath = "(//input[@step='any' and @min])[1]";
    // Xpath nút Preview
    private final String previewButtonXpath = "//div[@class='w-full']//button[contains(text(),'Preview')]";
    // Xpath Nút Wrap
    private final String swapButtonXpath = "//button[text()='Wrap']";
    // Xpath Dialog Waiting xác nhận chuyển tiền
    private final String dialogWattingWalletXpath = "//div[@role='dialog' and @data-state='open']";
    // Xpath xác nhận chuyển tiền bn
    private final String buttonVerifyOkxXpath = "(//div[@class='_action-buttons_j3bvq_1']//button)[2]";
    // Xpath Dialog Trang thai chuyen tien
    private final String buttonStatusTransaction = "(//div[@role='dialog']//button)[2]";
    // Xpath Lay Trang thai chuyen tien
    private final String statusTransaction = "//div[@role='dialog' and @data-state='open']//p";
    // Xpath Icon trang thai chuyen tien
    private final String iconStatusTrans = "//div[@role='dialog']//div//img";
    private final String xpathAddLiquidity = "//div[@class='w-full']//button[contains(text(),'Add Liquidity')]";

    //XPATH
    private final String buttonMintXpath = "//div[@class='w-full']//button[@role='tab' and @data-state and text()='Mint']";
    private final String buttonRedeemXpath = "//div[@class='w-full']//button[@role='tab' and @data-state and text()='Redeem']";
    private final String buttonApproveCoinXpath = "(//button[contains(text(), 'Approve')])[1]";
    private final String buttonApproveInfiniteXpath = "(//button[contains(text(), 'Approve')])[2]";
    private final String buttonMintHoneyXpath = "(//div//button[text()='Mint'])[2]";

    private final String listCoinTopXpath = "//div[@role='dialog']//div[contains(@class,'flex-wrap')]//div";
    private final String buttonPreviewButtonXpath = "//div[@class='w-full']//button[@type='button' and contains(text(),'Preview')]";
    private final String buttonSwapXpath = "//button[text()='Swap']";
    private final String closeButtonXpath = "//div[@role='dialog']//button[@type='button']";
    private final String changeSwapCoinXpath = "//div[@class='relative']//button";

    // XPath khong du so du
    private final String alertAmountExceeds = "//div[@role='alert']//h5[text()='Error']";
    private final String buttonUnwrapXpath = "//button[text()='Unwrap']";
    private final String rountNotFoundXpath = "//div[@role='alert']//h5[text()='Route Not Found']";

    public ProfileAutomation(Map<String, List<String>> coinSwaps) {
        this.coinSwaps = coinSwaps;
    }


    public void runAutomation(WebDriver driver, String pass, String id) {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WaitUtils waitUtils = new WaitUtils(driver, 10);
        try {
            goToChromeExtention(driver, waitUtils);
            interactOKX(driver, waitUtils, pass);
            loginWalletWithBerachain(driver, waitUtils);
            swapTokenAction(driver, waitUtils);
            performSwaps(driver, waitUtils, 0);
        } catch (NoSuchElementException e) {
            LoggerUtil.log("Khong tim thay Element " + e.getMessage());
            System.out.println("Khong tim thay Element " + e.getMessage());
        } finally {

            driver.quit();
            ProfileService profileService = new ProfileService();
            profileService.closeProfile(id);
        }
    }

    /*
        Hàm đăng nhập ví OKX
     */
    private void interactOKX(WebDriver driver, WaitUtils waitUtils, String passw) {
        waitUtils.sleepMillis(2000);
        waitUtils.waitForElementVisible(By.xpath(okxPasswordXpath), 100);
        WebElement okxInputElm = SeleniumUtils.findElementByXPath(driver, okxPasswordXpath);
        waitUtils.sleepMillis(2000);
        waitUtils.sendKeysSlowly(okxInputElm, passw, 400);
        waitUtils.sleepMillis(2000);
        SeleniumUtils.findElementByXPath(driver, okxPasswordXpath).sendKeys(Keys.ENTER);
        waitUtils.sleepMillis(2000);
    }

    private void performSwaps(WebDriver driver, WaitUtils waitUtils, int type) {
        for (Map.Entry<String, List<String>> entry : coinSwaps.entrySet()) {
            String fromCoin = entry.getKey();
            System.out.println("From Coin " + fromCoin);
            LoggerUtil.log("From Coin " + fromCoin);
            List<String> toCoins = entry.getValue();
            System.out.println("To Coin " + toCoins);
            LoggerUtil.log("To Coin " + toCoins);
            if (fromCoin.equals("BERA")) {
                swapCoins(driver, fromCoin, toCoins, "0.3", 1, waitUtils, type);
            } else {
                swapCoins(driver, fromCoin, toCoins, "1", 1, waitUtils, type);
            }
        }
    }

    /*
    Login OKX Wallet With Bera Chain
    */
    private void loginWalletWithBerachain(WebDriver driver, WaitUtils waitUtils) {
        SeleniumUtils.openNewTab(driver);
        SeleniumUtils.switchToTab(driver, 1);
        waitUtils.sleepMillis(1000);
        driver.get(urlHomeBeraChain);
        String currentUrl = driver.getCurrentUrl();
        waitUtils.sleepMillis(3000);
        if (currentUrl.equals("about:blank")) {
            driver.get(urlHomeBeraChain);
            waitUtils.sleepMillis(3000);
        } else {
            System.out.println("Đã truy cập URL: " + currentUrl);
            LoggerUtil.log("Đã truy cập URL: " + currentUrl);
        }
        waitUtils.waitForElementVisible(By.xpath(buttonAdressWalletXpath), 5);

        if (SeleniumUtils.isElementPresent(driver, connectWalletButtonXpath)) {
            WebElement connectWalletButtonElm = SeleniumUtils.findElementByXPath(driver, connectWalletButtonXpath);
            connectWalletButtonElm.click();
            System.out.println("Click into Connect Wallet");
            LoggerUtil.log("Click into Connect Wallet");
            waitUtils.sleepMillis(5000);
            if (SeleniumUtils.isElementInShadowDOMPresent(driver, shadowHostSelector, shadowElementSelector)) {
                System.out.println("Co Dialog Shadow Root");
                LoggerUtil.log("Co Dialog Shadow Root");
                WebElement shadowElement = SeleniumUtils.findElementInShadowDOM(driver, shadowHostSelector, shadowElementSelector);
                waitUtils.sleepMillis(2000);
                shadowElement.click();
                waitUtils.sleepMillis(5000);
                if (SeleniumUtils.isElementInShadowDOMPresent(driver, shadowHostSelector, selectBeraChainNetwork)) {
                    WebElement beraChainNetwork = SeleniumUtils.findElementInShadowDOM(driver, shadowHostSelector, selectBeraChainNetwork);
                    waitUtils.sleepMillis(1000);
                    beraChainNetwork.click();
                }
            } else {
                System.out.println("Hien tai da dang nhap thanh cong");
                LoggerUtil.log("Hien tai da dang nhap thanh cong");
                waitUtils.sleepMillis(5000);
                if (SeleniumUtils.isElementInShadowDOMPresent(driver, shadowHostSelector, shadowElementSelector)) {
                    System.out.println("Co Dialog Shadow Root");
                    LoggerUtil.log("Co Dialog Shadow Root");
                    WebElement shadowElement = SeleniumUtils.findElementInShadowDOM(driver, shadowHostSelector, shadowElementSelector);
                    shadowElement.click();
                    waitUtils.sleepMillis(2000);
                    if (SeleniumUtils.isElementInShadowDOMPresent(driver, shadowHostSelector, selectBeraChainNetwork)) {
                        WebElement beraChainNetwork = SeleniumUtils.findElementInShadowDOM(driver, shadowHostSelector, selectBeraChainNetwork);
                        waitUtils.sleepMillis(1000);
                        beraChainNetwork.click();
                    }
                }
            }
        } else if (SeleniumUtils.isElementPresent(driver, wroingConnectWalletButtonXpath)) {
            System.out.println("Click into Wrong Connect");
            LoggerUtil.log("Click into Wrong Connect");
            SeleniumUtils.waitForClickabilityAndClick(driver, wroingConnectWalletButtonXpath, 20);
        }
    }

    /*
      Đi vào trang swap
     */
    private void swapTokenAction(WebDriver driver, WaitUtils waitUtils) {
//        WebElement connectWalletButtonElm = SeleniumUtils.findElementByXPath(driver, swapTokensXpath);
//        connectWalletButtonElm.click();
        SeleniumUtils.findElementByXPath(driver, swapTokensXpath).click();
        waitUtils.sleepMillis(5000);
        SeleniumUtils.zoomOut(driver);
    }

    private void mintCoin(WebDriver driver, String coinName, String price, int numSwaps, WaitUtils waitUtils) {
        driver.get("https://bartio.honey.berachain.com");
        waitUtils.sleepMillis(3000);
        waitUtils.waitForVisibility(By.xpath(coinNameReceiverXpath));
        WebElement fromElm = SeleniumUtils.findElementByXPath(driver, coinNameReceiverXpath);
        fromElm.click();
        waitUtils.sleepMillis(4000);
        List<WebElement> webElementList = SeleniumUtils.findElementsByXPath(driver, listCoinNameXpath);
        for (WebElement webElement : webElementList) {
            System.out.println("WebElement " + webElement.getText());
            if (webElement.getText().equals(coinName)) {
                webElement.click();
                WebElement priceFromElm = SeleniumUtils.findElementByXPath(driver, priceFromXpath);
                waitUtils.sleepMillis(2000);
                priceFromElm.sendKeys(price);
                waitUtils.sleepMillis(3000);
                waitUtils.waitForElementVisible(By.xpath(buttonApproveCoinXpath), 20);
                if (SeleniumUtils.isElementPresent(driver, buttonApproveCoinXpath)) {
                    SeleniumUtils.findElementByXPath(driver, buttonApproveCoinXpath).click();
                    waitUtils.sleepMillis(3000);
                    System.out.println("Chuyển sang Tab OKX để xác nhận Approve Mint Coin");
                    SeleniumUtils.switchToTab(driver, 0);
                    waitUtils.sleepMillis(3000);
                    driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                    waitUtils.sleepMillis(3000);
                    SeleniumUtils.waitForClickabilityAndClick(driver, buttonVerifyOkxXpath, 30);

                    // Kiểm tra xem có hiển thị nút xác nhận bên ví OKX không?
                    if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                        System.out.println("Có nút xác nhận Approve Coin bên OKX");
                        waitUtils.sleepMillis(5000);
                        SeleniumUtils.waitForClickabilityAndClick(driver, buttonVerifyOkxXpath, 30);
                        System.out.println("Đã click vào nút xác nhận Approve Coin");
                        waitUtils.sleepMillis(5000);
                        System.out.println("Chuyển sang tab thứ nhất để MINT");
                        SeleniumUtils.switchToTab(driver, 1);
                        waitUtils.sleepMillis(3000);
                        waitUtils.waitForElementVisible(By.xpath(buttonMintHoneyXpath), 20);
                        if (SeleniumUtils.isElementPresent(driver, buttonMintHoneyXpath)) {
//                            SeleniumUtils.waitForClickabilityAndClick(driver,buttonMintHoneyXpath, 30);
//                            waitUtils.waitForClickability(By.xpath(buttonMintHoneyXpath));
                            System.out.println("Click vào MINT ");
                            SeleniumUtils.waitForClickabilityAndClick(driver, buttonMintHoneyXpath, 30);
                            waitUtils.sleepMillis(3000);
                            System.out.println("Chuyển sang Tab OKX để xác nhận MINT");
                            SeleniumUtils.switchToTab(driver, 0);
                            waitUtils.sleepMillis(3000);
                            driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                            waitUtils.sleepMillis(3000);
                            waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);
                            if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                                System.out.println("Có nút xác nhận MINT bên OKX");
                                waitUtils.sleepMillis(5000);
                                waitUtils.waitForClickability(By.xpath(buttonVerifyOkxXpath));
                                SeleniumUtils.waitForClickabilityAndClick(driver, buttonVerifyOkxXpath, 20);
                                System.out.println("Đã click vào nút xác nhận MINT");
                                waitUtils.sleepMillis(5000);
                                System.out.println("Chuyển sang tab thứ nhất xem trạng thái MINT");
                                SeleniumUtils.switchToTab(driver, 1);

                            }
                        }
                    }
                } else if (SeleniumUtils.isElementPresent(driver, buttonMintHoneyXpath)) {
                    waitUtils.waitForClickability(By.xpath(buttonMintHoneyXpath));
                    SeleniumUtils.waitForClickabilityAndClick(driver, buttonMintHoneyXpath, 20);
                    waitUtils.sleepMillis(5000);
                    System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                    SeleniumUtils.switchToTab(driver, 0);
                    waitUtils.sleepMillis(3000);
                    driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                    waitUtils.sleepMillis(3000);
                    waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);
                    if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                        System.out.println("Có nút xác nhận chuyển tiền bên OKX");
                        waitUtils.sleepMillis(5000);
                        waitUtils.waitForClickability(By.xpath(buttonVerifyOkxXpath));
                        SeleniumUtils.waitForClickabilityAndClick(driver, buttonVerifyOkxXpath, 20);
                        System.out.println("Đã click vào nút xác nhận chuyển tiền");
                        waitUtils.sleepMillis(5000);
                        System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                        SeleniumUtils.switchToTab(driver, 1);
                        waitUtils.sleepMillis(10000);
                    }
                }

//                REDEEM
                SeleniumUtils.findElementByXPath(driver, buttonRedeemXpath).click();
                WebElement priceFromElmt = SeleniumUtils.findElementByXPath(driver, priceFromXpath);
                waitUtils.sleepMillis(2000);
                priceFromElmt.sendKeys(price);
                waitUtils.sleepMillis(3000);
                waitUtils.waitForElementVisible(By.xpath(buttonApproveCoinXpath), 20);
                if (SeleniumUtils.isElementPresent(driver, buttonApproveCoinXpath)) {
                    SeleniumUtils.findElementByXPath(driver, buttonApproveCoinXpath).click();
                    waitUtils.sleepMillis(3000);
                    System.out.println("Chuyển sang Tab OKX để xác nhận chuyển tiền");
                    SeleniumUtils.switchToTab(driver, 0);
                    waitUtils.sleepMillis(3000);
                    driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                    waitUtils.sleepMillis(3000);
                    waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);

                    // Kiểm tra xem có hiển thị nút xác nhận bên ví OKX không?
                    if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                        System.out.println("Có nút xác nhận chuyển tiền bên OKX");
                        waitUtils.sleepMillis(5000);
                        SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                        System.out.println("Đã click vào nút xác nhận chuyển tiền");
                        waitUtils.sleepMillis(5000);
                        System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                        SeleniumUtils.switchToTab(driver, 1);
                        waitUtils.sleepMillis(3000);
                        waitUtils.waitForElementVisible(By.xpath(buttonMintHoneyXpath), 20);
                        if (SeleniumUtils.isElementPresent(driver, buttonMintHoneyXpath)) {
                            waitUtils.waitForClickability(By.xpath(buttonMintHoneyXpath));
                            SeleniumUtils.findElementByXPath(driver, buttonMintHoneyXpath).click();
                            waitUtils.sleepMillis(3000);
                            System.out.println("Chuyển sang Tab OKX để xác nhận chuyển tiền");
                            SeleniumUtils.switchToTab(driver, 0);
                            waitUtils.sleepMillis(3000);
                            driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                            waitUtils.sleepMillis(3000);
                            waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);
                            if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                                System.out.println("Có nút xác nhận chuyển tiền bên OKX");
                                waitUtils.sleepMillis(5000);
                                SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                                System.out.println("Đã click vào nút xác nhận chuyển tiền");
                                waitUtils.sleepMillis(5000);
                                System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                                SeleniumUtils.switchToTab(driver, 1);

                            }
                        }
                    }
                } else if (SeleniumUtils.isElementPresent(driver, buttonMintHoneyXpath)) {
                    SeleniumUtils.findElementByXPath(driver, buttonMintHoneyXpath).click();
                    waitUtils.sleepMillis(5000);
                    System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                    SeleniumUtils.switchToTab(driver, 0);
                    waitUtils.sleepMillis(3000);
                    driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                    waitUtils.sleepMillis(3000);
                    waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);
                    if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                        System.out.println("Có nút xác nhận chuyển tiền bên OKX");
                        waitUtils.sleepMillis(5000);
                        SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                        System.out.println("Đã click vào nút xác nhận chuyển tiền");
                        waitUtils.sleepMillis(5000);
                        System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                        SeleniumUtils.switchToTab(driver, 1);
                        waitUtils.sleepMillis(10000);
                    }
                }
            }

        }
    }

    private void goToChromeExtention(WebDriver driver, WaitUtils waitUtils) {
        try {
            driver.get("chrome://extensions");
            waitUtils.sleepMillis(2000);
            String idExtention = getIdOfExtention(driver);

            if (idExtention.isEmpty()) {
                System.out.println("Lay lai id extention mot lan nua");
                LoggerUtil.log("Lay lai id extention mot lan nua");
                driver.get("chrome://extensions");
                waitUtils.sleepMillis(1000);
                idExtention = getIdOfExtention(driver);
                if (idExtention.isEmpty()) {
                    idExtention = "mcohilncbfahbmgdjkbpemcciiolgcge";
                }
            }
            String urlChrome = "chrome-extension://" + idExtention + "/home.html#onboarding/welcome";
            driver.get(urlChrome);
            waitUtils.sleepMillis(2000);

        } catch (Exception e) {
            System.out.println("Co loi xay ra o ");
            LoggerUtil.log("Co loi xay ra o " + e.getMessage());
        }

    }

    private String getIdOfExtention(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "var shadowRoot = document.querySelector('extensions-manager').shadowRoot;" + "shadowRoot = shadowRoot.querySelector('#viewManager');" + "shadowRoot = shadowRoot.querySelector('#items-list').shadowRoot;" + "var listItems = shadowRoot.querySelectorAll('extensions-item');" + "let extId = '';" + "listItems.forEach((item) => {" + "    const itemShadow = item.shadowRoot;" + "    const name = itemShadow.getElementById('name').innerText;" + "    const id = item.getAttribute('id');" + "    if (name.includes('OKX')) {" + "        extId = id;" + "    }" + "});" + "return extId;";
        return (String) js.executeScript(script);
    }

    /*  ---------------------------------------------------------------------------------------*/
    public void swapCoins(WebDriver driver, String fromCoin, List<String> toCoins, String price, int numSwaps, WaitUtils waitUtils, int type) {
        List<String> randomToCoins = getRandomCoins(toCoins);
        for (String toCoin : randomToCoins) {
            String pricetranf = "";
            if (fromCoin.equalsIgnoreCase("BERA")) {
                pricetranf = String.valueOf(Utils.randomValue(0.1, 0.2));
                System.out.println(" BERA pricetranf " + pricetranf);
                LoggerUtil.log(" BERA pricetranf " + pricetranf);
            } else if (fromCoin.equalsIgnoreCase("HONEY")) {
                pricetranf = String.valueOf(Utils.randomValue(0.5, 0.9));
                System.out.println("HONEY pricetranf " + pricetranf);
                LoggerUtil.log("HONEY pricetranf " + pricetranf);
            }
            if (!fromCoin.equals(toCoin)) {
                for (int i = 0; i < numSwaps; i++) {
                    System.out.println("Bắt đầu chuyển từ " + fromCoin + " sang " + toCoin);
                    swapCoin(driver, fromCoin, toCoin, pricetranf, waitUtils, type);
                }
            } else {
                System.out.println("Không thể chuyển từ " + fromCoin + " sang chính nó.");
                LoggerUtil.log("Không thể chuyển từ " + fromCoin + " sang chính nó.");
            }
        }
    }

    private void swapCoin(WebDriver driver, String fromCoin, String toCoin, String price, WaitUtils waitUtils, int type) {
        // Lấy ra tên coin hiện tại
        String receiverCoinName = getCurrentCoin(driver, coinNameReceiverXpath);
        String tranferCoinName = getCurrentCoin(driver, coinNameTranferXpath);
        System.out.println("Tên coin chuyển hiện tại: " + tranferCoinName);
        LoggerUtil.log("Tên coin chuyển hiện tại: " + tranferCoinName);
        System.out.println("Tên coin nhận hiện tại: " + receiverCoinName);
        LoggerUtil.log("Tên coin nhận hiện tại: " + receiverCoinName);
        System.out.println("Coin nhận muốn chọn: " + toCoin);
        LoggerUtil.log("Coin nhận muốn chọn: " + toCoin);
        System.out.println("Coin chuyển muốn chọn: " + fromCoin);
        LoggerUtil.log("Coin chuyển muốn chọn: " + fromCoin);
        waitUtils.sleepMillis(2000);

        // Giá trị chuyển và nhận không trùng nhau
        if (receiverCoinName.equalsIgnoreCase(tranferCoinName)) {
            System.out.println("Kiểm tra lại. Giá trị giữa coin nhận và chuyển giống nhau");
            LoggerUtil.log("Kiểm tra lại. Giá trị giữa coin nhận và chuyển giống nhau");
            return;
        }

        // Kiểm tra nếu coin chuyển (A) trùng với giá trị coin nhận hiện tại (CB):
        if (fromCoin.equalsIgnoreCase(receiverCoinName) || toCoin.equalsIgnoreCase(tranferCoinName)) {
            System.out.println("Coin chuyển (A) trùng với giá trị coin nhận hiện tại (CB) hoặc coin nhận (B) trùng với giá trị coin chuyển hiện tại (CA):");
            LoggerUtil.log("Coin chuyển (A) trùng với giá trị coin nhận hiện tại (CB) hoặc coin nhận (B) trùng với giá trị coin chuyển hiện tại (CA):");
            System.out.println("Click Reverse");
            LoggerUtil.log("Click Reverse");
            reverseCoin(driver);
            receiverCoinName = getCurrentCoin(driver, coinNameReceiverXpath);
            tranferCoinName = getCurrentCoin(driver, coinNameTranferXpath);
            System.out.println("Đã click Reverse, giá trị mới của coin chuyển: " + tranferCoinName);
            LoggerUtil.log("Đã click Reverse, giá trị mới của coin chuyển: " + tranferCoinName);
            System.out.println("Đã click Reverse, giá trị mới của coin nhận: " + receiverCoinName);
            LoggerUtil.log("Đã click Reverse, giá trị mới của coin nhận: " + receiverCoinName);
        }
        // Kiểm tra nếu giá trị coin chuyển hiện tại bằng với coin chuyển mong muốn
        if (tranferCoinName.equalsIgnoreCase(fromCoin)) {
            System.out.println("Giá trị coin chuyển hiện tại bằng coin chuyển mong muốn");
            LoggerUtil.log("Giá trị coin chuyển hiện tại bằng coin chuyển mong muốn");
            if (receiverCoinName.equalsIgnoreCase(toCoin)) {
                System.out.println("Giá trị coin nhận hiện tại bằng coin nhận mong muốn");
                LoggerUtil.log("Giá trị coin nhận hiện tại bằng coin nhận mong muốn");
                enterPrice(driver, price, waitUtils, type != 0, fromCoin, toCoin);
                if (checkNotEnoughtMoney(driver)) {
                    approveTransaction(driver, buttonSwapXpath, waitUtils);
                    checkTransactionStatus(driver, waitUtils);
                }
                return;
            } else {
                System.out.println("Bắt đầu chọn lại coin nhận");
                LoggerUtil.log("Bắt đầu chọn lại coin nhận");
                selectCoin(driver, toCoin, coinNameReceiverXpath, waitUtils);
                enterPrice(driver, price, waitUtils, type != 0, fromCoin, toCoin);
                if (checkNotEnoughtMoney(driver)) {
                    approveTransaction(driver, buttonSwapXpath, waitUtils);
                    checkTransactionStatus(driver, waitUtils);
                }
                return;
            }
        } else {
            System.out.println("Bắt đầu chọn lại coin chuyển");
            LoggerUtil.log("Bắt đầu chọn lại coin chuyển");
            selectCoin(driver, fromCoin, coinNameTranferXpath, waitUtils);
            if (!receiverCoinName.equalsIgnoreCase(toCoin)) {
                System.out.println("Bắt đầu chọn lại coin nhận");
                LoggerUtil.log("Bắt đầu chọn lại coin nhận");
                selectCoin(driver, toCoin, coinNameReceiverXpath, waitUtils);
            }
            enterPrice(driver, price, waitUtils, type != 0, fromCoin, toCoin);
            if (checkNotEnoughtMoney(driver)) {
                approveTransaction(driver, buttonSwapXpath, waitUtils);
                checkTransactionStatus(driver, waitUtils);
            }
            return;
        }
    }


//    private void swapCoin(WebDriver driver, String fromCoin, String toCoin, String price, WaitUtils waitUtils) {
//        // Lấy ra tên coin hiện tại
//        String receiverCoinName = getCurrentCoin(driver, coinNameReceiverXpath);
//        String tranferCoinName = getCurrentCoin(driver, coinNameTranferXpath);
//        System.out.println("Tên coin chuyển Hien tai: " + tranferCoinName);
//        System.out.println("Tên coin nhận hiện taại: " + receiverCoinName);
//        System.out.println("To chuyen tiep theo" + toCoin);
//        waitUtils.sleepMillis(2000);
//        //Giá trị chuyển và nhận không trùng nhau
//        if (receiverCoinName.equalsIgnoreCase(toCoin) || tranferCoinName.equalsIgnoreCase(fromCoin)) {
//            System.out.println("Kiểm tra lại. Giá trị giữa coin nhận và chuyển giống nhau");
//            return;
//        } else if (fromCoin.equalsIgnoreCase(receiverCoinName)) {
//            //  Kiểm tra nếu coin chuyển (A) trùng với giá trị coin nhận hiện tại (CB):
//            System.out.println("Kiểm tra nếu coin chuyển (A) trùng với giá trị coin nhận hiện tại (CB):");
//            System.out.println("Click Reverse ne");
//            String rcv = getCurrentCoin(driver, coinNameReceiverXpath);
//            if (rcv.equalsIgnoreCase(toCoin)) {
//                System.out.println("Gia tri coin nhan hien tai = Coin nhan sap chon");
//            } else {
//                System.out.println("Bat dau chon lai Coin B");
//            }
//        }else if(fromCoin.equalsIgnoreCase(tranferCoinName)){
//            System.out.println("Gia tri coin chuyen hien tai bang coin chuyen");
//            String rcv = getCurrentCoin(driver, coinNameReceiverXpath);
//            if (rcv.equalsIgnoreCase(toCoin)) {
//                System.out.println("Gia tri coin nhan hien tai = Coin nhan sap chon");
//            }else{
//                System.out.println("Bat dau chon lai Coin nhan");
//            }
//        }else if(toCoin.equalsIgnoreCase(tranferCoinName)){
//            System.out.println("Coin nhan muon chon bang gia tri coin chuyen hien tai");
//            System.out.println("Click Reverse ne");
//            String getTF = getCurrentCoin(driver, coinNameTranferXpath);
//            if (fromCoin.equalsIgnoreCase(getTF)){
//                System.out.println("Hien tai gia tri con chuyen = gia tri coin chuyen muon chon => Chon gia thoi");
//            }else{
//                System.out.println("Bat dau chon lai coin chuyen");
//            }
//        }


    // Coin nhan trung voi coin gui
//        if (tranferCoinName.equalsIgnoreCase(fromCoin)) {
//            System.out.println("Hiện tại coin chuyển trùng với coin chẩn bị chọn -> Chỉ cần chọn coin nhân thôi ");
//            if (!receiverCoinName.equalsIgnoreCase(toCoin)) {
//                System.out.println("Tien hanh chon coin");
//                System.out.println("Day la To Coin " + toCoin);
//                selectCoin(driver, toCoin, coinNameReceiverXpath, waitUtils);
//            }
//            enterPrice(driver, price, waitUtils);
//            if (checkNotEnoughtMoney(driver)) {
//                approveTransaction(driver, buttonSwapXpath, waitUtils);
//                checkTransactionStatus(driver, waitUtils);
//            }
//
//        } else if (tranferCoinName.equalsIgnoreCase(toCoin)) {
//            System.out.println("Coin chuan bị chon trung voi coin chuyen => Swap lai thoi");
//            WebElement fromElm = SeleniumUtils.findElementByXPath(driver, changeSwapCoinXpath);
//            waitUtils.waitForClickability(By.xpath(changeSwapCoinXpath));
////            fromElm.click();
//            SeleniumUtils.waitForClickabilityAndClick(driver, changeSwapCoinXpath, 10);
//            enterPrice(driver, price, waitUtils);
//            if (checkNotEnoughtMoney(driver)) {
//                approveTransaction(driver, buttonSwapXpath, waitUtils);
//                checkTransactionStatus(driver, waitUtils);
//            }
//        } else {
//            System.out.println("Chon coin binh thuong thoi");
//            selectCoin(driver, toCoin, coinNameReceiverXpath, waitUtils);
//            selectCoin(driver, fromCoin, coinNameTranferXpath, waitUtils);
//            enterPrice(driver, price, waitUtils);
//            if (checkNotEnoughtMoney(driver)) {
//                approveTransaction(driver, buttonSwapXpath, waitUtils);
//                checkTransactionStatus(driver, waitUtils);
//            }
//        }
//    }

    private String getCurrentCoin(WebDriver driver, String xpathCoin) {
        WebElement fromElm;
        fromElm = SeleniumUtils.findElementByXPath(driver, xpathCoin);
        return fromElm.getText();
    }

    private boolean selectCoin(WebDriver driver, String coinName, String xpathCoinName, WaitUtils waitUtils) {
        System.out.println("Click vao nut chon coin");
        LoggerUtil.log("Click vao nut chon coin");
        try {

            SeleniumUtils.waitForClickabilityAndClick(driver, xpathCoinName, 10);
//            WebElement fromElm = SeleniumUtils.findElementByXPath(driver, xpathCoinName);
//            waitUtils.waitForClickability(By.xpath(xpathCoinName));
//            fromElm.click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("Khong the Click vao nut chon coin nen Reload ");
            LoggerUtil.log("Khong the Click vao nut chon coin nen Reload ");
            SeleniumUtils.reloadPage(driver);
            SeleniumUtils.waitForClickabilityAndClick(driver, xpathCoinName, 10);


        }
        waitUtils.sleepMillis(3000);

        List<WebElement> webElementList = SeleniumUtils.findElementsByXPath(driver, listCoinTopXpath);
        for (WebElement webElement : webElementList) {
            if (webElement.getText().equalsIgnoreCase(coinName)) {
                try {
                    System.out.println("webElement.getText(): " + webElement.getText());
                    LoggerUtil.log("webElement.getText(): " + webElement.getText());
                    waitUtils.sleepMillis(1000);
//                    SeleniumUtils.waitForClickabilityAndClick(driver, webElement, 10);
                    webElement.click();
                    return true; // Trả về true nếu chọn đồng coin thành công
                } catch (TimeoutException e) {
                    System.out.println("Element is not clickable within the timeout period: " + e.getMessage());
                    LoggerUtil.log("Element is not clickable within the timeout period: " + e.getMessage());
                } catch (ElementNotInteractableException e) {
                    System.out.println("Element is not interactable: " + e.getMessage());
                    LoggerUtil.log("Element is not interactable: " + e.getMessage());
                }
            }
        }
        return false; // Trả về false nếu không tìm thấy đồng coin
    }

    private void enterPrice(WebDriver driver, String price, WaitUtils waitUtils, boolean isMax, String fromCoin, String toCoin) {
        if (isMax) {
            if (toCoin.equalsIgnoreCase("BERA") && fromCoin.equalsIgnoreCase("HONEY")) {
                final String xpathCurrentValueOfBera = "//ul//li[1]//span[contains(@class,'text-muted-foreground')]//span/span[1]";
                String valueHoney = SeleniumUtils.findElementByXPath(driver, xpathCurrentValueOfBera).getText();
                if (Float.parseFloat(valueHoney) >= 15) {
                    float divideValue = (Float.parseFloat(valueHoney) / 2) + 2;
                    priceValue(driver, String.valueOf(divideValue), waitUtils);
                } else {
                    System.out.println("Amount of Honey  is too small.");
                    LoggerUtil.log("Amount of Honey  is too small.");
                    return;
                }
            } else {
                final String maxValue = "//span[@class='cursor-pointer']//span";
                final String xpathCurrentValueOfBera = "//ul//li[1]//span[contains(@class,'text-muted-foreground')]//span/span[1]";
                if (SeleniumUtils.isElementPresent(driver, xpathCurrentValueOfBera)) {
                    String valueBera = SeleniumUtils.findElementByXPath(driver, xpathCurrentValueOfBera).getText();
                    waitUtils.sleepMillis(3000);
                    if (Float.parseFloat(valueBera) > 0) {
                        if (fromCoin.equalsIgnoreCase("BERA")) {
                            String beraValue = SeleniumUtils.findElementByXPath(driver, xpathCurrentValueOfBera).getText();
                            priceValue(driver, String.valueOf(Float.parseFloat(beraValue) - 0.01), waitUtils);
                            waitUtils.sleepMillis(2000);
                        } else {
                            SeleniumUtils.waitForClickabilityAndClick(driver, maxValue, 20);
                            waitUtils.sleepMillis(2000);
                        }

                    } else {
                        priceValue(driver, "1", waitUtils);

                    }
                } else {
                    priceValue(driver, "1", waitUtils);
                }
            }
        } else {
            try {
                priceValue(driver, price, waitUtils);
                waitUtils.sleepMillis(1000);
            } catch (ElementNotInteractableException e) {
                System.out.println("Co loi xay ra o price: " + e.getMessage());
                LoggerUtil.log("Co loi xay ra o price: " + e.getMessage());
                waitUtils.sleepMillis(2000);
                priceValue(driver, price, waitUtils);
            }
        }

    }

    private void priceValue(WebDriver driver, String price, WaitUtils waitUtils) {
        WebElement priceFromElm = SeleniumUtils.findElementByXPath(driver, priceFromXpath);
        waitUtils.sleepMillis(2000);
        System.out.println("Nhập giá chuyển: " + price);
        LoggerUtil.log("Nhập giá chuyển: " + price);
        String inputValue = priceFromElm.getAttribute("value");
        if (!inputValue.isEmpty()) {
            priceFromElm.sendKeys(Keys.CONTROL + "a"); // Bôi đen toàn bộ nội dung
            priceFromElm.sendKeys(Keys.DELETE); // Xóa nội dung
        }
        priceFromElm.sendKeys(price);
    }

    private void clickSwapButtons(WebDriver driver, WaitUtils waitUtils) {
        try {
            if (SeleniumUtils.isElementPresent(driver, previewButtonXpath)) {
                System.out.println("Có nút Preview " + Thread.currentThread().getName());
                LoggerUtil.log("Có nút Preview " + Thread.currentThread().getName());
                SeleniumUtils.waitForClickabilityAndClick(driver, previewButtonXpath, 10);
                waitUtils.sleepMillis(4000);
                if (SeleniumUtils.isElementPresent(driver, buttonSwapXpath)) {
                    System.out.println("Click swap");
                    LoggerUtil.log("Click swap");
                    SeleniumUtils.waitForClickabilityAndClick(driver, buttonSwapXpath, 10);
                } else if (SeleniumUtils.isElementPresent(driver, xpathAddLiquidity)) {
                    System.out.println("Click swap");
                    LoggerUtil.log("Click swap");
                    SeleniumUtils.waitForClickabilityAndClick(driver, xpathAddLiquidity, 10);
                }
                waitUtils.sleepMillis(3000);
            } else if (SeleniumUtils.isElementPresent(driver, swapButtonXpath)) {
                System.out.println("Có nút Swap");
                LoggerUtil.log("Có nút Swap");
                waitUtils.waitForClickability(By.xpath(swapButtonXpath));
//              SeleniumUtils.findElementByXPath(driver, swapButtonXpath).click();
                SeleniumUtils.waitForClickabilityAndClick(driver, swapButtonXpath, 10);
            }
            waitUtils.sleepMillis(5000);
        } catch (ElementClickInterceptedException e) {
            System.out.println("Khong the click duoc vao nut swap button");
            LoggerUtil.log("Khong the click duoc vao nut swap button");
        }

    }

    private void checkTransactionStatus(WebDriver driver, WaitUtils waitUtils) {
        waitUtils.waitForElementVisible(By.xpath(buttonStatusTransaction), 10);
        if (SeleniumUtils.isElementPresent(driver, buttonStatusTransaction)) {
            System.out.println("Có dialog trạng thái chuyển tiền");
            LoggerUtil.log("Có dialog trạng thái chuyển tiền");
            waitUtils.waitForElementVisible(By.xpath(iconStatusTrans), 20);
            String transValue = SeleniumUtils.findElementByXPath(driver, statusTransaction).getText();
            System.out.println("Trạng thái chuyển tiền: " + transValue);
            LoggerUtil.log("Trạng thái chuyển tiền: " + transValue);
            waitUtils.sleepMillis(3000);
            if (transValue.equalsIgnoreCase("Transaction pending") || transValue.equalsIgnoreCase("You pay")) {
                waitUtils.waitForElementVisible(By.xpath(iconStatusTrans), 20);
            }

            transValue = SeleniumUtils.findElementByXPath(driver, statusTransaction).getText();
            if (transValue.equalsIgnoreCase("Transaction Success")) {
                System.out.println("Chuyển thành công -> Đóng Popup");
                LoggerUtil.log("Chuyển thành công -> Đóng Popup");
                waitUtils.sleepMillis(3000);
                waitUtils.waitForClickability(By.xpath(buttonStatusTransaction));
//                SeleniumUtils.waitForClickabilityAndClick(driver, buttonStatusTransaction, 10);
                SeleniumUtils.findElementByXPath(driver, buttonStatusTransaction).click();
            } else if (transValue.toLowerCase().contains("error")) {
                System.out.println("Chuyển thất bại -> Đóng Popup");
                LoggerUtil.log("Chuyển thất bại -> Đóng Popup");
                waitUtils.sleepMillis(3000);

                if (SeleniumUtils.isElementPresent(driver, buttonStatusTransaction)) {
                    SeleniumUtils.waitForClickabilityAndClick(driver, buttonStatusTransaction, 10);
                } else {
                    SeleniumUtils.waitForClickabilityAndClick(driver, "//div[@role='dialog']//button", 10);
                }

            }
            waitUtils.sleepMillis(3000);
        }
    }

    private void approveTransaction(WebDriver driver, String xpathAction, WaitUtils waitUtils) {
//        waitUtils.waitForElementVisible(By.xpath(buttonApproveCoinXpath), 10);
        System.out.println("Bat dau vao Approve Transaction");
        LoggerUtil.log("Bat dau vao Approve Transaction");
        waitUtils.sleepMillis(2000);
        if (SeleniumUtils.isElementPresent(driver, buttonApproveCoinXpath)) {
            System.out.println("Co Approve");
//            SeleniumUtils.findElementByXPath(driver, buttonApproveCoinXpath).click();
            SeleniumUtils.waitForClickabilityAndClick(driver, buttonApproveCoinXpath, 10);
            waitUtils.sleepMillis(3000);
            System.out.println("Chuyển sang Tab OKX để xác nhận Approve Mint Coin");
            LoggerUtil.log("Chuyển sang Tab OKX để xác nhận Approve Mint Coin");
            SeleniumUtils.switchToTab(driver, 0);
            switchOKXToVerifyTrans(driver, waitUtils);
            if (SeleniumUtils.isElementPresent(driver, xpathAction)) {
                waitUtils.waitForClickability(By.xpath(xpathAction));
                System.out.println("Click vào MINT ");
                LoggerUtil.log("Click vào MINT ");
                waitUtils.waitForClickability(By.xpath(xpathAction));
//                SeleniumUtils.findElementByXPath(driver, xpathAction).click();
                SeleniumUtils.waitForClickabilityAndClick(driver, xpathAction, 10);
            } else if (SeleniumUtils.isElementPresent(driver, previewButtonXpath)) {
                System.out.println("Co Preview Button trong approve");
                LoggerUtil.log("Co Preview Button trong approve");
                clickSwapButtons(driver, waitUtils);
            }
            if (!checkSomethingwentwrong(driver, waitUtils)) {
                switchOKXToVerifyTrans(driver, waitUtils);
            }

        } else if (SeleniumUtils.isElementPresent(driver, buttonMintHoneyXpath)) {
            System.out.println("Co Button Mint");
            LoggerUtil.log("Co Button Mint");
            waitUtils.waitForClickability(By.xpath(buttonMintHoneyXpath));
//            SeleniumUtils.findElementByXPath(driver, buttonMintHoneyXpath).click();
            SeleniumUtils.waitForClickabilityAndClick(driver, buttonMintHoneyXpath, 10);
            switchOKXToVerifyTrans(driver, waitUtils);
        } else if (SeleniumUtils.isElementPresent(driver, previewButtonXpath)) {
            clickSwapButtons(driver, waitUtils);
            System.out.println("Co Preview Button");
            LoggerUtil.log("Co Preview Button");
            if (!checkSomethingwentwrong(driver, waitUtils)) {
                switchOKXToVerifyTrans(driver, waitUtils);
            }
        } else if (SeleniumUtils.isElementPresent(driver, buttonUnwrapXpath)) {
            waitUtils.waitForClickability(By.xpath(buttonUnwrapXpath));
//            SeleniumUtils.findElementByXPath(driver, buttonUnwrapXpath).click();
            SeleniumUtils.waitForClickabilityAndClick(driver, buttonUnwrapXpath, 10);
            System.out.println("Co UnWrap Button Button trong approve");
            LoggerUtil.log("Co UnWrap Button Button trong approve");
            if (!checkSomethingwentwrong(driver, waitUtils)) {
                switchOKXToVerifyTrans(driver, waitUtils);
            }
        } else if (SeleniumUtils.isElementPresent(driver, swapButtonXpath)) {
            System.out.println("Co Swap Button");
            LoggerUtil.log("Co Swap Button");
            waitUtils.waitForClickability(By.xpath(swapButtonXpath));
//            SeleniumUtils.findElementByXPath(driver, swapButtonXpath).click();
            SeleniumUtils.waitForClickabilityAndClick(driver, swapButtonXpath, 10);

            if (!checkSomethingwentwrong(driver, waitUtils)) {
                switchOKXToVerifyTrans(driver, waitUtils);
            }
        }
    }

    private Boolean checkSomethingwentwrong(WebDriver driver, WaitUtils waitUtils) {
        final String somethingWentWrongXpath = "//div[@role='dialog']//span[contains(text(),'Something went')]";
        if (SeleniumUtils.isElementPresent(driver, somethingWentWrongXpath)) {
            waitUtils.sleepMillis(2000);
            waitUtils.waitForClickability(By.xpath(closeButtonXpath));
//            SeleniumUtils.findElementByXPath(driver, closeButtonXpath).click();
            SeleniumUtils.waitForClickabilityAndClick(driver, closeButtonXpath, 10);
            return true;
        }
        return false;
    }

    private boolean checkNotEnoughtMoney(WebDriver driver) {

        if (SeleniumUtils.isElementPresent(driver, alertAmountExceeds)) {
            System.out.println("Hiện tại đồng coin không đủ tiền");
            LoggerUtil.log("Hiện tại đồng coin không đủ tiền");
            return false;
        } else if (SeleniumUtils.isElementPresent(driver, rountNotFoundXpath)) {
            System.out.println("Route Not Found");
            LoggerUtil.log("Route Not Found");
            return false;
        }
        return true;

    }

    private void switchOKXToVerifyTrans(WebDriver driver, WaitUtils waitUtils) {
        waitUtils.sleepMillis(3000);
        System.out.println("Chuyển sang Tab OKX để xác nhận MINT");
        LoggerUtil.log("Chuyển sang Tab OKX để xác nhận MINT");
        SeleniumUtils.switchToTab(driver, 0);
        waitUtils.sleepMillis(3000);
        if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
            waitUtils.waitForClickability(By.xpath(buttonVerifyOkxXpath));
            SeleniumUtils.waitForClickabilityAndClick(driver, buttonVerifyOkxXpath, 10);
        } else {
            driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
            waitUtils.sleepMillis(3000);
            waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);
            if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                System.out.println("Có nút xác nhận MINT bên OKX");
                LoggerUtil.log("Có nút xác nhận MINT bên OKX");
                waitUtils.sleepMillis(5000);
                waitUtils.waitForClickability(By.xpath(buttonVerifyOkxXpath));
                SeleniumUtils.waitForClickabilityAndClick(driver, buttonVerifyOkxXpath, 10);
                System.out.println("Đã click vào nút xác nhận MINT");
                LoggerUtil.log("Đã click vào nút xác nhận MINT");
                waitUtils.sleepMillis(5000);
                System.out.println("Chuyển sang tab thứ nhất xem trạng thái MINT");
                LoggerUtil.log("Chuyển sang tab thứ nhất xem trạng thái MINT");
            } else {
                SeleniumUtils.switchToTab(driver, 1);
                approveTransaction(driver, buttonSwapXpath, waitUtils);
            }
        }
        SeleniumUtils.switchToTab(driver, 1);
        waitUtils.sleepMillis(5000);
    }


    private List<String> getRandomCoins(List<String> toCoins) {
        List<String> shuffledToCoins = new ArrayList<>(toCoins);
        Collections.shuffle(shuffledToCoins, new Random());
        return shuffledToCoins;
    }

    private void reverseCoin(WebDriver driver) {
        SeleniumUtils.waitForClickabilityAndClick(driver, changeSwapCoinXpath, 20);
    }

    public void clameBGT(WebDriver driver, String pass, String id) {

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WaitUtils waitUtils = new WaitUtils(driver, 10);
        try {
            goToChromeExtention(driver, waitUtils);
            interactOKX(driver, waitUtils, pass);
            loginWalletWithBerachain(driver, waitUtils);
            gotoClameBgt(driver, waitUtils, 0);
        } catch (NoSuchElementException e) {
            System.out.println("Khong tim thay Element " + e.getMessage());
            LoggerUtil.log("Khong tim thay Element " + e.getMessage());
        } finally {
            driver.quit();
            ProfileService profileService = new ProfileService();
            profileService.closeProfile(id);
        }
    }

    private void gotoClameBgt(WebDriver driver, WaitUtils waitUtils, int type) {
        final String tablePool = "//table";
        final String poolHoneyBera = "//td//span[text()='HONEY-WBERA']";
        final String clameRewards = "//button[text()='Claim Rewards']";
        final String deposit = "//a[contains(@href,'https://bartio.station.berachain.com/gauge/')]";
        driver.get("https://bartio.bex.berachain.com/pools");
        waitUtils.sleepMillis(4000);
        waitUtils.waitForVisibility(By.xpath(tablePool));
        if (SeleniumUtils.isElementPresent(driver, tablePool)) {
            waitUtils.sleepMillis(5000);
            SeleniumUtils.waitForClickabilityAndClick(driver, poolHoneyBera, 20);
            waitUtils.sleepMillis(2000);
            switch (type) {
                case 0:
                    waitUtils.waitForVisibility(By.xpath(deposit));
                    SeleniumUtils.waitForClickabilityAndClick(driver, deposit, 20);
                    waitUtils.sleepMillis(4000);
                    Set<String> allHandles = driver.getWindowHandles();
                    List<String> handlesList = new ArrayList<>(allHandles);
                    int currentTabIndex = handlesList.size() - 1;
                    SeleniumUtils.switchToTab(driver, currentTabIndex);
                    // In ra số thứ tự của tab hiện tại
                    try {
                        waitUtils.sleepMillis(4000);
                        if (SeleniumUtils.isElementPresent(driver, clameRewards)) {
                            loginWalletWithBerachain(driver, waitUtils);
                            SeleniumUtils.waitForClickabilityAndClick(driver, clameRewards, 20);
                        } else {
                            int loop = -1;
                            for (String handle : driver.getWindowHandles()) {
                                driver.switchTo().window(handle);
                                String currentUrl = driver.getCurrentUrl();
                                loop += 1;
                                if (currentUrl.contains("https://bartio.station.berachain.com/gauge/")) {
                                    loginWalletWithBerachain(driver, waitUtils);
                                    SeleniumUtils.waitForClickabilityAndClick(driver, clameRewards, 20);
                                    currentTabIndex = loop;
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Khong tim thay duoc element" + e.getMessage());
                        for (String handle : driver.getWindowHandles()) {
                            driver.switchTo().window(handle);
                            String currentUrl = driver.getCurrentUrl();
                            if (currentUrl.contains("https://bartio.station.berachain.com/gauge/")) {
                                SeleniumUtils.waitForClickabilityAndClick(driver, clameRewards, 20);
                                break;
                            }
                        }
                    }
                    switchOKXToVerifyTransClame(driver, waitUtils, currentTabIndex);
                    waitUtils.sleepMillis(2000);
                    checkTransactionStatus(driver, waitUtils);
                    break;
                case 1:
                    waitUtils.sleepMillis(3000);
                    final String xpathAddPool = "//a[contains(@href,'/add-liquidity')]";
                    final String xpathMax = "(//span[@class='cursor-pointer']//span)[1]";
                    final String xpathAddCoinToPool = "//button[text()='Deposit' and not(@type)]";
                    if (SeleniumUtils.isElementPresent(driver, xpathAddPool)) {
                        LoggerUtil.log("Click vao Add Pool");
                        SeleniumUtils.waitForClickabilityAndClick(driver, xpathAddPool, 20);
                        waitUtils.sleepMillis(5000);
                        LoggerUtil.log("Click vao Max");
                        SeleniumUtils.waitForClickabilityAndClick(driver, xpathMax, 20);
                        approveTransaction(driver, "", waitUtils);
                        waitUtils.sleepMillis(4000);
                        LoggerUtil.log("Click vao Deposit");
                        SeleniumUtils.waitForClickabilityAndClick(driver, deposit, 20);
                        waitUtils.sleepMillis(5000);
                        LoggerUtil.log("Click vao Max");
                        SeleniumUtils.waitForClickabilityAndClick(driver, xpathMax, 20);
                        waitUtils.sleepMillis(5000);
                        LoggerUtil.log("Click vao xpath add coin to pool");
                        SeleniumUtils.waitForClickabilityAndClick(driver, xpathAddCoinToPool, 20);
                        switchOKXToVerifyTransClame(driver, waitUtils, 1);
                        waitUtils.sleepMillis(2000);
                        checkTransactionStatus(driver, waitUtils);
                    }
                    break;
            }

        }
    }

    private void switchOKXToVerifyTransClame(WebDriver driver, WaitUtils waitUtils, int currentTab) {
        waitUtils.sleepMillis(3000);
        System.out.println("Chuyển sang Tab OKX để xác nhận MINT");
        LoggerUtil.log("Chuyển sang Tab OKX để xác nhận MINT");
        SeleniumUtils.switchToTab(driver, 0);
        waitUtils.sleepMillis(3000);
        if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
            waitUtils.waitForClickability(By.xpath(buttonVerifyOkxXpath));
            SeleniumUtils.waitForClickabilityAndClick(driver, buttonVerifyOkxXpath, 10);
        } else {
            driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
            waitUtils.sleepMillis(3000);
            waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);
            if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                System.out.println("Có nút xác nhận MINT bên OKX");
                LoggerUtil.log("Có nút xác nhận MINT bên OKX");
                waitUtils.sleepMillis(5000);
                waitUtils.waitForClickability(By.xpath(buttonVerifyOkxXpath));
                SeleniumUtils.waitForClickabilityAndClick(driver, buttonVerifyOkxXpath, 10);
                waitUtils.sleepMillis(5000);
            } else {
                SeleniumUtils.switchToTab(driver, currentTab);
                approveTransaction(driver, buttonSwapXpath, waitUtils);
            }
        }
        SeleniumUtils.switchToTab(driver, currentTab);
        waitUtils.sleepMillis(5000);
    }

    public void addPool(WebDriver driver, String pass, String id) {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WaitUtils waitUtils = new WaitUtils(driver, 10);
        try {
            goToChromeExtention(driver, waitUtils);
            interactOKX(driver, waitUtils, pass);
            loginWalletWithBerachain(driver, waitUtils);
            swapTokenAction(driver, waitUtils);
            performSwaps(driver, waitUtils, 1);
            swapCoin(driver, "HONEY", "BERA", "20", waitUtils, 2);
            gotoClameBgt(driver, waitUtils, 1);
        } catch (NoSuchElementException e) {
            System.out.println("Khong tim thay Element " + e.getMessage());
            LoggerUtil.log("Khong tim thay Element " + e.getMessage());
        } finally {
            driver.quit();
            ProfileService profileService = new ProfileService();
            profileService.closeProfile(id);
        }
    }
}

