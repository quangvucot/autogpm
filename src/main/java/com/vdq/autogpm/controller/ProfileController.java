package com.vdq.autogpm.controller;


import com.vdq.autogpm.api.ApiService;
import com.vdq.autogpm.api.Group;
import com.vdq.autogpm.api.Profile;
import com.vdq.autogpm.service.ProfileService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class ProfileController {
    private ProfileService profileService;
    private List<Profile> profileList;
    private static final Logger logger = Logger.getLogger(ProfileController.class.getName());

    public ProfileController() {
        this.profileService = new ProfileService();
    }

    public CompletableFuture<List<Profile>> fetchProfiles() {

        CompletableFuture<List<Profile>> future = new CompletableFuture<>();
        profileService.fetchProfiles(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful()) {
                    profileList = new ArrayList<>();
                    assert response.body() != null;
                    profileList = response.body().data;
                    future.complete(profileList);
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                logger.info("Vui lòng bật GPM");
                t.printStackTrace();
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    public CompletableFuture<List<Profile>> fetchProfilesByGroup(String groupName) {
        CompletableFuture<List<Profile>> future = new CompletableFuture<>();
        profileService.fetchProfilesByName(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful()) {
                    profileList = new ArrayList<>();
                    assert response.body() != null;
                    profileList = response.body().data;
                    future.complete(profileList);
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                logger.info("Vui lòng bật GPM");
                t.printStackTrace();
                future.completeExceptionally(t);
            }
        }, groupName);
        return future;
    }

    public CompletableFuture<List<Group>> getGroups() {
        CompletableFuture<List<Group>> future = new CompletableFuture<>();
        profileService.fetchGroups(new Callback<ApiService.ApiResponseGroup>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponseGroup> call, Response<ApiService.ApiResponseGroup> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<Group> groupArrayList = response.body().data;
                    future.complete(groupArrayList);
                } else {
                    future.completeExceptionally(new RuntimeException("Failed to fetch groups"));
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponseGroup> call, Throwable t) {
                logger.info("Vui lòng bật GPM");
                t.printStackTrace();
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    public void startProfile(Profile profile) {
        profileService.getProfile(profile);
    }

    public void openProfile(Profile profile) {
        profileService.getProfileData(profile);
    }


}
