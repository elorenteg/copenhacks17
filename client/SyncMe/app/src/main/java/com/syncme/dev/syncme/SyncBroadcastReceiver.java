package com.syncme.dev.syncme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.syncme.dev.syncme.controllers.HttpController;
import com.syncme.dev.syncme.controllers.LocationController;
import com.syncme.dev.syncme.controllers.SMSController;

import org.json.JSONException;
import org.json.JSONObject;

public class SyncBroadcastReceiver extends BroadcastReceiver implements LocationController.OnNewLocationCallback,
 SMSController.OnReadMessagesCallback {

    private static final String TAG = "SyncBroadcastReceiver";
    private static final String USERID_LOCATION_URL = "http://6c6a0657.ngrok.io/user/";
    private Context context;

    private String userID = "null-user";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        Log.e(TAG, "Event received");
        Toast.makeText(context, "SyncMe has detected an event", Toast.LENGTH_LONG).show();

        //SMSController.getInstance(context).readMessages(this);
        SMSController.getInstance(context).readReceivedMessage(this, intent);
    }

    private void checkLocation(Context context) {
        LocationController.getInstance(context).startLocation(this);
    }

    @Override
    public void onNewLocation(Location location) {
        if (location != null) {
            String url = USERID_LOCATION_URL;
            url += userID;
            url += "/loc/" + location.getLatitude() + "," + location.getLongitude();

            Toast.makeText(context, url, Toast.LENGTH_LONG).show();
            Log.e(TAG, url);

            HttpController.getInstance(context).executePost(url);
        }
    }

    @Override
    public void onReadMessages(boolean hasMessage) {
        Log.e(TAG, "" + hasMessage);
        Toast.makeText(context, "HasMessage " + hasMessage, Toast.LENGTH_LONG).show();

        if (hasMessage) {
            Toast.makeText(context, "SyncMe has detected a key message!", Toast.LENGTH_LONG).show();
            //checkLocation(context);
        }
    }

    @Override
    public void onReadReceivedMessage(boolean isEventMessage, String userID) {
        Log.e(TAG, "" + isEventMessage);
        //Toast.makeText(context, "HasMessage " + isEventMessage, Toast.LENGTH_LONG).show();

        if (isEventMessage) {
            Toast.makeText(context, "SyncMe has detected a key message!", Toast.LENGTH_LONG).show();
            this.userID = userID;
            checkLocation(context);
        }
    }
}