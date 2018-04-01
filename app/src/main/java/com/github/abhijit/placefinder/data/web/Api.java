package com.github.abhijit.placefinder.data.web;

import com.github.abhijit.placefinder.data.web.models.PlaceDetails;
import com.github.abhijit.placefinder.data.web.models.Places;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String KEY = "AIzaSyAEQUEF212etm4tfVMvZlQPz4cS4qVuxGI";

    @GET("/maps/api/place/nearbysearch/json?key=" + KEY + "&type=restaurant&")
    Call<Places> getPlaces(@Query("location") String location,
                           @Query("radius") int radius);

    @GET("/maps/api/place/nearbysearch/json?key=" + KEY + "&type=restaurant&")
    Call<Places> searchPlaces(@Query("location") String location,
                              @Query("radius") int radius,
                              @Query("keyword") String query);

    @GET("/maps/api/place/nearbysearch/json?key=" + KEY)
    Call<Places> getNextPlaces(@Query("pagetoken") String nextPageToken);


    @GET("/maps/api/place/details/json?key=" + KEY)
    Call<PlaceDetails> getPlaceDetails(@Query("placeid") String nextPageToken);
}
