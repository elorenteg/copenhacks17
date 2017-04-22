package com.syncme.dev.syncme.controllers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationController implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = LocationController.class.getSimpleName();
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 15; // 15s
    private static Context mContext;
    private static LocationController instance;
    private boolean isLocationInProgress;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private OnNewLocationCallback onNewLocationCallback;

    private LocationController(Context context) {
        mContext = context;
        this.isLocationInProgress = false;
    }

    public static LocationController getInstance(Context ctx) {
        if (instance == null) {
            instance = new LocationController(ctx.getApplicationContext());
        }
        return instance;
    }

    public boolean checkLocationServiceAvailable() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            return !(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
        } else {
            return false;
        }
    }

    public boolean startLocation(OnNewLocationCallback callback) {
        if (!isLocationInProgress) {
            this.onNewLocationCallback = callback;
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();

                mLocationRequest = new LocationRequest();

                // Sets the desired interval for active location updates.
                mLocationRequest.setInterval(MIN_TIME_BW_UPDATES);

                // Sets the fastest rate for active location updates.
                mLocationRequest.setFastestInterval(MIN_TIME_BW_UPDATES / 2);

                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

                mGoogleApiClient.connect();

                isLocationInProgress = true;

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void stopLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                    mGoogleApiClient.disconnect();
                }
            }
        }
        isLocationInProgress = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e(TAG, "NEW location: " + location.getLatitude() + ", " + location.getLongitude());
            if (onNewLocationCallback != null) {
                onNewLocationCallback.onNewLocation(location);
            }
            stopLocation();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnected");
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectedSuspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectedFailed");
        Log.e(TAG, connectionResult.getErrorMessage() + " code: " + connectionResult.getErrorCode());
    }

    public interface OnNewLocationCallback {
        void onNewLocation(Location location);
    }
}