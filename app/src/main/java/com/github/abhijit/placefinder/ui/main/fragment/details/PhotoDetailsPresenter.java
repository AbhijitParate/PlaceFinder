package com.github.abhijit.placefinder.ui.main.fragment.details;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.base.BasePresenter;
import com.github.abhijit.placefinder.data.scheduler.SchedulerProvider;
import com.github.abhijit.placefinder.data.web.WebService;
import com.github.abhijit.placefinder.data.web.models.PlaceDetails;

import io.reactivex.observers.DisposableMaybeObserver;

final class PhotoDetailsPresenter extends BasePresenter<PhotoDetailsContract.View>
        implements
        PhotoDetailsContract.Presenter {

    private final String placeId;

    PhotoDetailsPresenter(String placeId, PhotoDetailsContract.View view, WebService client, SchedulerProvider schedulerProvider) {
        super(view, client, schedulerProvider);
        this.placeId = placeId;
        getPlaceDetails();
    }

    @Override
    public void subscribe() {
        super.subscribe();
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    private void getPlaceDetails() {
        addToDisposable(
                getWebService().getPlaceDetails(placeId),
                new DisposableMaybeObserver<PlaceDetails>() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {
                        if ("OK".equalsIgnoreCase(placeDetails.getStatus())) {
                            PlaceDetails.Result result = placeDetails.getResult();
                            getView().setPlaceTitle(result.getName());
                            getView().setRating(result.getRating());
                            getView().setAddress(result.getAddress());
                            getView().setContact(result.getPhone());
                            getView().setPhotos(result.getPhotos());
                            getView().setReviews(result.getReview());
                        } else {
                            getView().showMessage(R.string.message_api_issue);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }
        );
    }
}
