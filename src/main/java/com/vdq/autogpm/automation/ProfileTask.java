package com.vdq.autogpm.automation;

import com.vdq.autogpm.api.Profile;
import com.vdq.autogpm.service.ProfileService;
import com.vdq.autogpm.webdriver.WebDriverManager;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.Callable;
import java.util.logging.Logger;


public class ProfileTask implements Callable<Profile> {
    private Profile profile;
    private ProfileService profileService;
    private ProfileAutomation profileAutomation;
    private String pass;
    private static final Logger logger = Logger.getLogger(ProfileTask.class.getName());

    public ProfileTask(Profile profile, ProfileService profileService, ProfileAutomation profileAutomation, String okxPassword) {
        this.profile = profile;
        this.profileService = profileService;
        this.profileAutomation = profileAutomation;
        this.pass = okxPassword;
    }


    @Override
    public Profile call() throws Exception {
        logger.info("Bắt đầu khởi chạy profile" + profile.getName());

        try {
            WebDriverManager webDriverManager = new WebDriverManager();
            // Lấy dữ liệu profile
            Profile profileData = profileService.getProfileData(profile).join();
            logger.info(profileData.getName() + " Running current port: " + profileData.getRemote_debugging_address());
            logger.info("Running current Debugging: " + profileData.getDriver_path());
            WebDriver driver = webDriverManager.initializeDriver(profileData);
            // Chạy automation cho profile
            profileAutomation.runAutomation(driver, pass, profile.getId());
            return profileData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
