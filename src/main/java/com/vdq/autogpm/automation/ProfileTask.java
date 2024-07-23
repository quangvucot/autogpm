package com.vdq.autogpm.automation;

import com.vdq.autogpm.api.Profile;
import com.vdq.autogpm.service.ProfileService;
import com.vdq.autogpm.util.LoggerUtil;
import com.vdq.autogpm.util.Utils;
import com.vdq.autogpm.webdriver.WebDriverManager;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.Callable;
import java.util.logging.Logger;


public class ProfileTask implements Callable<Profile> {
    private Profile profile;
    private ProfileService profileService;
    private ProfileAutomation profileAutomation;
    private String pass;
    private int type;
    private static final Logger logger = Logger.getLogger(ProfileTask.class.getName());

    public ProfileTask(Profile profile, ProfileService profileService, ProfileAutomation profileAutomation, String okxPassword, int type) {
        this.profile = profile;
        this.profileService = profileService;
        this.profileAutomation = profileAutomation;
        this.pass = okxPassword;
        this.type = type;
        LoggerUtil.initLogger(Utils.removeGmail(profile.getName()));
    }


    @Override
    public Profile call() throws Exception {
        logger.info("Bắt đầu khởi chạy profile" + profile.getName());
        LoggerUtil.log("Starting task for profile: " + profile.getName());
        try {
            WebDriverManager webDriverManager = new WebDriverManager();
            // Lấy dữ liệu profile
            Profile profileData = profileService.getProfileData(profile).join();
            LoggerUtil.log(profileData.getName() + " Running current port: " + profileData.getRemote_debugging_address());
            logger.info(profileData.getName() + " Running current port: " + profileData.getRemote_debugging_address());
            logger.info("Running current Debugging: " + profileData.getDriver_path());
            LoggerUtil.log("Running current Debugging: " + profileData.getDriver_path());
            WebDriver driver = webDriverManager.initializeDriver(profileData);
            switch (type) {
                case 0:
                    profileAutomation.runAutomation(driver, pass, profile.getId());
                    break;
                case 1:
                    profileAutomation.clameBGT(driver, pass, profile.getId());
                    break;
                case 2:
                    profileAutomation.addPool(driver, pass, profile.getId());
                    break;
                default:
                    System.out.println("Khong co");

            }
            // Chạy automation cho profile

            return profileData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
