package com.syncme.dev.syncme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

public class BasePermissionAppCompatActivity extends AppCompatActivity {

    private final static int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 123;

    private String[] permissions = {Manifest.permission.READ_SMS, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};

    RequestPermissionAction onPermissionCallBack;

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = 0;
            while (i < permissions.length) {
                String permission = permissions[i];
                if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED)
                    return false;
                ++i;
            }
        }
        return true;
    }

    public void getPermissions(RequestPermissionAction onPermissionCallBack) {
        this.onPermissionCallBack = onPermissionCallBack;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermissions()) {
                requestPermissions(permissions, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
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
            if (onPermissionCallBack != null)
                onPermissionCallBack.permissionGranted();

        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (onPermissionCallBack != null)
                onPermissionCallBack.permissionDenied();
        }
    }

    public interface RequestPermissionAction {
        void permissionDenied();

        void permissionGranted();
    }
}