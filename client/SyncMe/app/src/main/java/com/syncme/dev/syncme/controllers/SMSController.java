package com.syncme.dev.syncme.controllers;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Calendar;
import java.util.Locale;

public class SMSController {
    public static final String INBOX = "content://sms/inbox";
    private static final String TWILIO_NUM = "629412318";
    private static final String TWILIO_KEY = "Twilio";
    private static Context mContext;
    private static SMSController instance;

    private SMSController(Context context) {
        mContext = context;
    }

    public static SMSController getInstance(Context ctx) {
        if (instance == null) {
            instance = new SMSController(ctx.getApplicationContext());
        }
        return instance;
    }

    public void readMessages() {
        Cursor cursor = mContext.getContentResolver().query(Uri.parse(INBOX), null, null, null, null);
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                int indAddr = cursor.getColumnIndex("address");
                int indBody = cursor.getColumnIndex("body");
                int indDate = cursor.getColumnIndex("date");
                int indDateSent = cursor.getColumnIndex("date_sent");
                if (cursor.getString(indBody).indexOf(TWILIO_KEY) >= 0) {
                    String msgData = cursor.getColumnName(indAddr) + ": " + cursor.getString(indAddr) + "\n" +
                            cursor.getColumnName(indDateSent) + ": " + getDate(cursor.getString(indDateSent)) + "\n" +
                            cursor.getColumnName(indDate) + ": " + getDate(cursor.getString(indDate)) + "\n" +
                            cursor.getColumnName(indBody) + ": " + cursor.getString(indBody);
                    Log.e("MA", msgData);
                }
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
    }

    private String getDate(String time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.valueOf(time));
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        return date;
    }
}
