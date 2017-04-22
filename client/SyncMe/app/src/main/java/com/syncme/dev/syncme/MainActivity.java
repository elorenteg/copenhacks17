package com.syncme.dev.syncme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends BasePermissionAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TWILIO_NUM = "629412318";
    private static final String TWILIO_KEY = "Twilio";
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public static final String INBOX = "content://sms/inbox";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        configurePermissions();
    }

    private void configurePermissions() {
        getPermissions(new RequestPermissionAction() {
            @Override
            public void permissionDenied() {
                // Call Back, when permission is Denied
                Log.e("MA", "Permission Denied");
            }

            @Override
            public void permissionGranted() {
                // Call Back, when permission is Granted
                Log.e("MA", "Permission Granted");
                readMessages();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void readMessages() {
        Cursor cursor = getContentResolver().query(Uri.parse(INBOX), null, null, null, null);
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

            openFragment();
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

    public void openFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, MainFragment.newInstance());
        ft.commit();
    }
}


