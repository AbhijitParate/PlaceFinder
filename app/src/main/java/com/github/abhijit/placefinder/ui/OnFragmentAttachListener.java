package com.github.abhijit.placefinder.ui;

import com.google.android.gms.maps.model.LatLng;

public interface OnFragmentAttachListener {
    void onFragmentAttach(OnResultListener listener);
    void refreshPlaces();
    void onMapMoved(LatLng target);
}