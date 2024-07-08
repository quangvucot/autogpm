package com.vdq.autogpm.automation;

import com.vdq.autogpm.api.Profile;
import com.vdq.autogpm.service.ProfileService;
import com.vdq.autogpm.webdriver.WebDriverManager;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.Callable;

public class ProfileTask implements Callable<Profile> {
    private Profile profile;
    private ProfileService profileService;
    private ProfileAutomation profileAutomation;

    public ProfileTask(Profile profile, ProfileService profileService, ProfileAutomation profileAutomation) {
        this.profile = profile;
        this.profileService = profileService;
        this.profileAutomation = profileAutomation;
    }


    @Override
    public Profile call() throws Exception {
        try {
            WebDriverManager webDriverManager = new WebDriverManager();
            // Lấy dữ liệu profile
            Profile profileData = profileService.getProfileData(profile).join();
            WebDriver driver = webDriverManager.initializeDriver(profileData);
            // Chạy automation cho profile
            System.out.println("Running automation for profile: " + profileData.getId());
            profileAutomation.runAutomation(driver);
            System.out.println("Finished automation for profile: " + profileData.getId());

            return profileData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
