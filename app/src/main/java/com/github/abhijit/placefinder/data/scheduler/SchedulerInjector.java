package com.github.abhijit.placefinder.data.scheduler;


public class SchedulerInjector {

    public static SchedulerProviderImpl getScheduler() {
        return SchedulerProviderImpl.getInstance();
    }

}