package com.github.abhijit.placefinder.utils;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.data.web.Api;

public class GlideUtils {

//    private static final String KEY = "AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=" + Api.KEY + "&photoreference=";

    public static void load(@Nullable String what, ImageView where) {
        Glide.with(where.getContext())
                .setDefaultRequestOptions(new GlideRequestOptions())
                .load(BASE_URL + what)
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .into(where);
    }

    @GlideModule
    private static final class GlideRequestOptions extends RequestOptions {
        private GlideRequestOptions() {
            placeholder(R.drawable.placeholder);
            error(R.drawable.placeholder);
            fallback(R.drawable.placeholder);
        }
    }
}
