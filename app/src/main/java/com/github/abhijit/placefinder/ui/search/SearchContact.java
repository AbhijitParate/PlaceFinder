package com.github.abhijit.placefinder.ui.search;

import com.github.abhijit.placefinder.base.BaseContract;
import com.github.abhijit.placefinder.data.web.models.SearchPredictions;

import java.util.List;

interface SearchContact {
    interface View extends BaseContract.View {
        void setPredictions(List<SearchPredictions.Prediction> predictions);
        void showNearbyPlaces(SearchPredictions.Prediction prediction);
    }

    interface Presenter extends BaseContract.Presenter{
        void getPredictions(String s);
        void onPredictionClicked(SearchPredictions.Prediction prediction);
    }
}
