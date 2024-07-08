package com.vdq.autogpm.service;


import com.vdq.autogpm.api.ApiClient;
import com.vdq.autogpm.api.ApiService;
import com.vdq.autogpm.api.Group;
import com.vdq.autogpm.api.Profile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class ProfileService {
    private ApiService apiService;
    private Map<String, Profile> profileMap;

    public ProfileService() {
        this.apiService = ApiClient.getApiService();
        this.profileMap = new HashMap<>();
    }

    public void fetchProfiles(Callback<ApiService.ApiResponse> callback) {
        apiService.getProfiles().enqueue(callback);
    }
    public void fetchProfilesByName(Callback<ApiService.ApiResponse> callback, String groupName) {
        apiService.getProfilesByGroup(groupName).enqueue(callback);
    }
    public void fetchGroups(Callback<ApiService.ApiResponseGroup> callback) {
        apiService.getGroup().enqueue(callback);
    }

    public void startProfile(String profileId, Callback<Profile> callback) {

    }

    public void saveProfile(Profile profile) {
        profileMap.put(profile.getId(), profile);
    }

    public void getProfile(Profile profile) {
        Call<ApiService.OpenProfileResponse> call = apiService.openProfile(profile.getId());
        call.enqueue(new Callback<ApiService.OpenProfileResponse>() {
            @Override
            public void onResponse(Call<ApiService.OpenProfileResponse> call, Response<ApiService.OpenProfileResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().success) {
                        Profile updatedProfile = response.body().data;
                        System.out.println(profile.getId());
                        profile.setRemote_debugging_address(updatedProfile.getRemote_debugging_address());
                        profile.setDriver_path(updatedProfile.getDriver_path());

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiService.OpenProfileResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    public CompletableFuture<Profile> getProfileData(Profile profile) {
        CompletableFuture<Profile> future = new CompletableFuture<>();
        Call<ApiService.OpenProfileResponse> call = apiService.openProfile(profile.getId());
        call.enqueue(new Callback<ApiService.OpenProfileResponse>() {
            @Override
            public void onResponse(Call<ApiService.OpenProfileResponse> call, Response<ApiService.OpenProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().success) {
                    Profile updatedProfile = response.body().data;
                    profile.setRemote_debugging_address(updatedProfile.getRemote_debugging_address());
                    profile.setDriver_path(updatedProfile.getDriver_path());
                    System.out.println("Opened profile: " + profile.getId());
                    future.complete(profile); // Đảm bảo hoàn thành CompletableFuture sau khi cập nhật
                } else {
                    future.completeExceptionally(new RuntimeException("Failed to open profile"));
                }
            }

            @Override
            public void onFailure(Call<ApiService.OpenProfileResponse> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

}
