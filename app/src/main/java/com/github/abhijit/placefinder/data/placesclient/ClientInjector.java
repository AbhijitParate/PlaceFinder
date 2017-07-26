package com.github.abhijit.placefinder.data.placesclient;

public class ClientInjector {

    public static PlacesClient getClient() {
        return PlacesClientImpl.getClient();
    }
}