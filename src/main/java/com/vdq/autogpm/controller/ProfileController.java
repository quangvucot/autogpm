package com.vdq.autogpm.controller;


import com.vdq.autogpm.api.ApiService;
import com.vdq.autogpm.api.Profile;
import com.vdq.autogpm.service.ProfileService;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class ProfileController {
    private ProfileService profileService;
    private List<Profile> profileList;

    public ProfileController() {
        this.profileService = new ProfileService();
    }

    public List<Profile> fetchProfiles() {
        profileService.fetchProfiles(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful()) {
                    profileList = new ArrayList<>();
                    assert response.body() != null;
                    profileList = response.body().data;
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                System.out.println("Vui lòng bật GPM");
                t.printStackTrace();
            }
        });
        return profileList;
    }

    public void startProfile(Profile profile) {
        profileService.getProfile(profile);
    }
    public void openProfile(Profile profile) {
        profileService.getProfileData(profile);
    }


}
