package com.github.abhijit.placefinder.ui.presenter;

import com.github.abhijit.placefinder.data.placesclient.ClientInjector;
import com.github.abhijit.placefinder.data.placesclient.PlacesClient;
import com.github.abhijit.placefinder.data.scheduler.SchedulerInjector;
import com.github.abhijit.placefinder.data.scheduler.SchedulerProvider;
import com.github.abhijit.placefinder.retrofit.models.Places;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;

public class MainPresenter implements Contract.Presenter {

    public static final String TAG = MainPresenter.class.getSimpleName();

    private PlacesClient client;
    private SchedulerProvider schedulerProvider;
    private CompositeDisposable disposable;

    private Contract.View view;

    public MainPresenter(Contract.View view) {
        this.view = view;
        client = ClientInjector.getClient();
        schedulerProvider = SchedulerInjector.getScheduler();

        disposable = new CompositeDisposable();

        view.setPresenter(this);
    }

    public void subscribe() {
        getPlaces();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    @Override
    public void getPlaces() {
        disposable.add(
                client.getPlaces(view.getLatitude(), view.getLongitude(), view.getRadius())
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(new DisposableMaybeObserver<Places>(){
                            @Override
                            public void onSuccess(@NonNull Places places) {
                                view.setPlaces(places);
                            }

                            @Override
                            public void onComplete() {

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                view.makeToast(e.getMessage());
                            }
                        })
        );
    }

    @Override
    public void searchPlaces() {
        disposable.add(
                client.searchPlaces(view.getLatitude(), view.getLongitude(), view.getRadius(), view.getQueryTerm())
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribeWith(new DisposableMaybeObserver<Places>(){
                            @Override
                            public void onSuccess(@NonNull Places places) {
                                view.setPlaces(places);
                            }

                            @Override
                            public void onComplete() {

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                view.makeToast(e.getMessage());
                            }
                        })
        );
    }
}
