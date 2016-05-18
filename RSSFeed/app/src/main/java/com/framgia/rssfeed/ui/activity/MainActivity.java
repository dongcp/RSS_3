package com.framgia.rssfeed.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.framgia.rssfeed.ui.base.BaseActivity;
import com.framgia.rssfeed.ui.base.BaseFragment;
import com.framgia.rssfeed.ui.fragment.HomeFragment;

public class MainActivity extends BaseActivity {

    public final static int MY_PERMISSIONS_ACCESS_STORAGE = 1;

    @Override
    protected BaseFragment getFragment() {
        return new HomeFragment();
    }

    @Override
    protected void onCreateContentView() {
        int writePermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (writePermissionCheck != PackageManager.PERMISSION_GRANTED
                || readPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_ACCESS_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                break;
            }
        }
    }
}
