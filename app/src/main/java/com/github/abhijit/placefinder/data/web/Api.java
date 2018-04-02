package com.github.abhijit.placefinder.data.web;

import com.github.abhijit.placefinder.data.web.models.PlaceDetails;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.github.abhijit.placefinder.data.web.models.SearchPredictions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    String KEY = "AIzaSyAVQAdyM4Fh1QYCmuP7WyNJm_UaJxtN-kM";

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

    @GET("/maps/api/place/autocomplete/json?key="+ KEY)
    Call<SearchPredictions> searchNearByPlaces(@Query("input") String searchTerm);

}
