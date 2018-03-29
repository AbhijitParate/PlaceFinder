package com.github.abhijit.placefinder.utils;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.github.abhijit.placefinder.R;

public class GlideUtils {

    public static void load(@Nullable Object what, ImageView where) {
        Glide.with(where.getContext())
                .setDefaultRequestOptions(new GlideRequestOptions())
                .load(what)
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
