package com.syncme.dev.syncme.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SMSController {
    public static final String INBOX = "content://sms/inbox";
    private static final String TWILIO_KEY = "SyncMe";
    private static Context mContext;
    private static SMSController instance;
    private OnReadMessagesCallback onReadMessagesCallback;

    private static int USERID_LENGTH = 24;

    private SMSController(Context context) {
        mContext = context;
    }

    public static SMSController getInstance(Context ctx) {
        if (instance == null) {
            instance = new SMSController(ctx.getApplicationContext());
        }
        return instance;
    }

    public void readMessages(OnReadMessagesCallback callback) {
        this.onReadMessagesCallback = callback;

        boolean hasMessage = false;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        Cursor cursor = mContext.getContentResolver().query(Uri.parse(INBOX), null, null, null, null);
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                int indAddr = cursor.getColumnIndex("address");
                int indBody = cursor.getColumnIndex("body");
                int indDate = cursor.getColumnIndex("date");
                int indDateSent = cursor.getColumnIndex("date_sent");
                if (cursor.getString(indBody).indexOf(TWILIO_KEY) >= 0) {
                    String date = getDate(cursor.getString(indDateSent));
                    long numDays = numsDaysBetweenDates(date, df.format(c.getTime()));
                    Log.e("SMS", "Days: " + numDays);
                    if (numDays <= 1) {
                        String msgData = cursor.getColumnName(indAddr) + ": " + cursor.getString(indAddr) + "\n" +
                                cursor.getColumnName(indDateSent) + ": " + getDate(cursor.getString(indDateSent)) + "\n" +
                                cursor.getColumnName(indDate) + ": " + getDate(cursor.getString(indDate)) + "\n" +
                                cursor.getColumnName(indBody) + ": " + cursor.getString(indBody);
                        Log.e("SMS", msgData);
                        hasMessage = true;
                    }
                }
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }

        onReadMessagesCallback.onReadMessages(hasMessage);
    }

    private String getDate(String time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.valueOf(time));
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        return date;
    }

    private long numsDaysBetweenDates(String str1, String str2) {
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        try {
            Date date1 = myFormat.parse(str1);
            Date date2 = myFormat.parse(str2);
            long diff = date2.getTime() - date1.getTime();
            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            return Math.abs(diff) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.MAX_VALUE;
    }

    public void readReceivedMessage(OnReadMessagesCallback callback, Intent intent) {
        this.onReadMessagesCallback = callback;

        final Bundle bundle = intent.getExtras();
        boolean isEventMessage = false;
        String userID = "";

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    Log.e("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                    //Toast.makeText(mContext, "senderNum: "+ senderNum + ", message: " + message,
                    //        Toast.LENGTH_LONG).show();

                    if (message.indexOf(TWILIO_KEY) >= 0) {
                        isEventMessage = true;
                        int posID = message.indexOf(TWILIO_KEY) + TWILIO_KEY.length();
                        userID = message.substring(posID, posID + USERID_LENGTH);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }
        onReadMessagesCallback.onReadReceivedMessage(isEventMessage, userID);
    }

    public interface OnReadMessagesCallback {
        void onReadMessages(boolean hasMessage);
        void onReadReceivedMessage(boolean isEventMessage, String userID);
    }
}
