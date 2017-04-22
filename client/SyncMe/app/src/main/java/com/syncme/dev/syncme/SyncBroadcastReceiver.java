package com.syncme.dev.syncme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

public class SyncBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SyncBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        Toast.makeText(context, "SyncMe has detected a key message!", Toast.LENGTH_LONG).show();
    }
}