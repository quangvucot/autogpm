package com.vdq.autogpm.automation;

import com.vdq.autogpm.api.Profile;
import com.vdq.autogpm.util.SeleniumUtils;
import com.vdq.autogpm.util.WaitUtils;
import com.vdq.autogpm.webdriver.WebDriverManager;
import org.openqa.selenium.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProfileAutomation {

    private WebDriverManager webDriverManager;
    private WaitUtils waitUtils;

    String urlHomeBeraChain = "https://bartio.bex.berachain.com/";

    //    XPath mật khẩu OKX
    private final String okxPasswordXpath = "//input[@type='password' or @id='password']";
    // Mật khẩu OKX (Chưa mã hóa)
    private final String yourPassowrd = "VudqMeta1908@#";
    // Xpath nút kết nối ví
    private final String connectWalletButtonXpath = "//button[@data-testid='ConnectButton']";
    // Xpath Trạng thái kết  nối ví lỗi
    private final String wroingConnectWalletButtonXpath = "//button[text()='Wrong network']";
    // Xpath Shadow Popup
    private final String shadowHostSelector = "div[data-testid='dynamic-modal-shadow']";

    private final String buttonAdressWalletXpath = "(//nav//button[@aria-haspopup='dialog'])[1]";
    private final String shadowElementSelector = "button[data-testid='ListTile']";
    //
    private final String selectBeraChainNetwork = "button[data-testid='SelectNetworkButton']";
    private final String swapTokensXpath = "//button[contains(text(),'Swap Token')]";
    private final String transferTo = "(//button[contains(@class, 'items-center') and contains(@class, 'transition-duration-300')]//span[contains(@class,'truncate')])[2]";

    private final String transferFrom = "(//button[contains(@class, 'items-center') and contains(@class, 'transition-duration-300')]//span[contains(@class,'truncate')])[1]";
    private final String listCoinNameXpath = "//div[@role='dialog']//div[contains(@class,'max-h-')]//button//div[@class='flex flex-col']//span";
    private final String priceFromXpath = "(//input[@step='any' and @min])[1]";

    private final String previewButtonXpath = "//div[@class='w-full']//button[@type='button' and contains(text(),'Preview')]";
    private final String swapButtonXpath = "//button[text()='Wrap']";
    private final String dialogWattingWalletXpath = "//div[@role='dialog' and @data-state='open']";

    private final String buttonVerifyOkxXpath = "(//div[@class='_action-buttons_j3bvq_1']//button)[2]";
    private final String buttonStatusTransaction = "(//div[@role='dialog']//button)[2]";
    private final String statusTransaction = "//div[@role='dialog' and @data-state='open']//p";
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

    public ProfileAutomation() {
        this.webDriverManager = new WebDriverManager();

    }

    public void runAutomation(Profile profile) {
        WebDriver driver = webDriverManager.initializeDriver(profile);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        this.waitUtils = new WaitUtils(driver, 10);
        try {
            goToChromeExtention(driver);
            interactOKX(driver);
            beraChain(driver);
            swapTokenAction(driver);
            performSwaps(driver);
//            performMint(driver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private void interactOKX(WebDriver driver) {
        waitUtils.sleepSecond(5000);
        WebElement okxInputElm = SeleniumUtils.findElementByXPath(driver, okxPasswordXpath);
        waitUtils.sleepSecond(2000);
        waitUtils.sendKeysSlowly(okxInputElm, yourPassowrd, 300);
        waitUtils.sleepSecond(2000);
        SeleniumUtils.findElementByXPath(driver, okxPasswordXpath).sendKeys(Keys.ENTER);
        waitUtils.sleepSecond(2000);

    }

    private void swapBeraToWBERA(WebDriver driver) {
        WebElement fromElm = SeleniumUtils.findElementByXPath(driver, transferTo);
        fromElm.click();
        waitUtils.sleepSecond(4000);
        List<WebElement> webElementList = SeleniumUtils.findElementsByXPath(driver, listCoinTopXpath);
        for (WebElement webElement : webElementList) {
            if (webElement.getText().equals("WBERA")) {
                System.out.println("Đang chọn Coin chuyển");
                webElement.click();
                WebElement priceFromElm = SeleniumUtils.findElementByXPath(driver, priceFromXpath);
                waitUtils.sleepSecond(2000);
                System.out.println("Nhập gia chuyển");
                priceFromElm.sendKeys("0.1");
                waitUtils.sleepSecond(3000);

                if (SeleniumUtils.isElementPresent(driver, previewButtonXpath)) {
                    System.out.println("Có nút Preview");
                    waitUtils.waitForClickability(By.xpath(previewButtonXpath));
                } else if (SeleniumUtils.isElementPresent(driver, swapButtonXpath)) {
                    System.out.println("Có nút Swap");
                    SeleniumUtils.findElementByXPath(driver, swapButtonXpath).click();
                }
                waitUtils.sleepSecond(5000);
                waitUtils.waitForElementVisible(By.xpath(dialogWattingWalletXpath), 10);
                // Kiem tra xem co dialog thong bao dang xac nhan chuyen tien ben Bera khong?
                if (SeleniumUtils.isElementPresent(driver, dialogWattingWalletXpath)) {
                    System.out.println("Có hiễn thị thông báo Watting Transaction");
                    // Có hiễn thị thông báo Watting Transaction
                    System.out.println("Chuyển sang Tab OKX để xác nhận chuyển tiền");
                    SeleniumUtils.switchToTab(driver, 0);
                    waitUtils.sleepSecond(3000);
                    driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                    waitUtils.sleepSecond(3000);
                    waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);

                    // Kiem tra xem co hien thi nut xac nhan ben vi OKX khong?
                    if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                        System.out.println("Có nút xác nhận chueyern tiền bên OKX");
                        waitUtils.sleepSecond(5000);
                        SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                        System.out.println("Đã click vào nút xác nhận chuyển tiền");
                        waitUtils.sleepSecond(5000);
                        System.out.println("Chuyển sang tab thứ nhất xem trang thai chuyen tien");
                        SeleniumUtils.switchToTab(driver, 1);
                        waitUtils.sleepSecond(4000);
                        waitUtils.waitForElementVisible(By.xpath(buttonStatusTransaction), 10);

                        if (SeleniumUtils.isElementPresent(driver, buttonStatusTransaction)) {
                            System.out.println("Co dialog trang thai chuyen tien");
                            waitUtils.waitForElementVisible(By.xpath(iconStatusTrans), 20);
                            String transValue = SeleniumUtils.findElementByXPath(driver, statusTransaction).getText();
                            System.out.println("Trạng thái chuyển tiền " + transValue);
                            if (transValue.equalsIgnoreCase("Transaction pending")) {
                                waitUtils.waitForElementVisible(By.xpath(iconStatusTrans), 20);
                            }
                            transValue = SeleniumUtils.findElementByXPath(driver, statusTransaction).getText();
                            if (transValue.equalsIgnoreCase("Transaction Success")) {
                                System.out.println("Chuyển Thành công -> Đóng Popup");
                                waitUtils.sleepSecond(3000);
                                SeleniumUtils.findElementByXPath(driver, buttonStatusTransaction).click();
                                waitUtils.sleepSecond(3000);
                            } else {
                                waitUtils.sleepSecond(3000);
                                SeleniumUtils.findElementByXPath(driver, buttonStatusTransaction).click();
                                waitUtils.sleepSecond(3000);
                            }
                            System.out.println("DONE");
                        }
                    } else {
                        System.out.println("Không hiễn thị đoạn xac nhận chuển tiền");
                    }
                }
            }
        }

    }

    private void swapCoin(WebDriver driver, String coinName, String price, int numSwaps, int type) {
        System.out.println("Bắt đầu chuyển coin");

        for (int i = 0; i < numSwaps; i++) {
            String currentCoin;
            if (type == 0) {
                WebElement fromElm = SeleniumUtils.findElementByXPath(driver, transferTo);
                currentCoin = fromElm.getText();
                fromElm.click();
            } else {
                WebElement fromElm = SeleniumUtils.findElementByXPath(driver, changeSwapCoinXpath);
                fromElm.click();
                waitUtils.sleepSecond(3000);
                WebElement toElm = SeleniumUtils.findElementByXPath(driver, transferFrom);
                System.out.println("Click vào ây");
                currentCoin = toElm.getText();
                toElm.click();
            }
            waitUtils.sleepSecond(4000);
            List<WebElement> webElementList = SeleniumUtils.findElementsByXPath(driver, listCoinTopXpath);
            for (WebElement webElement : webElementList) {
                if (webElement.getText().equals(coinName)) {
                    System.out.println("Đang chọn Coin chuyển: " + coinName);
                    if (currentCoin.equals(webElement.getText())) {
                        SeleniumUtils.findElementByXPath(driver, closeButtonXpath).click();
                    } else {
                        webElement.click();
                    }
                    WebElement priceFromElm = SeleniumUtils.findElementByXPath(driver, priceFromXpath);
                    waitUtils.sleepSecond(2000);
                    System.out.println("Nhập giá chuyển: " + price);
                    priceFromElm.sendKeys(price);
                    waitUtils.sleepSecond(3000);

                    if (SeleniumUtils.isElementPresent(driver, previewButtonXpath)) {
                        System.out.println("Có nút Preview");
                        waitUtils.waitForClickability(By.xpath(previewButtonXpath));
                        SeleniumUtils.findElementByXPath(driver, previewButtonXpath).click();
                        waitUtils.sleepSecond(5000);
                        waitUtils.waitForClickability(By.xpath(buttonSwapXpath));
                        SeleniumUtils.findElementByXPath(driver, buttonSwapXpath).click();
                        waitUtils.sleepSecond(3000);
                    } else if (SeleniumUtils.isElementPresent(driver, swapButtonXpath)) {
                        System.out.println("Có nút Swap");
                        SeleniumUtils.findElementByXPath(driver, swapButtonXpath).click();
                    }
                    waitUtils.sleepSecond(5000);
                    waitUtils.waitForElementVisible(By.xpath(dialogWattingWalletXpath), 10);

                    // Kiểm tra xem có dialog thông báo đang xác nhận chuyển tiền bên Bera không?
                    if (SeleniumUtils.isElementPresent(driver, dialogWattingWalletXpath)) {
                        System.out.println("Có hiển thị thông báo Watting Transaction");
                        // Có hiển thị thông báo Watting Transaction
                        System.out.println("Chuyển sang Tab OKX để xác nhận chuyển tiền");
                        SeleniumUtils.switchToTab(driver, 0);
                        waitUtils.sleepSecond(3000);
                        driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                        waitUtils.sleepSecond(3000);
                        waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);

                        // Kiểm tra xem có hiển thị nút xác nhận bên ví OKX không?
                        if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                            System.out.println("Có nút xác nhận chuyển tiền bên OKX");
                            waitUtils.sleepSecond(5000);
                            SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                            System.out.println("Đã click vào nút xác nhận chuyển tiền");
                            waitUtils.sleepSecond(5000);
                            System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                            SeleniumUtils.switchToTab(driver, 1);
                            waitUtils.sleepSecond(4000);
                            waitUtils.waitForElementVisible(By.xpath(buttonStatusTransaction), 10);

                            if (SeleniumUtils.isElementPresent(driver, buttonStatusTransaction)) {
                                System.out.println("Có dialog trạng thái chuyển tiền");
                                waitUtils.waitForElementVisible(By.xpath(iconStatusTrans), 20);
                                String transValue = SeleniumUtils.findElementByXPath(driver, statusTransaction).getText();
                                System.out.println("Trạng thái chuyển tiền: " + transValue);
                                if (transValue.equalsIgnoreCase("Transaction pending")) {
                                    waitUtils.waitForElementVisible(By.xpath(iconStatusTrans), 20);
                                }
                                transValue = SeleniumUtils.findElementByXPath(driver, statusTransaction).getText();
                                if (transValue.equalsIgnoreCase("Transaction Success")) {
                                    System.out.println("Chuyển thành công -> Đóng Popup");
                                    waitUtils.sleepSecond(3000);
                                    SeleniumUtils.findElementByXPath(driver, buttonStatusTransaction).click();
                                    waitUtils.sleepSecond(3000);
                                } else {
                                    System.out.println("Chuyển thất bại -> Đóng Popup");
                                    waitUtils.sleepSecond(3000);
                                    SeleniumUtils.findElementByXPath(driver, buttonStatusTransaction).click();
                                    waitUtils.sleepSecond(3000);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }


    private void performSwaps(WebDriver driver) {
        swapCoin(driver, "WBERA", "0.1", 1, 0);
//        swapCoin(driver, "HONEY", "0.7", 1, 0);
//        swapCoin(driver, "WBTC", "0.05", 1, 0);
//        swapCoin(driver, "WETH", "0.03", 1, 0);
//        swapCoin(driver, "WBERA", "0.03", 1, 0);
        swapCoin(driver, "HONEY", "2", 1, 1);
    }

    private void performMint(WebDriver driver) {
        mintCoin(driver, "DAI", "0.5", 1);
    }

    private void beraChain(WebDriver driver) {
        SeleniumUtils.openNewTab(driver);
        SeleniumUtils.switchToTab(driver, 1);
        waitUtils.sleepSecond(1000);
        driver.get(urlHomeBeraChain);
        waitUtils.sleepSecond(3000);
        waitUtils.waitForElementVisible(By.xpath(buttonAdressWalletXpath), 10);
        if (SeleniumUtils.isElementPresent(driver, buttonAdressWalletXpath)) {
            String addressWallet = SeleniumUtils.findElementByXPath(driver, buttonAdressWalletXpath).getText();
            if (addressWallet.toLowerCase().contains("0x")) {

            }
        } else {
            if (SeleniumUtils.isElementPresent(driver, connectWalletButtonXpath)) {
                WebElement connectWalletButtonElm = SeleniumUtils.findElementByXPath(driver, connectWalletButtonXpath);
                connectWalletButtonElm.click();
                System.out.println("Click into Connect Wallet");
                waitUtils.sleepSecond(4000);
                if (SeleniumUtils.isElementInShadowDOMPresent(driver, shadowHostSelector, shadowElementSelector)) {
                    WebElement shadowElement = SeleniumUtils.findElementInShadowDOM(driver, shadowHostSelector, shadowElementSelector);
                    waitUtils.sleepSecond(2000);
                    shadowElement.click();
                    waitUtils.sleepSecond(5000);
                    WebElement beraChainNetwork = SeleniumUtils.findElementInShadowDOM(driver, shadowHostSelector, selectBeraChainNetwork);
                    waitUtils.sleepSecond(1000);
                    beraChainNetwork.click();
                }

            } else if (SeleniumUtils.isElementPresent(driver, wroingConnectWalletButtonXpath)) {
                System.out.println("Click into Wrong Connect");
                WebElement wrongConnectWalletButtonElm = SeleniumUtils.findElementByXPath(driver, wroingConnectWalletButtonXpath);
                wrongConnectWalletButtonElm.click();
            }
        }
    }

    private void swapTokenAction(WebDriver driver) {
        WebElement connectWalletButtonElm = SeleniumUtils.findElementByXPath(driver, swapTokensXpath);
        connectWalletButtonElm.click();
        waitUtils.sleepSecond(5000);
    }


    private void mintCoin(WebDriver driver, String coinName, String price, int numSwaps) {
        driver.get("https://bartio.honey.berachain.com");
        waitUtils.sleepSecond(3000);
        waitUtils.waitForVisibility(By.xpath(transferFrom));
        WebElement fromElm = SeleniumUtils.findElementByXPath(driver, transferFrom);
        fromElm.click();
        waitUtils.sleepSecond(4000);
        List<WebElement> webElementList = SeleniumUtils.findElementsByXPath(driver, listCoinNameXpath);
        for (WebElement webElement : webElementList) {
            System.out.println("WebElement " + webElement.getText());
            if (webElement.getText().equals(coinName)) {
                webElement.click();
                WebElement priceFromElm = SeleniumUtils.findElementByXPath(driver, priceFromXpath);
                waitUtils.sleepSecond(2000);
                priceFromElm.sendKeys(price);
                waitUtils.sleepSecond(3000);
                waitUtils.waitForElementVisible(By.xpath(buttonApproveCoinXpath), 20);
                if (SeleniumUtils.isElementPresent(driver, buttonApproveCoinXpath)) {
                    SeleniumUtils.findElementByXPath(driver, buttonApproveCoinXpath).click();
                    waitUtils.sleepSecond(3000);
                    System.out.println("Chuyển sang Tab OKX để xác nhận Approve Mint Coin");
                    SeleniumUtils.switchToTab(driver, 0);
                    waitUtils.sleepSecond(3000);
                    driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                    waitUtils.sleepSecond(3000);
                    waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);

                    // Kiểm tra xem có hiển thị nút xác nhận bên ví OKX không?
                    if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                        System.out.println("Có nút xác nhận Approve Coin bên OKX");
                        waitUtils.sleepSecond(5000);
                        SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                        System.out.println("Đã click vào nút xác nhận Approve Coin");
                        waitUtils.sleepSecond(5000);
                        System.out.println("Chuyển sang tab thứ nhất để MINT");
                        SeleniumUtils.switchToTab(driver, 1);
                        waitUtils.sleepSecond(3000);
                        waitUtils.waitForElementVisible(By.xpath(buttonMintHoneyXpath), 20);
                        if (SeleniumUtils.isElementPresent(driver, buttonMintHoneyXpath)) {
                            waitUtils.waitForClickability(By.xpath(buttonMintHoneyXpath));
                            System.out.println("Click vào MINT ");
                            SeleniumUtils.findElementByXPath(driver, buttonMintHoneyXpath).click();
                            waitUtils.sleepSecond(3000);
                            System.out.println("Chuyển sang Tab OKX để xác nhận MINT");
                            SeleniumUtils.switchToTab(driver, 0);
                            waitUtils.sleepSecond(3000);
                            driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                            waitUtils.sleepSecond(3000);
                            waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);
                            if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                                System.out.println("Có nút xác nhận MINT bên OKX");
                                waitUtils.sleepSecond(5000);
                                SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                                System.out.println("Đã click vào nút xác nhận MINT");
                                waitUtils.sleepSecond(5000);
                                System.out.println("Chuyển sang tab thứ nhất xem trạng thái MINT");
                                SeleniumUtils.switchToTab(driver, 1);

                            }
                        }
                    }
                } else if (SeleniumUtils.isElementPresent(driver, buttonMintHoneyXpath)) {
                    SeleniumUtils.findElementByXPath(driver, buttonMintHoneyXpath).click();
                    waitUtils.sleepSecond(5000);
                    System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                    SeleniumUtils.switchToTab(driver, 0);
                    waitUtils.sleepSecond(3000);
                    driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                    waitUtils.sleepSecond(3000);
                    waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);
                    if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                        System.out.println("Có nút xác nhận chuyển tiền bên OKX");
                        waitUtils.sleepSecond(5000);
                        SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                        System.out.println("Đã click vào nút xác nhận chuyển tiền");
                        waitUtils.sleepSecond(5000);
                        System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                        SeleniumUtils.switchToTab(driver, 1);
                        waitUtils.sleepSecond(10000);
                    }
                }

//                REDEEM
                SeleniumUtils.findElementByXPath(driver, buttonRedeemXpath).click();
                WebElement priceFromElmt = SeleniumUtils.findElementByXPath(driver, priceFromXpath);
                waitUtils.sleepSecond(2000);
                priceFromElmt.sendKeys(price);
                waitUtils.sleepSecond(3000);
                waitUtils.waitForElementVisible(By.xpath(buttonApproveCoinXpath), 20);
                if (SeleniumUtils.isElementPresent(driver, buttonApproveCoinXpath)) {
                    SeleniumUtils.findElementByXPath(driver, buttonApproveCoinXpath).click();
                    waitUtils.sleepSecond(3000);
                    System.out.println("Chuyển sang Tab OKX để xác nhận chuyển tiền");
                    SeleniumUtils.switchToTab(driver, 0);
                    waitUtils.sleepSecond(3000);
                    driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                    waitUtils.sleepSecond(3000);
                    waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);

                    // Kiểm tra xem có hiển thị nút xác nhận bên ví OKX không?
                    if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                        System.out.println("Có nút xác nhận chuyển tiền bên OKX");
                        waitUtils.sleepSecond(5000);
                        SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                        System.out.println("Đã click vào nút xác nhận chuyển tiền");
                        waitUtils.sleepSecond(5000);
                        System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                        SeleniumUtils.switchToTab(driver, 1);
                        waitUtils.sleepSecond(3000);
                        waitUtils.waitForElementVisible(By.xpath(buttonMintHoneyXpath), 20);
                        if (SeleniumUtils.isElementPresent(driver, buttonMintHoneyXpath)) {
                            waitUtils.waitForClickability(By.xpath(buttonMintHoneyXpath));
                            SeleniumUtils.findElementByXPath(driver, buttonMintHoneyXpath).click();
                            waitUtils.sleepSecond(3000);
                            System.out.println("Chuyển sang Tab OKX để xác nhận chuyển tiền");
                            SeleniumUtils.switchToTab(driver, 0);
                            waitUtils.sleepSecond(3000);
                            driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                            waitUtils.sleepSecond(3000);
                            waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);
                            if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                                System.out.println("Có nút xác nhận chuyển tiền bên OKX");
                                waitUtils.sleepSecond(5000);
                                SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                                System.out.println("Đã click vào nút xác nhận chuyển tiền");
                                waitUtils.sleepSecond(5000);
                                System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                                SeleniumUtils.switchToTab(driver, 1);

                            }
                        }
                    }
                } else if (SeleniumUtils.isElementPresent(driver, buttonMintHoneyXpath)) {
                    SeleniumUtils.findElementByXPath(driver, buttonMintHoneyXpath).click();
                    waitUtils.sleepSecond(5000);
                    System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                    SeleniumUtils.switchToTab(driver, 0);
                    waitUtils.sleepSecond(3000);
                    driver.get("chrome-extension://mcohilncbfahbmgdjkbpemcciiolgcge/home.html");
                    waitUtils.sleepSecond(3000);
                    waitUtils.waitForElementVisible(By.xpath(buttonVerifyOkxXpath), 10);
                    if (SeleniumUtils.isElementPresent(driver, buttonVerifyOkxXpath)) {
                        System.out.println("Có nút xác nhận chuyển tiền bên OKX");
                        waitUtils.sleepSecond(5000);
                        SeleniumUtils.findElementByXPath(driver, buttonVerifyOkxXpath).click();
                        System.out.println("Đã click vào nút xác nhận chuyển tiền");
                        waitUtils.sleepSecond(5000);
                        System.out.println("Chuyển sang tab thứ nhất xem trạng thái chuyển tiền");
                        SeleniumUtils.switchToTab(driver, 1);
                        waitUtils.sleepSecond(10000);
                    }
                }
            }

        }
    }

    private void goToChromeExtention(WebDriver driver) {
        driver.get("chrome://extensions");
        waitUtils.sleepSecond(2000);
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
}
