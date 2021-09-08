package com.viki.toth.barbequeTime.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.*;

import java.util.function.BiConsumer;

public class BarbequeLocation {
    public final int PERMISSION_ID = 44;
    private final String LOCATION = "Location";
    private FusedLocationProviderClient mFusedLocationClient;
    private final LocationManager locationManager;

    public BarbequeLocation(FusedLocationProviderClient mFusedLocationClient, LocationManager locationManager) {
        this.mFusedLocationClient = mFusedLocationClient;
        this.locationManager = locationManager;
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation(Activity activity, BiConsumer<Double, Double> callback) {
        Log.d(LOCATION, "Get last location");
        if (checkPermissions(activity)) {
            if (isLocationEnabled()) {
                Log.d(LOCATION, "Enabled");
                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData(activity, callback);
                    } else {
                        callback.accept(location.getLatitude(), location.getLongitude());
                    }
                });
            } else {
                Log.d(LOCATION, "Disabled");
                Toast.makeText(activity, "Please turn on your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
            }
        } else {
            Log.d(LOCATION, "Request permission!");
            requestPermissions(activity);
        }
    }

    public void onResume(Activity activity, BiConsumer<Double, Double> callback) {
        if (checkPermissions(activity)) {
            getLastLocation(activity, callback);
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(Activity activity, BiConsumer<Double, Double> callback) {
        Log.d(LOCATION, "requestNewLocationData");
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5)
                .setFastestInterval(0)
                .setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location mLastLocation = locationResult.getLastLocation();
                callback.accept(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            }
        }, Looper.myLooper());
    }

    private boolean checkPermissions(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
