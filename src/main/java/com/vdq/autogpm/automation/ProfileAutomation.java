package com.vdq.autogpm.automation;

import com.vdq.autogpm.api.Profile;
import com.vdq.autogpm.util.SeleniumUtils;
import com.vdq.autogpm.util.WaitUtils;
import com.vdq.autogpm.webdriver.WebDriverManager;
import org.openqa.selenium.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ProfileAutomation {
    private WebDriver driver;
    private WebDriverManager webDriverManager;
    private WaitUtils waitUtils;
    private Map<String, List<String>> coinSwaps;
    String urlHomeBeraChain = "https://bartio.bex.berachain.com/";
    //    XPath mật khẩu OKX
    private final String okxPasswordXpath = "//input[@type='password' or @id='password']";
    // Mật khẩu OKX (Chưa mã hóa)
    private final String yourPassowrd = "59Bachlieu@#";
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
    private final String previewButtonXpath = "//div[@class='w-full']//button[@type='button' and contains(text(),'Preview')]";
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

    public ProfileAutomation() {
        this.webDriverManager = new WebDriverManager();
        this.coinSwaps = initializeCoinSwaps();
    }

    public void runAutomation(Profile profile) {
        driver = webDriverManager.initializeDriver(profile);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        this.waitUtils = new WaitUtils(driver, 10);
        try {
            goToChromeExtention();
            interactOKX();
            loginWalletWithBerachain();
            swapTokenAction();
            performSwaps();
//            performMint(driver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    /*
        Hàm đăng nhập ví OKX
     */
    private void interactOKX() {
        waitUtils.sleepMillis(5000);
        WebElement okxInputElm = SeleniumUtils.findElementByXPath(driver, okxPasswordXpath);
        waitUtils.sleepMillis(2000);
        waitUtils.sendKeysSlowly(okxInputElm, yourPassowrd, 300);
        waitUtils.sleepMillis(2000);
        SeleniumUtils.findElementByXPath(driver, okxPasswordXpath).sendKeys(Keys.ENTER);
        waitUtils.sleepMillis(2000);
    }
//    private void swapCoin(String coinName, String price, int numSwaps, int type) {
//        System.out.println("Bắt đầu chuyển coin");
//        System.out.println("Property " + SeleniumUtils.getProperty());
//
//        for (int i = 0; i < numSwaps; i++) {
//            String currentCoin;
//            if (type == 0) {
//                WebElement fromElm = SeleniumUtils.findElementByXPath(driver, coinNameTranferXpath);
//                currentCoin = fromElm.getText();
//                fromElm.click();
//            } else {
//                WebElement fromElm = SeleniumUtils.findElementByXPath(driver, changeSwapCoinXpath);
//                fromElm.click();
//                waitUtils.sleepMillis(3000);
//                WebElement toElm = SeleniumUtils.findElementByXPath(driver, coinNameReceiverXpath);
//                System.out.println("Click vào đây");
//                currentCoin = toElm.getText();
//                toElm.click();
//            }
//            waitUtils.sleepMillis(4000);
//            List<WebElement> webElementList = SeleniumUtils.findElementsByXPath(driver, listCoinTopXpath);
//            for (WebElement webElement : webElementList) {
//                if (webElement.getText().equals(coinName)) {
//                    System.out.println("Đang chọn Coin chuyển: " + coinName);
//                    if (currentCoin.equals(webElement.getText())) {
//                        SeleniumUtils.findElementByXPath(driver, closeButtonXpath).click();
//                    } else {
//                        webElement.click();
//                    }
//                    WebElement priceFromElm = SeleniumUtils.findElementByXPath(driver, priceFromXpath);
//                    waitUtils.sleepMillis(2000);
//                    System.out.println("Nhập giá chuyển: " + price);
//                    priceFromElm.sendKeys(price);
//                    waitUtils.sleepMillis(3000);
//
//                    if (SeleniumUtils.isElementPresent(driver, previewButtonXpath)) {
//                        System.out.println("Có nút Preview");
//                        waitUtils.waitForClickability(By.xpath(previewButtonXpath));
//                        SeleniumUtils.findElementByXPath(driver, previewButtonXpath).click();
//                        waitUtils.sleepMillis(5000);
//                        waitUtils.waitForClickability(By.xpath(buttonSwapXpath));
//                        SeleniumUtils.findElementByXPath(driver, buttonSwapXpath).click();
//                        waitUtils.sleepMillis(3000);
//                    } else if (SeleniumUtils.isElementPresent(driver, swapButtonXpath)) {
//                        System.out.println("Có nút Swap");
//                        SeleniumUtils.findElementByXPath(driver, swapButtonXpath).click();
//                    }
//                    waitUtils.sleepMillis(5000);
//                    waitUtils.waitForElementVisible(By.xpath(dialogWattingWalletXpath), 10);
//
//                    // Kiểm tra xem có dialog thông báo đang xác nhận chuyển tiền bên Bera không?
//                    if (SeleniumUtils.isElementPresent(driver, dialogWattingWalletXpath)) {
//                        System.out.println("Có hiển thị thông báo Watting Transaction");
//                        // Có hiển thị thông báo Watting Transaction
//                        System.out.println("Chuyển sang Tab OKX để xác nhận chuyển tiền");
//                        SeleniumUtils.switchToTab(driver, 0);
//                        waitUtils.sleepMillis(3000);
//                        driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
//                        waitUtils.sleepMillis(3000);
//                        waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);
//
//                        // Kiểm tra xem có hiển thị nút xác nhận bên ví OKX không?
//                        if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
//                            System.out.println("Có nút xác nhận chuyển tiền bên OKX");
//                            waitUtils.sleepMillis(5000);
//                            SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
//                            System.out.println("Đã click vào nút xác nhận chuyển tiền");
//                            waitUtils.sleepMillis(5000);
//                            System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
//                            SeleniumUtils.switchToTab(driver, 1);
//                            waitUtils.sleepMillis(4000);
//                            waitUtils.waitForElementVisible(By.xpath(buttonStatusTransaction), 10);
//
//                            if (SeleniumUtils.isElementPresent(driver, buttonStatusTransaction)) {
//                                System.out.println("Có dialog trạng thái chuyển tiền");
//                                waitUtils.waitForElementVisible(By.xpath(iconStatusTrans), 20);
//                                String transValue = SeleniumUtils.findElementByXPath(driver, statusTransaction).getText();
//                                System.out.println("Trạng thái chuyển tiền: " + transValue);
//                                if (transValue.equalsIgnoreCase("Transaction pending")) {
//                                    waitUtils.waitForElementVisible(By.xpath(iconStatusTrans), 20);
//                                }
//                                transValue = SeleniumUtils.findElementByXPath(driver, statusTransaction).getText();
//                                if (transValue.equalsIgnoreCase("Transaction Success")) {
//                                    System.out.println("Chuyển thành công -> Đóng Popup");
//                                    waitUtils.sleepMillis(3000);
//                                    SeleniumUtils.findElementByXPath(driver, buttonStatusTransaction).click();
//                                    waitUtils.sleepMillis(3000);
//                                } else {
//                                    System.out.println("Chuyển thất bại -> Đóng Popup");
//                                    waitUtils.sleepMillis(3000);
//                                    SeleniumUtils.findElementByXPath(driver, buttonStatusTransaction).click();
//                                    waitUtils.sleepMillis(3000);
//                                }
//                            }
//                        }
//                    }
//                    break;
//                }
//            }
//        }
//    }

    // Danh sách các đồng Coin Can Chuyen
    private Map<String, List<String>> initializeCoinSwaps() {
        Map<String, List<String>> coinSwaps = new HashMap<>();
        coinSwaps.put("BERA", List.of("WBERA", "HONEY", "WBTC", "WETH", "DAI", "STGUSDC", "USDT"));
        coinSwaps.put("HONEY", List.of("Bera", "WBERA", "WBTC", "WETH", "DAI"));
        return coinSwaps;
    }


    private void performSwaps() {
        for (Map.Entry<String, List<String>> entry : coinSwaps.entrySet()) {
            String fromCoin = entry.getKey();
            System.out.println("From Coin " + fromCoin);
            List<String> toCoins = entry.getValue();
            System.out.println("To Coin " + toCoins);
            swapCoins(fromCoin, toCoins, "0.5", 3);
        }
    }

    /*
    Login OKX Wallet With Bera Chain
    */
    private void loginWalletWithBerachain() {
        SeleniumUtils.openNewTab(driver);
        SeleniumUtils.switchToTab(driver, 1);
        waitUtils.sleepMillis(1000);
        driver.get(urlHomeBeraChain);
        waitUtils.sleepMillis(3000);
        waitUtils.waitForElementVisible(By.xpath(buttonAdressWalletXpath), 10);
//        if (SeleniumUtils.isElementPresent(driver, buttonAdressWalletXpath)) {
//            String addressWallet = SeleniumUtils.findElementByXPath(driver, buttonAdressWalletXpath).getText();
//            if (addressWallet.toLowerCase().contains("0x")) {
//                System.out.println("Da Dang Nhap");
//            }
//        } else {
        if (SeleniumUtils.isElementPresent(driver, connectWalletButtonXpath)) {
            WebElement connectWalletButtonElm = SeleniumUtils.findElementByXPath(driver, connectWalletButtonXpath);
            connectWalletButtonElm.click();
            System.out.println("Click into Connect Wallet");
            waitUtils.sleepMillis(4000);
            if (SeleniumUtils.isElementInShadowDOMPresent(driver, shadowHostSelector, shadowElementSelector)) {
                WebElement shadowElement = SeleniumUtils.findElementInShadowDOM(driver, shadowHostSelector, shadowElementSelector);
                waitUtils.sleepMillis(2000);
                shadowElement.click();
                waitUtils.sleepMillis(5000);
                if (SeleniumUtils.isElementInShadowDOMPresent(driver, shadowHostSelector, selectBeraChainNetwork)) {
                    WebElement beraChainNetwork = SeleniumUtils.findElementInShadowDOM(driver, shadowHostSelector, selectBeraChainNetwork);
                    waitUtils.sleepMillis(1000);
                    beraChainNetwork.click();
                }
            }

        } else if (SeleniumUtils.isElementPresent(driver, wroingConnectWalletButtonXpath)) {
            System.out.println("Click into Wrong Connect");
            WebElement wrongConnectWalletButtonElm = SeleniumUtils.findElementByXPath(driver, wroingConnectWalletButtonXpath);
            wrongConnectWalletButtonElm.click();
        }

    }

    /*
      Đi vào trang swap
     */
    private void swapTokenAction() {
        WebElement connectWalletButtonElm = SeleniumUtils.findElementByXPath(driver, swapTokensXpath);
        connectWalletButtonElm.click();
        waitUtils.sleepMillis(5000);
    }


    private void mintCoin(String coinName, String price, int numSwaps) {
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
                    waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);

                    // Kiểm tra xem có hiển thị nút xác nhận bên ví OKX không?
                    if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                        System.out.println("Có nút xác nhận Approve Coin bên OKX");
                        waitUtils.sleepMillis(5000);
                        SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                        System.out.println("Đã click vào nút xác nhận Approve Coin");
                        waitUtils.sleepMillis(5000);
                        System.out.println("Chuyển sang tab thứ nhất để MINT");
                        SeleniumUtils.switchToTab(driver, 1);
                        waitUtils.sleepMillis(3000);
                        waitUtils.waitForElementVisible(By.xpath(buttonMintHoneyXpath), 20);
                        if (SeleniumUtils.isElementPresent(driver, buttonMintHoneyXpath)) {
                            waitUtils.waitForClickability(By.xpath(buttonMintHoneyXpath));
                            System.out.println("Click vào MINT ");
                            SeleniumUtils.findElementByXPath(driver, buttonMintHoneyXpath).click();
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
                                SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                                System.out.println("Đã click vào nút xác nhận MINT");
                                waitUtils.sleepMillis(5000);
                                System.out.println("Chuyển sang tab thứ nhất xem trạng thái MINT");
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

    private void goToChromeExtention() {
        driver.get("chrome://extensions");
        waitUtils.sleepMillis(2000);
        String idExtention = getIdOfExtention(driver);
        String urlChrome = "chrome-extension://" + idExtention + "/home.html#onboarding/welcome";
        driver.get(urlChrome);

    }

    private String getIdOfExtention(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "var shadowRoot = document.querySelector('extensions-manager').shadowRoot;" +
                "shadowRoot = shadowRoot.querySelector('#viewManager');" +
                "shadowRoot = shadowRoot.querySelector('#items-list').shadowRoot;" +
                "var listItems = shadowRoot.querySelectorAll('extensions-item');" +
                "let extId = '';" +
                "listItems.forEach((item) => {" +
                "    const itemShadow = item.shadowRoot;" +
                "    const name = itemShadow.getElementById('name').innerText;" +
                "    const id = item.getAttribute('id');" +
                "    if (name.includes('OKX')) {" +
                "        extId = id;" +
                "    }" +
                "});" +
                "return extId;";
        String id = (String) js.executeScript(script);
        return id;
    }

    /*  ---------------------------------------------------------------------------------------*/
    public void swapCoins(String fromCoin, List<String> toCoins, String price, int numSwaps) {
        for (String toCoin : toCoins) {
            if (!fromCoin.equals(toCoin)) {
                for (int i = 0; i < numSwaps; i++) {
                    System.out.println("Bắt đầu chuyển từ " + fromCoin + " sang " + toCoin);
                    swapCoin(fromCoin, toCoin, price);
                }
            } else {
                System.out.println("Không thể chuyển từ " + fromCoin + " sang chính nó.");
            }
        }
    }

    private void swapCoin(String fromCoin, String toCoin, String price) {
        // Lấy ra tên coin hiện tại
        String receiverCoinName = getCurrentCoin(coinNameReceiverXpath);
        String tranferCoinName = getCurrentCoin(coinNameTranferXpath);
        System.out.println("Tên coin chuyển: " + tranferCoinName);
        System.out.println("Tên coin nhận: " + receiverCoinName);
        System.out.println("To Coin " + toCoin);
        waitUtils.sleepMillis(2000);
        // Coin nhan trung voi coin gui
        if (tranferCoinName.equalsIgnoreCase(fromCoin)) {
            System.out.println("Hiện tại coin chuyển trùng với coin chẩn bị chọn -> Chỉ cần chọn coin nhân thôi ");
            if (!receiverCoinName.equalsIgnoreCase(toCoin)) {
                selectCoin(toCoin);

            }
            enterPrice(price);
            approveTransaction(buttonSwapXpath);

        } else if (tranferCoinName.equalsIgnoreCase(toCoin)) {
            System.out.println("Coin chuan bị chon trung voi coin chuyen => Swap lai thoi");
            WebElement fromElm = SeleniumUtils.findElementByXPath(driver, changeSwapCoinXpath);
            fromElm.click();
        } else {
            System.out.println("Chon coin binh thuong thoi");
        }
//        if (!tranferCoinName.equals(fromCoin)) {
//            selectCoin(fromCoin);
//            waitUtils.sleepMillis(2000);
//        }
//        if (!currentCoin.equals(toCoin) && selectCoin(toCoin, currentCoin)) {
//            enterPrice(price);
//            clickSwapButtons();
//            confirmTransaction();
//        } else {
//            System.out.println("Không thể chuyển từ " + currentCoin + " sang " + toCoin);
//        }
    }

    private String getCurrentCoin(String xpathCoin) {
        WebElement fromElm;
        fromElm = SeleniumUtils.findElementByXPath(driver, xpathCoin);
        return fromElm.getText();
    }

    private String clickTransferButton(String fromCoin) {
        WebElement fromElm;
        String currentCoin;
        fromElm = SeleniumUtils.findElementByXPath(driver, coinNameTranferXpath);
        currentCoin = fromElm.getText();
        fromElm.click();
        fromElm = SeleniumUtils.findElementByXPath(driver, changeSwapCoinXpath);
        fromElm.click();
        waitUtils.sleepMillis(3000);
        WebElement toElm = SeleniumUtils.findElementByXPath(driver, coinNameReceiverXpath);
        System.out.println("Click vào đây");
        currentCoin = toElm.getText();
        toElm.click();
        return currentCoin;
    }

    private boolean selectCoin(String coinName) {
        WebElement fromElm = SeleniumUtils.findElementByXPath(driver, coinNameTranferXpath);
        fromElm.click();
        waitUtils.sleepMillis(3000);
        List<WebElement> webElementList = SeleniumUtils.findElementsByXPath(driver, listCoinTopXpath);
        for (WebElement webElement : webElementList) {
            if (webElement.getText().equals(coinName)) {
                System.out.println("Đang chọn Coin chuyển: " + coinName);
                webElement.click();
                return true; // Trả về true nếu chọn đồng coin thành công
            }
        }
        return false; // Trả về false nếu không tìm thấy đồng coin
    }

    private boolean selectCoin(String coinName, String currentCoin) {
        List<WebElement> webElementList = SeleniumUtils.findElementsByXPath(driver, listCoinTopXpath);

        for (WebElement webElement : webElementList) {
            if (webElement.getText().equals(coinName)) {
                System.out.println("Đang chọn Coin chuyển: " + coinName);
                if (currentCoin.equals(webElement.getText())) {
                    SeleniumUtils.findElementByXPath(driver, closeButtonXpath).click();
                } else {
                    webElement.click();
                }
                return true; // Trả về true nếu chọn đồng coin thành công
            }
        }
        return false; // Trả về false nếu không tìm thấy đồng coin
    }

    private void enterPrice(String price) {
        WebElement priceFromElm = SeleniumUtils.findElementByXPath(driver, priceFromXpath);
        waitUtils.sleepMillis(2000);
        System.out.println("Nhập giá chuyển: " + price);
        priceFromElm.sendKeys(price);
        waitUtils.sleepMillis(3000);
    }

    private void clickSwapButtons() {
        if (SeleniumUtils.isElementPresent(driver, previewButtonXpath)) {
            System.out.println("Có nút Preview");
            waitUtils.waitForClickability(By.xpath(previewButtonXpath));
            SeleniumUtils.findElementByXPath(driver, previewButtonXpath).click();
            waitUtils.sleepMillis(5000);
            waitUtils.waitForClickability(By.xpath(buttonSwapXpath));
            SeleniumUtils.findElementByXPath(driver, buttonSwapXpath).click();
            waitUtils.sleepMillis(3000);
        } else if (SeleniumUtils.isElementPresent(driver, swapButtonXpath)) {
            System.out.println("Có nút Swap");
            SeleniumUtils.findElementByXPath(driver, swapButtonXpath).click();
        }
        waitUtils.sleepMillis(5000);
    }

    private void confirmTransaction() {
        waitUtils.waitForElementVisible(By.xpath(dialogWattingWalletXpath), 10000);
        if (SeleniumUtils.isElementPresent(driver, dialogWattingWalletXpath)) {
            System.out.println("Có hiển thị thông báo Watting Transaction");
            SeleniumUtils.switchToTab(driver, 0);
            waitUtils.sleepMillis(3000);
            driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
            waitUtils.sleepMillis(3000);
            waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10000);

            if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                System.out.println("Có nút xác nhận chuyển tiền bên OKX");
                waitUtils.sleepMillis(5000);
                SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                System.out.println("Đã click vào nút xác nhận chuyển tiền");
                waitUtils.sleepMillis(5000);
                SeleniumUtils.switchToTab(driver, 1);
                waitUtils.sleepMillis(4000);
                checkTransactionStatus();
            }
        }
    }

    private void checkTransactionStatus() {
        waitUtils.waitForElementVisible(By.xpath(buttonStatusTransaction), 10000);

        if (SeleniumUtils.isElementPresent(driver, buttonStatusTransaction)) {
            System.out.println("Có dialog trạng thái chuyển tiền");
            waitUtils.waitForElementVisible(By.xpath(iconStatusTrans), 20000);

            String transValue = SeleniumUtils.findElementByXPath(driver, statusTransaction).getText();
            System.out.println("Trạng thái chuyển tiền: " + transValue);

            if (transValue.equalsIgnoreCase("Transaction pending")) {
                waitUtils.waitForElementVisible(By.xpath(iconStatusTrans), 20000);
            }

            transValue = SeleniumUtils.findElementByXPath(driver, statusTransaction).getText();
            if (transValue.equalsIgnoreCase("Transaction Success")) {
                System.out.println("Chuyển thành công -> Đóng Popup");
                waitUtils.sleepMillis(3000);
                SeleniumUtils.findElementByXPath(driver, buttonStatusTransaction).click();
            } else {
                System.out.println("Chuyển thất bại -> Đóng Popup");
                waitUtils.sleepMillis(3000);
                SeleniumUtils.findElementByXPath(driver, buttonStatusTransaction).click();
            }
            waitUtils.sleepMillis(3000);
        }
    }

    private void approveTransaction(String xpathAction) {
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
            waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);

            // Kiểm tra xem có hiển thị nút xác nhận bên ví OKX không?
            if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                System.out.println("Có nút xác nhận Approve Coin bên OKX");
                waitUtils.sleepMillis(5000);
                SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                System.out.println("Đã click vào nút xác nhận Approve Coin");
                waitUtils.sleepMillis(5000);
                System.out.println("Chuyển sang tab thứ nhất để MINT");
                SeleniumUtils.switchToTab(driver, 1);
                waitUtils.sleepMillis(3000);
                waitUtils.waitForElementVisible(By.xpath(xpathAction), 20);

                if (SeleniumUtils.isElementPresent(driver, xpathAction)) {
                    waitUtils.waitForClickability(By.xpath(xpathAction));
                    System.out.println("Click vào MINT ");
                    SeleniumUtils.findElementByXPath(driver, xpathAction).click();
                } else if (SeleniumUtils.isElementPresent(driver, previewButtonXpath)) {
                    clickSwapButtons();
                }
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
                    SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                    System.out.println("Đã click vào nút xác nhận MINT");
                    waitUtils.sleepMillis(5000);
                    System.out.println("Chuyển sang tab thứ nhất xem trạng thái MINT");
                    SeleniumUtils.switchToTab(driver, 1);

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
        } else if (SeleniumUtils.isElementPresent(driver, previewButtonXpath)) {
            clickSwapButtons();

        }
    }
}

