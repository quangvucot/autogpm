package com.vdq.autogpm.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
	private static ApiClient instance;
	private static final String BASE_URL = "http://127.0.0.1:19995/api/v3/";
	private static ApiService apiService;
	private Retrofit retrofit;
	static OkHttpClient okHttpClient = OkHttpClientSingleton.getInstance();

	public static ApiService getApiService() {
		if (apiService == null) {
			Retrofit  retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
					.addConverterFactory(GsonConverterFactory.create()).build();
			apiService = retrofit.create(ApiService.class);
		}
		return apiService;
	}
	public static synchronized ApiClient getInstance() {
		if (instance == null) {
			instance = new ApiClient();
		}
		return instance;
	}
//	public static ApiService getApiService() {
//		return retrofit.create(ApiService.class);
//	}
}
