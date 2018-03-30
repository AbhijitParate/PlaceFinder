package com.github.abhijit.placefinder.retrofit;

import com.github.abhijit.placefinder.retrofit.models.Places;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("/maps/api/place/nearbysearch/json?key=AIzaSyDQ6kfEwxB2qKw_Pd5p0qladlSlCO0xr3w&type=restaurant&")
    Call<Places> getPlaces(@Query("location") String location,
                           @Query("radius") int radius);

    @GET("/maps/api/place/nearbysearch/json?key=AIzaSyDQ6kfEwxB2qKw_Pd5p0qladlSlCO0xr3w&type=restaurant&")
    Call<Places> searchPlaces(@Query("location") String location,
                           @Query("radius") int radius,
                           @Query("keyword") String query);
}
