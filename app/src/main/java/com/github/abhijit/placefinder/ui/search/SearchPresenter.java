package com.github.abhijit.placefinder.ui.search;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.base.BasePresenter;
import com.github.abhijit.placefinder.data.scheduler.SchedulerProvider;
import com.github.abhijit.placefinder.data.web.WebService;
import com.github.abhijit.placefinder.data.web.models.SearchPredictions;

import io.reactivex.observers.DisposableMaybeObserver;

class SearchPresenter extends BasePresenter<SearchContact.View>
        implements
        SearchContact.Presenter {

    SearchPresenter(SearchContact.View view, WebService client, SchedulerProvider schedulerProvider) {
        super(view, client, schedulerProvider);
    }


    @Override
    public void getPredictions(String s) {
        addToDisposable(
                getWebService().getSearchResults(s),
                new DisposableMaybeObserver<SearchPredictions>() {
                    @Override
                    public void onSuccess(SearchPredictions searchPredictions) {
                        getView().setPredictions(searchPredictions.getPredictions());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showMessage(R.string.message_failed_to_get_predictions);
                    }

                    @Override
                    public void onComplete() {

                    }
                }
        );
    }

    @Override
    public void onPredictionClicked(SearchPredictions.Prediction prediction) {
        getView().showNearbyPlaces(prediction);
    }
}
