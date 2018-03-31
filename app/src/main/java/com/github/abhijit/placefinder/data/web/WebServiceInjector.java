package com.github.abhijit.placefinder.data.web;

public class WebServiceInjector {
    private static WebService webService;
    public static WebService getWebService() {
        return webService == null ? webService = new WebServiceImpl() : webService;
    }
}