package com.github.abhijit.placefinder;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    private static Application self;

    @Override
    public void onCreate() {
        self = this;
        super.onCreate();
    }

    public static Context getContext(){
        return self.getApplicationContext();
    }
}
