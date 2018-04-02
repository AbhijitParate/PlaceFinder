package com.github.abhijit.placefinder.ui.main.fragment.details;

import com.github.abhijit.placefinder.base.BaseContract;
import com.github.abhijit.placefinder.data.web.models.Photo;
import com.github.abhijit.placefinder.data.web.models.PlaceDetails;

import java.util.List;

interface PhotoDetailsContract {

    interface View extends BaseContract.View {
        void setPlaceTitle(String name);
        void setRating(Double rating);
        void setAddress(String address);
        void setContact(String phone);
        void setPhotos(List<Photo> photos);
        void setReviews(List<PlaceDetails.Result.Review> review);
    }

    interface Presenter extends BaseContract.Presenter {
        void getPlaceDetails();
    }

}
