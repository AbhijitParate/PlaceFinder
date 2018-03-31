package com.github.abhijit.placefinder.ui.main;

import com.github.abhijit.placefinder.data.web.models.Places;

public interface OnResultListener {
    void onResultReady(Places places);
}