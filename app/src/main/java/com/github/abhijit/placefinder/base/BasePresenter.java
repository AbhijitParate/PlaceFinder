package com.github.abhijit.placefinder.base;

import com.github.abhijit.placefinder.data.placesclient.WebService;
import com.github.abhijit.placefinder.data.scheduler.SchedulerProvider;

import io.reactivex.Maybe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;

public class BasePresenter<V extends BaseContract.View> {

    private V view;
    private WebService placesClient;
    private SchedulerProvider schedulerProvider;
    private CompositeDisposable disposable;

    public BasePresenter(V view, WebService client, SchedulerProvider schedulerProvider) {
        this.view = view;
        this.placesClient = client;
        this.schedulerProvider = schedulerProvider;
        this.disposable = new CompositeDisposable();
    }

    public void subscribe(){

    }

    public void unsubscribe(){
        disposable.clear();
    }

    protected <T> void addToDisposable(Maybe<T> placesMaybe, DisposableMaybeObserver<T> disposableMaybeObserver) {
        disposable.add(placesMaybe
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribeWith(disposableMaybeObserver));
    }

    public WebService getPlacesClient() {
        return placesClient;
    }

    public V getView() {
        return view;
    }
}
