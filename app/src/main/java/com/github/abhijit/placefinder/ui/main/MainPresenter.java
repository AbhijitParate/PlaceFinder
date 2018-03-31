package com.github.abhijit.placefinder.ui.main;

import android.location.Location;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.base.BasePresenter;
import com.github.abhijit.placefinder.data.scheduler.SchedulerProvider;
import com.github.abhijit.placefinder.data.web.WebService;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableMaybeObserver;

class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private static final int DEFAULT_RADIUS = 1000;

    MainPresenter(MainContract.View view, WebService client, SchedulerProvider scheduler) {
        super(view, client, scheduler);
        checkForLocationPermission();
    }

    public void subscribe() {
        super.subscribe();
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    @Override
    public void getPlaces(LatLng latLng) {
        addToDisposable(
                getPlacesClient().getPlaces(latLng.latitude, latLng.longitude, DEFAULT_RADIUS),
                new DisposableMaybeObserver<Places>() {
                    @Override
                    public void onSuccess(@NonNull Places places) {
                        if (places.getResults() != null) {
                            if (places.getResults().size() > 0) {
                                getView().setPlaces(places);
                            } else {
                                getView().showMessage(R.string.message_no_places_found);
                            }
                        } else {
                            getView().showMessage(R.string.message_failed_to_get_places);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().showMessage(R.string.message_failed_to_get_places);
                    }
                }
        );
    }

    @Override
    public void searchPlaces(String queryTerm, LatLng latLng) {
        addToDisposable(
                getPlacesClient().searchPlaces(latLng.latitude, latLng.longitude, DEFAULT_RADIUS, queryTerm),
                new DisposableMaybeObserver<Places>() {
                    @Override
                    public void onSuccess(@NonNull Places places) {
                        if (places.getResults() != null) {
                            if (places.getResults().size() > 0) {
                                getView().setPlaces(places);
                            } else {
                                getView().showMessage(R.string.message_no_places_found);
                            }
                        } else {
                            getView().showMessage(R.string.message_failed_to_get_places);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().showMessage(R.string.message_failed_to_get_places);
                    }
                }
        );
    }

    @Override
    public void checkForLocationPermission() {
        if (getView().hasLocationPermission()) {
            Location lastKnownLocation = getView().getLastKnownLocation();
            if (lastKnownLocation != null)
                getPlaces(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
        } else {
            getView().requestLocationPermission();
        }
    }

    @Override
    public void locationPermissionGranted() {
        Location lastKnownLocation = getView().getLastKnownLocation();
        getPlaces(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
    }

    @Override
    public void locationPermissionDenied() {
        getView().showMessage(R.string.message_permission_denied);
    }
}
