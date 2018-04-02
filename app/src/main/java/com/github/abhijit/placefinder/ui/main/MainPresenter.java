package com.github.abhijit.placefinder.ui.main;

import android.text.TextUtils;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.base.BasePresenter;
import com.github.abhijit.placefinder.data.scheduler.SchedulerProvider;
import com.github.abhijit.placefinder.data.web.WebService;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableMaybeObserver;

class MainPresenter extends BasePresenter<MainContract.View>
        implements
        MainContract.Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    static final int DEFAULT_RADIUS = 5000;
    private LatLng lastLatLng;
    private String nextPageToken;

    MainPresenter(MainContract.View view, WebService client, SchedulerProvider scheduler) {
        super(view, client, scheduler);
    }

    @Override
    public void getPlaces(LatLng latLng) {
        if (latLng != null) lastLatLng = latLng;
        addToDisposable(
                getWebService().getPlaces(lastLatLng, DEFAULT_RADIUS),
                new PlaceObserver() {
                    @Override
                    void onPlacesReady(List<Places.Result> places) {
                        getView().setPlaces(places);
                    }
                });
    }

    @Override
    public void searchPlaces(String queryTerm, LatLng latLng) {
        if (latLng != null) lastLatLng = latLng;
        addToDisposable(
                getWebService().searchPlaces(lastLatLng, DEFAULT_RADIUS, queryTerm),
                new PlaceObserver() {
                    @Override
                    void onPlacesReady(List<Places.Result> places) {
                        getView().setPlaces(places);
                    }
                });
    }

    @Override
    public void checkForLocationPermission() {
        if (getView().hasLocationPermission()) {
            getView().getUserLocation();
        } else {
            getView().requestLocationPermission();
        }
    }

    @Override
    public void locationPermissionGranted() {
        getView().getUserLocation();
    }

    @Override
    public void locationPermissionDenied() {
        getView().showLocationPermissionDeniedMessage();
    }

    @Override
    public void loadMorePlaces() {
        // Technically no need to put this check, but its a safeguard
        if (!TextUtils.isEmpty(nextPageToken)) {
            addToDisposable(
                    getWebService().getNextPlaces(nextPageToken),
                    new PlaceObserver() {
                        @Override
                        void onPlacesReady(List<Places.Result> places) {
                            getView().appendPlaces(places);
                        }
                    }
            );
        }
    }

    private void updateNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
        if (TextUtils.isEmpty(nextPageToken)){
            getView().notifyNoMorePlaces();
        }
    }

    private abstract class PlaceObserver extends DisposableMaybeObserver<Places> {
        @Override
        public void onSuccess(@NonNull Places places) {
            if (places.getResults().size() > 0) {
                onPlacesReady(places.getResults());
            } else {
                getView().showMessage(R.string.message_no_places_found);
            }
            updateNextPageToken(places.getNextPageToken());
        }

        abstract void onPlacesReady(@NonNull List<Places.Result> places);

        @Override
        public void onComplete() {
            getView().notifyNoMorePlaces();
        }

        @Override
        public void onError(@NonNull Throwable e) {
            getView().showMessage(R.string.message_failed_to_get_places);
        }
    }
}
