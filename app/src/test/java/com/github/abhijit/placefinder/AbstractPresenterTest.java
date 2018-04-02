package com.github.abhijit.placefinder;

import com.github.abhijit.placefinder.base.BaseContract;
import com.github.abhijit.placefinder.data.scheduler.SchedulerProvider;
import com.github.abhijit.placefinder.data.scheduler.TestSchedulerProviderImpl;
import com.github.abhijit.placefinder.data.web.FakeWebService;
import com.github.abhijit.placefinder.data.web.WebService;

public class AbstractPresenterTest<P extends BaseContract.Presenter> {

    private WebService webService= new FakeWebService();

    private SchedulerProvider schedulerProvider = new TestSchedulerProviderImpl();


    protected WebService getWebService() {
        return webService;
    }

    protected SchedulerProvider getSchedulerProvider() {
        return schedulerProvider;
    }
}
