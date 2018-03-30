package com.github.abhijit.placefinder.base;

import android.support.annotation.StringRes;

public interface BaseContract {

    interface View {
        void showMessage(@StringRes int stringId);
    }

    interface Presenter {
        void subscribe();
        void unsubscribe();
    }
}
