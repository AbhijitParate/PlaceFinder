package com.github.abhijit.placefinder.data.scheduler;


public class SchedulerInjector {
    private static SchedulerProvider schedulerProvider;
    public static SchedulerProvider getScheduler() {
        if (schedulerProvider == null) {
            schedulerProvider = new SchedulerProviderImpl();
        }
        return schedulerProvider;
    }
}