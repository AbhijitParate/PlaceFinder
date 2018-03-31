package com.github.abhijit.placefinder.ui;

import android.location.Location;
import android.support.annotation.StringRes;

import com.github.abhijit.placefinder.base.BaseContract;
import com.github.abhijit.placefinder.retrofit.models.Places;
import com.google.android.gms.maps.model.LatLng;

public interface MainContract {

    interface View extends BaseContract.View {
        void setPlaces(Places places);
        boolean hasLocationPermission();
        void requestLocationPermission();
        Location getLastKnownLocation();
        void showMessage(@StringRes int stringId);
    }

    interface Presenter extends BaseContract.Presenter {
        void getPlaces(LatLng latLng);
        void searchPlaces(String query, LatLng latLng);
        void checkForLocationPermission();
        void locationPermissionGranted();
        void locationPermissionDenied();
    }
}
