package com.github.abhijit.placefinder.ui;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.retrofit.models.Places;
import com.github.abhijit.placefinder.ui.fragment.list.ListFragment;
import com.github.abhijit.placefinder.ui.fragment.map.MapFragment;
import com.github.abhijit.placefinder.ui.presenter.Contract;
import com.github.abhijit.placefinder.ui.presenter.MainPresenter;
import com.github.abhijit.placefinder.ui.view.CustomSearchView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity
        implements
        Contract.View,
        OnFragmentAttachListener,
        SearchView.OnQueryTextListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String FRAGMENT_TAG_MAP = "MAP";
    public static final String FRAGMENT_TAG_LIST = "LIST";
    public static final int LOCATION_PERMISSIONS_REQUEST_CODE = 345;

    private Location mLastLocation;
    private OnResultListener listener;
    private Contract.Presenter presenter;
    private Places places;
    private SearchView searchView;
    private String currentView = FRAGMENT_TAG_LIST;
    private FusedLocationProviderClient mFusedLocationClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (presenter == null) {
            presenter = new MainPresenter(this);
        }

        switchToListView();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.subscribe();
        getLastKnownLocation();
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLastLocation = location;
                presenter.getPlaces();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    public void requestLocationPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission. ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSIONS_REQUEST_CODE);

        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: GPS Permission granted");
                        getLastKnownLocation();
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "onRequestPermissionsResult: GPS Permission denied");
                    Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Location permission denied", Snackbar.LENGTH_INDEFINITE);
                    snackBar.setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestLocationPermission();
                        }
                    });
                    snackBar.show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.github.abhijit.placefinder.R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (CustomSearchView) menu.findItem(R.id.action_search).getActionView();

        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(this);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentView.equals(FRAGMENT_TAG_MAP))
            menu.removeItem(com.github.abhijit.placefinder.R.id.action_map_view);
        else if (currentView.equals(FRAGMENT_TAG_LIST))
            menu.removeItem(com.github.abhijit.placefinder.R.id.action_list_view);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.github.abhijit.placefinder.R.id.action_list_view) {
            switchToListView();
            return true;
        } else if (id == com.github.abhijit.placefinder.R.id.action_map_view){
            switchToMapView();
            return true;
        } else if (id == android.R.id.home) {
            setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void switchToMapView(){
        Fragment mapFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_MAP);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_enter_from_right, R.anim.slide_exit_to_left);
        ft.replace(com.github.abhijit.placefinder.R.id.fragment_container, mapFragment, FRAGMENT_TAG_MAP);
        ft.commit();
        currentView = FRAGMENT_TAG_MAP;
        invalidateOptionsMenu();
    }

    public void switchToListView() {
        Fragment listFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_LIST);
        if (listFragment == null){
            listFragment = ListFragment.newInstance();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_enter_from_left, R.anim.slide_exit_to_right);
        ft.replace(R.id.fragment_container, listFragment, FRAGMENT_TAG_LIST);
        ft.commit();
        currentView = FRAGMENT_TAG_LIST;
        invalidateOptionsMenu();
    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void makeToast(@StringRes int strId) {
        makeToast(getString(strId));
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public double getLatitude() {
        if (mLastLocation != null)
            return mLastLocation.getLatitude();
        else
            return 0;
    }

    @Override
    public double getLongitude() {
        if (mLastLocation != null)
            return mLastLocation.getLongitude();
        else
            return 0;
    }

    @Override
    public int getRadius() {
        return 1000;
    }

    @Override
    public String getQueryTerm() {
        if (searchView != null) {
            return searchView.getQuery().toString();
        } else return null;
    }

    @Override
    public void setPlaces(Places places) {
        this.places = places;
        listener.onResultReady(places);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        makeToast(query);
        searchView.onActionViewCollapsed();
        presenter.searchPlaces();
        setTitle(query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onFragmentAttach(OnResultListener listener) {
        this.listener = listener;
        listener.onResultReady(places);
    }

}
