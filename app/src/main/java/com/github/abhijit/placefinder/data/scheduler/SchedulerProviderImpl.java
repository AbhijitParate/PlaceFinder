package com.github.abhijit.placefinder.data.scheduler;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProviderImpl implements SchedulerProvider {

    private static SchedulerProviderImpl INSTANCE;

    private SchedulerProviderImpl() {}

    public static SchedulerProviderImpl getInstance() {
        if (INSTANCE == null) INSTANCE = new SchedulerProviderImpl();
        return INSTANCE;
    }

    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
