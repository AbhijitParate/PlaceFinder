package com.github.abhijit.placefinder.ui.main;

import com.google.android.gms.maps.model.LatLng;

public interface OnFragmentAttachListener {
    void onFragmentAttach(ResultListener listener);
    void refreshPlaces();
    void onMapMoved(LatLng newLatLng);
    void loadMorePlaces(String nextPageToken);
}