package com.github.abhijit.placefinder.ui.presenter;

import com.github.abhijit.placefinder.base.BasePresenter;
import com.github.abhijit.placefinder.base.BaseView;
import com.github.abhijit.placefinder.retrofit.models.Places;

public interface Contract {

    interface View extends BaseView<Presenter> {
        double getLatitude();
        double getLongitude();
        int getRadius();
        String getQueryTerm();

        void setPlaces(Places places);
    }

    interface Presenter extends BasePresenter {
        void getPlaces();
        void searchPlaces();
    }
}
