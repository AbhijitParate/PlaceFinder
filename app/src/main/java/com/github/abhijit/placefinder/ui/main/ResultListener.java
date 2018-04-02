package com.github.abhijit.placefinder.ui.main;

import com.github.abhijit.placefinder.data.web.models.Places;

import java.util.List;

public interface ResultListener {
    void setPlaces(List<Places.Result> places);
    void appendPlaces(List<Places.Result> places);
    void noMorePlaces();
}