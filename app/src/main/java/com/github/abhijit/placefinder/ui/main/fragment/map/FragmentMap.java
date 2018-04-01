package com.github.abhijit.placefinder.ui.main.fragment.map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.github.abhijit.placefinder.ui.main.OnFragmentAttachListener;
import com.github.abhijit.placefinder.ui.main.ResultListener;
import com.github.abhijit.placefinder.ui.main.fragment.details.FragmentPlaceDetails;
import com.github.abhijit.placefinder.utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class FragmentMap extends Fragment
        implements
        ResultListener,
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {

    public static final String TAG = FragmentMap.class.getName();

    @BindView(R.id.mapView)
    MapView mapView;

    private boolean isMapInitialized = false;
    private List<Places.Result> placeList = new ArrayList<>();
    private Map<Marker, Places.Result> markerMap = new HashMap<>();

    public static FragmentMap newInstance() {
        Bundle args = new Bundle();
        FragmentMap fragment = new FragmentMap();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMap() { /* empty public constructor required by Android */ }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, v);
        mapView.onCreate(savedInstanceState);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        ((OnFragmentAttachListener) getActivity()).onFragmentAttach(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void setPlaces(Places places) {
        placeList.clear();
        placeList.addAll(places.getResults());
        markerMap.clear();
        mapView.getMapAsync(this);
    }

    @Override
    public void appendPlaces(Places places) {
        // Nothing goes here
    }

    @Override
    public void noMorePlaces() {
        // Nothing goes here
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        double lat = 0, lng = 0;

        googleMap.clear();

        for (Places.Result result : placeList) {

            lat += result.getGeometry().getLocation().getLat();
            lng += result.getGeometry().getLocation().getLng();

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(
                            result.getGeometry().getLocation().getLat(),
                            result.getGeometry().getLocation().getLng())
                    )
                    .title(result.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            Marker m = googleMap.addMarker(markerOptions);
            markerMap.put(m, result);
        }

        if (!isMapInitialized) {
            LatLng latLng = new LatLng(lat / placeList.size(), lng / placeList.size());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15.0f).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
        if (PermissionUtils.hasPermission(getContext(), ACCESS_FINE_LOCATION)
                || PermissionUtils.hasPermission(getContext(), ACCESS_COARSE_LOCATION)) {
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                ((OnFragmentAttachListener) getActivity()).onMapMoved(googleMap.getCameraPosition().target);
            }
        });
        isMapInitialized = true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Places.Result result = markerMap.get(marker);
        FragmentPlaceDetails.newInstance(result).show(getChildFragmentManager());
    }
}
