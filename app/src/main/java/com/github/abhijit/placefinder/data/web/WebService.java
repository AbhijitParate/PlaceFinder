package com.github.abhijit.placefinder.data.web;

import com.github.abhijit.placefinder.data.web.models.PlaceDetails;
import com.github.abhijit.placefinder.data.web.models.Places;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface WebService {
    Maybe<Places> getPlaces(final double lat, final double lng, final int radius);
    Maybe<Places> searchPlaces(final double lat, final double lng, final int radius, final String query);
    Maybe<Places> getNextPlaces(final String nextPageToken);
    Maybe<PlaceDetails> getPlaceDetails(final String placeId);
}
