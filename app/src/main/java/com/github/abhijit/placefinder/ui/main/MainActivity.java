package com.github.abhijit.placefinder.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.data.scheduler.SchedulerInjector;
import com.github.abhijit.placefinder.data.web.WebServiceInjector;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.github.abhijit.placefinder.ui.main.fragment.details.FragmentPlaceDetails;
import com.github.abhijit.placefinder.ui.main.fragment.list.FragmentList;
import com.github.abhijit.placefinder.ui.main.fragment.map.FragmentMap;
import com.github.abhijit.placefinder.ui.view.CustomSearchView;
import com.github.abhijit.placefinder.utils.PermissionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements
        MainContract.View,
        OnFragmentAttachListener,
        SearchView.OnQueryTextListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int CAMERA_MOVE_DELAY = 1000; // 1 Seconds delay
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 345;

    private Set<ResultListener> resultListeners = new HashSet<>();
    private SearchView searchView;
    private String currentView = FragmentList.TAG;

    private static MainContract.Presenter presenter;

    private Fragment fragmentList, fragmentMap;

    private static Handler handler = new Handler();
    private Runnable cameraMoveRunnable;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (presenter == null) {
            presenter = new MainPresenter(this, WebServiceInjector.getWebService(), SchedulerInjector.getScheduler());
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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

    @SuppressLint("MissingPermission")
    @Override
    public void getUserLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        Log.d(TAG, "onComplete() called with: Location = [" + location.toString() + "]");
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        presenter.getPlaces(latLng);
                        Toast.makeText(MainActivity.this, "Location found", Toast.LENGTH_SHORT).show();
                    }
                });
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
    public void showLocationPermissionDeniedMessage() {
        final Snackbar snackbar
                = Snackbar.make(findViewById(android.R.id.content), R.string.message_permission_denied, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationPermission();
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(ResourcesCompat.getColor(getResources(), android.R.color.white, getTheme()));
        snackbar.show();
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

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setOnQueryTextListener(this);

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
    public void onBackPressed() {
        if ((getSupportActionBar().getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) != 0){
            resetTitle();
            presenter.getPlaces(null);
        } else super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_list_view:
                switchToListView();
                return true;
            case R.id.action_map_view:
                switchToMapView();
                return true;
            case android.R.id.home:
                resetTitle();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetTitle() {
        setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
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
    public void setPlaces(List<Places.Result> places) {
        for (ResultListener l : resultListeners) {
            l.setPlaces(places);
        }
    }

    @Override
    public void appendPlaces(List<Places.Result> places) {
        for (ResultListener l : resultListeners) {
            l.appendPlaces(places);
        }
    }

    @Override
    public void notifyNoMorePlaces() {
        for (ResultListener l : resultListeners) {
            l.noMorePlaces();
        }
    }

    @Override
    public boolean hasLocationPermission() {
        return PermissionUtils.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO: 4/1/18 Retain last search term
        searchView.onActionViewCollapsed();
        setTitle(query.trim());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        presenter.searchPlaces(query.trim(), null);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onFragmentAttach(ResultListener listener) {
        this.resultListeners.add(listener);
    }

    @Override
    public void refreshPlaces() {
        String query = searchView.getQuery().toString();
        if (!TextUtils.isEmpty(query)) {
            presenter.searchPlaces(query, null);
        } else {
            presenter.getPlaces(null);
        }

    }

    @Override
    public void onMapMoved(final LatLng latLng) {
        handler.removeCallbacks(cameraMoveRunnable);
        cameraMoveRunnable = new Runnable() {
            @Override
            public void run() {
                String query = searchView.getQuery().toString();
                if (!TextUtils.isEmpty(query)) {
                    presenter.searchPlaces(query, latLng);
                } else {
                    presenter.getPlaces(latLng);
                }
            }
        };
        handler.postDelayed(cameraMoveRunnable, CAMERA_MOVE_DELAY);
    }

    @Override
    public void loadMorePlaces() {
        presenter.loadMorePlaces();
    }

    @Override
    public void showPlaceDetails(Places.Result result) {
        FragmentPlaceDetails.newInstance(result).show(getSupportFragmentManager());
    }
}
