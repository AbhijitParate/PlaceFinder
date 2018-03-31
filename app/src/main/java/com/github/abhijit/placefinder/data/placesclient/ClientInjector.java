package com.github.abhijit.placefinder.data.placesclient;

public class ClientInjector {
    public static WebService getClient() {
        return WebServiceImpl.getClient();
    }
}