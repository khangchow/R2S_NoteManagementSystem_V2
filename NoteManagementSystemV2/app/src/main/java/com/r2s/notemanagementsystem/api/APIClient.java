package com.r2s.notemanagementsystem.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static String baseURL = "https://api.dmq.biz/";
    private static Retrofit retrofit;

    public static Retrofit getCate() {
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(APIClient.baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
