package com.vdq.autogpm.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiService {

    @GET("profiles?per_page=1000")
    Call<ApiResponse> getProfiles();

    @GET("profiles/start/{id}?win_scale=0.6&win_size=620,800")
    Call<OpenProfileResponse> openProfile(@Path("id") String id);

    @GET("groups")
    Call<ApiResponseGroup> getGroup();

    @GET("profiles")
    Call<ApiResponse> getProfilesByGroup(@Query("group_id") String group);

    @GET("profiles/close/{id}")
    Call<OpenProfileResponse> closeProfile(@Path("id") String id);

    public class ApiResponse {
        public boolean success;
        public List<Profile> data;
        public String message;
    }

    public class OpenProfileResponse {
        public boolean success;
        public Profile data;
        public String message;

        //getters and setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public Profile getData() {
            return data;
        }

        public void setData(Profile data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }

    public class ProfileRequest {
        private String profileId;

        public ProfileRequest(String profileId) {
            this.profileId = profileId;
        }

        public String getProfileId() {
            return profileId;
        }

        public void setProfileId(String profileId) {
            this.profileId = profileId;
        }
    }

    public class ApiResponseGroup {
        public boolean success;
        public List<Group> data;
        public String message;
    }
}
