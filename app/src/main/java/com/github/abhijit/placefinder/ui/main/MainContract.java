package com.github.abhijit.placefinder.ui.main;

import android.location.Location;
import android.support.annotation.StringRes;

import com.github.abhijit.placefinder.base.BaseContract;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

interface MainContract {

    interface View extends BaseContract.View {
        boolean hasLocationPermission();
        void requestLocationPermission();
        Location getLastKnownLocation();
        void showMessage(@StringRes int stringId);
        void setPlaces(List<Places.Result> places);
        void appendPlaces(List<Places.Result> places);
        void notifyNoMorePlaces();
    }

    interface Presenter extends BaseContract.Presenter {
        void getPlaces(LatLng latLng);
        void searchPlaces(String query, LatLng latLng);
        void checkForLocationPermission();
        void locationPermissionGranted();
        void locationPermissionDenied();
        void loadMorePlaces();
    }
}
