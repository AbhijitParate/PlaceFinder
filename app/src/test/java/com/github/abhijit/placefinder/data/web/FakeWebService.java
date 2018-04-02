package com.github.abhijit.placefinder.data.web;

import com.github.abhijit.placefinder.data.web.models.PlaceDetails;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.google.android.gms.maps.model.LatLng;

import org.mockito.Mockito;

import io.reactivex.Maybe;

public class FakeWebService implements WebService {


    // TODO: 4/1/18 Need fake data here

    @Override
    public Maybe<Places> getPlaces(LatLng latLng, int radius) {
        return Maybe.just(Mockito.any(Places.class));
    }

    @Override
    public Maybe<Places> searchPlaces(LatLng latLng, int radius, String query) {
        return Maybe.just(Mockito.any(Places.class));
    }

    @Override
    public Maybe<Places> getNextPlaces(String nextPageToken) {
        return Maybe.just(Mockito.any(Places.class));
    }

    @Override
    public Maybe<PlaceDetails> getPlaceDetails(String placeId) {
        return Maybe.just(Mockito.any(PlaceDetails.class));
    }
}
