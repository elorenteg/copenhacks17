package com.syncme.dev.syncme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

public class BasePermissionAppCompatActivity extends AppCompatActivity {

    private final static String APP_NAME = "APP_NAME";
    private final static int REQUEST_READ_SMS_PERMISSION = 3004;
    public final static String READ_SMS_PERMISSION_NOT_GRANTED = "Please allow " + APP_NAME + " to access your SMS from setting";

    RequestPermissionAction onPermissionCallBack;

    private boolean checkReadSMSPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void getReadSMSPermission(RequestPermissionAction onPermissionCallBack) {
        this.onPermissionCallBack = onPermissionCallBack;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkReadSMSPermission()) {
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, REQUEST_READ_SMS_PERMISSION);
                return;
            }
        }
        if (onPermissionCallBack != null)
            onPermissionCallBack.permissionGranted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (REQUEST_READ_SMS_PERMISSION == requestCode) {
                // TODO Request Granted for READ_SMS.
                System.out.println("REQUEST_READ_SMS_PERMISSION Permission Granted");
            }
            if (onPermissionCallBack != null)
                onPermissionCallBack.permissionGranted();

        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (REQUEST_READ_SMS_PERMISSION == requestCode) {
                // TODO REQUEST_READ_SMS_PERMISSION Permission is not Granted.
                // TODO Request Not Granted.


                // This code is for get permission from setting.
                //final Intent i = new Intent();
                //i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                //i.addCategory(Intent.CATEGORY_DEFAULT);
                //i.setData(Uri.parse("package:" + getPackageName()));
                //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                //i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                //startActivity(i);
            }
            if (onPermissionCallBack != null)
                onPermissionCallBack.permissionDenied();
        }
    }

    public interface RequestPermissionAction {
        void permissionDenied();

        void permissionGranted();
    }

}