package com.github.abhijit.placefinder.ui;

import android.location.Location;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.base.BasePresenter;
import com.github.abhijit.placefinder.data.placesclient.PlacesClient;
import com.github.abhijit.placefinder.data.scheduler.SchedulerProviderImpl;
import com.github.abhijit.placefinder.retrofit.models.Places;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableMaybeObserver;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    private static final String TAG = MainPresenter.class.getSimpleName();

    private static final int DEFAULT_RADIUS = 1000;

    private Location lastKnownLocation;

    MainPresenter(MainContract.View view, PlacesClient client, SchedulerProviderImpl scheduler) {
        super(view, client, scheduler);
    }

    public void subscribe() {
        checkForLocationPermission();
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    @Override
    public void getPlaces() {
        lastKnownLocation = view.getLastKnownLocation();
        addToDisposable(
                client.getPlaces(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), DEFAULT_RADIUS),
                new DisposableMaybeObserver<Places>() {
                    @Override
                    public void onSuccess(@NonNull Places places) {
                        if (places.getResults() != null) {
                            if (places.getResults().size() > 0) {
                                view.setPlaces(places);
                            } else {
                                view.showMessage(R.string.message_no_places_found);
                            }
                        } else {
                            view.showMessage(R.string.message_failed_to_get_places);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.showMessage(R.string.message_failed_to_get_places);
                    }
                }
        );
    }

    @Override
    public void searchPlaces(String queryTerm) {
        lastKnownLocation = view.getLastKnownLocation();
        addToDisposable(
                client.searchPlaces(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), DEFAULT_RADIUS, queryTerm),
                new DisposableMaybeObserver<Places>() {
                    @Override
                    public void onSuccess(@NonNull Places places) {
                        if (places.getResults() != null) {
                            if (places.getResults().size() > 0) {
                                view.setPlaces(places);
                            } else {
                                view.showMessage(R.string.message_no_places_found);
                            }
                        } else {
                            view.showMessage(R.string.message_failed_to_get_places);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.showMessage(R.string.message_failed_to_get_places);
                    }
                }
        );
    }

    @Override
    public void checkForLocationPermission() {
        if (view.hasLocationPermission()) {
            getPlaces();
        } else {
            view.requestLocationPermission();
        }
    }

    @Override
    public void locationPermissionGranted() {
        getPlaces();
    }

    @Override
    public void locationPermissionDenied() {
        view.showMessage(R.string.message_permission_denied);
    }
}
