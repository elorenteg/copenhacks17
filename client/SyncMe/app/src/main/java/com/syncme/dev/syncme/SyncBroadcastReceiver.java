package com.syncme.dev.syncme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.syncme.dev.syncme.controllers.LocationController;

public class SyncBroadcastReceiver extends BroadcastReceiver implements LocationController.OnNewLocationCallback {

    public static final int MIN_TIME_REQUEST = 5 * 1000;
    private static final String TAG = "SyncBroadcastReceiver";
    private String GpsProvider = LocationManager.GPS_PROVIDER;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME) + "\n");
        Toast.makeText(context, "SyncMe has detected a key message!", Toast.LENGTH_LONG).show();

        checkLocation(context);
    }

    private void checkLocation(Context context) {
        LocationController.getInstance(context).startLocation(this);
    }

    @Override
    public void onNewLocation(Location location) {
        if (location != null) {
            Toast.makeText(context, location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
        }
    }
}