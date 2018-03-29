package com.github.abhijit.placefinder.ui.fragment.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.retrofit.models.Places;
import com.github.abhijit.placefinder.retrofit.models.Result;
import com.github.abhijit.placefinder.ui.OnFragmentAttachListener;
import com.github.abhijit.placefinder.ui.OnResultListener;
import com.github.abhijit.placefinder.ui.fragment.detail.DetailsFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapFragment extends Fragment
        implements
        OnResultListener,
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {

    @BindView(R.id.mapView)
    MapView mapView;

    private OnFragmentAttachListener listener;
    private List<Result> placeList;
    private Map<Marker, Result> markerMap;

    public static MapFragment newInstance() {

        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() { }

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
        listener = (OnFragmentAttachListener) getActivity();
        listener.onFragmentAttach(this);
        mapView.onStart();
        mapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mapView != null) mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
    }

    @Override
    public void onResultReady(Places places) {
        placeList = places.getResults();
        markerMap = new HashMap<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double lat = 0, lng = 0;
        for (Result result : placeList){

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

        LatLng latLng = new LatLng(lat/placeList.size(), lng/placeList.size());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15.0f).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Result result = markerMap.get(marker);
        DetailsFragment.newInstance(result).show(getChildFragmentManager(), "Details");
    }
}
