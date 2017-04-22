package com.syncme.dev.syncme;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.syncme.dev.syncme.controllers.LocationController;

public class SyncBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SyncBroadcastReceiver";
    private static Context context;
    private static LocationManager locationManager;
    private String GpsProvider = LocationManager.GPS_PROVIDER;

    public static final int MIN_TIME_REQUEST = 5 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        Toast.makeText(context, "SyncMe has detected a key message!", Toast.LENGTH_LONG).show();

        this.context = context;
        checkLocation(context);
    }

    private void checkLocation(Context context) {
        Location location = LocationController.getInstance(context).getLastLocation();
        Toast.makeText(context, location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }
}