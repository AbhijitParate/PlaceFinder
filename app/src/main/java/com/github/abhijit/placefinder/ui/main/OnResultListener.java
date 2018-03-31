package com.github.abhijit.placefinder.ui.main;

import com.github.abhijit.placefinder.retrofit.models.Places;

public interface OnResultListener {
    void onResultReady(Places places);
}