package com.github.abhijit.placefinder.data.placesclient;

import com.github.abhijit.placefinder.retrofit.models.Places;

import io.reactivex.Maybe;

public interface WebService {
    Maybe<Places> getPlaces(final double lat, final double lng, final int radius);
    Maybe<Places> searchPlaces(final double lat, final double lng, final int radius, final String query);
}
