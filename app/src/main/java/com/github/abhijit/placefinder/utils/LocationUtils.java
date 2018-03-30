package com.github.abhijit.placefinder.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.github.abhijit.placefinder.utils.PermissionUtils.hasPermission;

public class LocationUtils {

    private static final String TAG = LocationUtils.class.getName();

    public static Location getLastKnownLocation(Context context) {
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null &&
                (hasPermission(context, ACCESS_FINE_LOCATION) || hasPermission(context, ACCESS_COARSE_LOCATION))) {
            try {
                return locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            } catch (SecurityException e) {
                e.printStackTrace();
                Log.e(TAG, "getLastKnownLocation: SecurityException occurred");
            }
        }
        return null;
    }
}
