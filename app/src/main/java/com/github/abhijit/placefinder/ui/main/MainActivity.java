package com.github.abhijit.placefinder.ui.main;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.data.placesclient.ClientInjector;
import com.github.abhijit.placefinder.data.scheduler.SchedulerInjector;
import com.github.abhijit.placefinder.retrofit.models.Places;
import com.github.abhijit.placefinder.ui.main.fragment.list.FragmentList;
import com.github.abhijit.placefinder.ui.main.fragment.map.FragmentMap;
import com.github.abhijit.placefinder.ui.view.CustomSearchView;
import com.github.abhijit.placefinder.utils.LocationUtils;
import com.github.abhijit.placefinder.utils.PermissionUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements
        MainContract.View,
        OnFragmentAttachListener,
        SearchView.OnQueryTextListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int CAMERA_MOVE_DELAY = 1000; // 1 Seconds delay
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 345;

    private Set<OnResultListener> resultListeners = new HashSet<>();
    private SearchView searchView;
    private String currentView = FragmentList.TAG;

    private static MainContract.Presenter presenter;

    private Fragment fragmentList, fragmentMap;

    private Handler handler = new Handler();
    private Runnable cameraMoveRunnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (presenter == null) {
            presenter = new MainPresenter(this, ClientInjector.getClient(), SchedulerInjector.getScheduler());
        }
        switchToListView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() called");
        super.onStart();
        presenter.subscribe();
    }

    @Override
    public Location getLastKnownLocation() {
        return LocationUtils.getLastKnownLocation(this);
    }

    @Override
    public void showMessage(int stringId) {
        Snackbar.make(findViewById(android.R.id.content), getString(stringId), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop() called");
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_LOCATION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.locationPermissionGranted();
            } else {
                presenter.locationPermissionDenied();
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
        if (currentView.equals(FragmentMap.TAG))
            menu.removeItem(R.id.action_map_view);
        else if (currentView.equals(FragmentList.TAG))
            menu.removeItem(R.id.action_list_view);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_list_view) {
            switchToListView();
            return true;
        } else if (id == R.id.action_map_view) {
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

    public void switchToMapView() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        addFragments(fragmentTransaction);
        fragmentTransaction.show(fragmentMap).hide(fragmentList).commit();
        currentView = FragmentMap.TAG;
        invalidateOptionsMenu();
    }

    public void switchToListView() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        addFragments(fragmentTransaction);
        fragmentTransaction.show(fragmentList).hide(fragmentMap).commit();
        currentView = FragmentList.TAG;
        invalidateOptionsMenu();
    }

    private void addFragments(FragmentTransaction fragmentTransaction) {
        fragmentMap = getSupportFragmentManager().findFragmentByTag(FragmentMap.TAG);
        if (fragmentMap == null) {
            fragmentMap = FragmentMap.newInstance();
            fragmentTransaction.add(R.id.fragment_container, fragmentMap, FragmentMap.TAG);
        }

        fragmentList = getSupportFragmentManager().findFragmentByTag(FragmentList.TAG);
        if (fragmentList == null) {
            fragmentList = FragmentList.newInstance();
            fragmentTransaction.add(R.id.fragment_container, fragmentList, FragmentList.TAG);
        }
    }

    @Override
    public void setPlaces(Places places) {
        for (OnResultListener l : resultListeners) {
            l.onResultReady(places);
        }
    }

    @Override
    public boolean hasLocationPermission() {
        return PermissionUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.onActionViewCollapsed();
        setTitle(query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Location lastKnownLocation = getLastKnownLocation();
        presenter.searchPlaces(query, new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onFragmentAttach(OnResultListener listener) {
        this.resultListeners.add(listener);
    }

    @Override
    public void refreshPlaces() {
        String query = searchView.getQuery().toString();
        if (!TextUtils.isEmpty(query)) {
            Location lastKnownLocation = getLastKnownLocation();
            presenter.searchPlaces(query, new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
        } else {
            Location lastKnownLocation = getLastKnownLocation();
            presenter.getPlaces(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
        }
    }

    @Override
    public void onMapMoved(final LatLng latLng) {
        handler.removeCallbacks(cameraMoveRunnable);
        cameraMoveRunnable = new Runnable() {
            @Override
            public void run() {
                String query = searchView.getQuery().toString();
                if (!TextUtils.isEmpty(query)){
                    presenter.searchPlaces(query, latLng);
                } else {
                    presenter.getPlaces(latLng);
                }
            }
        };
        handler.postDelayed(cameraMoveRunnable, CAMERA_MOVE_DELAY);
    }
}
