package com.github.abhijit.placefinder.ui.main;

import com.github.abhijit.placefinder.data.web.models.Places;
import com.google.android.gms.maps.model.LatLng;

public interface OnFragmentAttachListener {
    void onFragmentAttach(ResultListener listener);
    void refreshPlaces();
    void onMapMoved(LatLng newLatLng);
    void loadMorePlaces();
    void showPlaceDetails(Places.Result result);
}