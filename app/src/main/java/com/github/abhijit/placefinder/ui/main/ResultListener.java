package com.github.abhijit.placefinder.ui.main;

import com.github.abhijit.placefinder.data.web.models.Places;

public interface ResultListener {
    void setPlaces(Places places);
    void appendPlaces(Places places);
    void noMorePlaces();
}