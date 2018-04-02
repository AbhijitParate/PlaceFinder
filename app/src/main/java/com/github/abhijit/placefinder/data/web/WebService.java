package com.github.abhijit.placefinder.data.web;

import com.github.abhijit.placefinder.data.web.models.PlaceDetails;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.github.abhijit.placefinder.data.web.models.SearchPredictions;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Maybe;

public interface WebService {
    Maybe<Places> getPlaces(LatLng latLng, int radius);
    Maybe<Places> searchPlaces(LatLng latLng, int radius, final String query);
    Maybe<Places> getNextPlaces(final String nextPageToken);
    Maybe<PlaceDetails> getPlaceDetails(final String placeId);
    Maybe<SearchPredictions> getSearchResults(final String searchTerm);
}
