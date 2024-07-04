package com.vdq.autogpm.api;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

public class OkHttpClientSingleton {
	private static volatile OkHttpClient instance;

	private OkHttpClientSingleton() {

	}

	public static OkHttpClient getInstance() {
		if (instance == null) {
			synchronized (OkHttpClientSingleton.class) {
				if (instance == null) {
					instance = new OkHttpClient.Builder().connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
							.readTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS).build();
				}
			}
		}
		return instance;
	}
}
