package com.github.abhijit.placefinder.ui.main;

import com.github.abhijit.placefinder.AbstractPresenterTest;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest extends AbstractPresenterTest<MainContract.Presenter> {


    @Mock
    private MainContract.View view;

    private MainContract.Presenter presenter;


    @Before
    public void init() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
        // Create presenter with mocks and test scheduler
        presenter = new MainPresenter(view, getWebService(), getSchedulerProvider());
    }

    @Test
    public void test_Permissions_Denied() {

        when(view.hasLocationPermission()).thenReturn(false);

        presenter.checkForLocationPermission();

        verify(view).hasLocationPermission();
        verify(view).requestLocationPermission();

        presenter.locationPermissionDenied();

        verify(view).showLocationPermissionDeniedMessage();


    }

    @Test
    public void test_Permissions_Granted() {

        when(view.hasLocationPermission()).thenReturn(true);

        presenter.checkForLocationPermission();

        verify(view).hasLocationPermission();

        presenter.locationPermissionGranted();

        verify(view).getUserLocation();


    }

//    @Test
    public void test_GetPlaces() {

        LatLng latLng = new LatLng(0,0);

        presenter.getPlaces(latLng);

        getWebService().getPlaces(latLng, MainPresenter.DEFAULT_RADIUS);

        verify(view).setPlaces(Mockito.<Places.Result>anyList());


    }

}