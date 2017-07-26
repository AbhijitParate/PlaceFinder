package com.github.abhijit.placefinder.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitInstance {

    public static final String BASE_URL = "https://maps.googleapis.com";

    public static Retrofit RETROFIT = null;

    public static Retrofit getRetrofit() {
        if (RETROFIT == null) {
            RETROFIT = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return RETROFIT;
    }

    private RetroFitInstance() {

    }
}
