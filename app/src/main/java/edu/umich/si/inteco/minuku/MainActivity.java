package edu.umich.si.inteco.minuku;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umich.si.inteco.minuku.constants.Constants;
import edu.umich.si.inteco.minuku.fragments.ProfileFragment;
import edu.umich.si.inteco.minuku.receivers.UserSwitchReceiver;
import edu.umich.si.inteco.minuku.services.MinukuMainService;
import edu.umich.si.inteco.minuku.util.PreferenceHelper;
import edu.umich.si.inteco.minuku.util.UserAccountManager;
import io.fabric.sdk.android.Fabric;

/**
 * Created by tsung on 2017/2/7.
 */

public class MainActivity extends FragmentActivity {

    private static final String LOG_TAG = "MainActivity";

    // Permissions
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    // View pager and fragments
    private static String PROFILE_FRAGMENT;
    private FragmentManager fragmentManager;
    public static Context context;
    private String currentPage;

    // Receiver
    private UserSwitchReceiver userSwitchReceiver;

    // Device info
    public static String wifiMacAddr;
    public static String btMacAddr;
    public static int screenWidth;
    public static int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());

        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        //permissions
        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }

        // Get screen info
        getScreenInfo();

        /**start the contextManager service**/
        if (!MinukuMainService.isServiceRunning()) {
            Log.d(LOG_TAG, "[test service running]  going start the probe service isServiceRunning:" + MinukuMainService.isServiceRunning());
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MinukuMainService.class);
            intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

            //start the Minuku service
            startService(intent);
        }


        /** Create the adapter that will return a fragment for each of the three primary sections
         // of the app.**/
        fragmentManager = getSupportFragmentManager();
        setFragment();
    }

    public void setFragment() {
        Fragment fragment;

        if (null != fragmentManager.findFragmentByTag(PROFILE_FRAGMENT)) {
            fragment = fragmentManager.findFragmentByTag(PROFILE_FRAGMENT);
        } else {
            fragment = new ProfileFragment();
        }

        currentPage = PROFILE_FRAGMENT;

        try {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.activity_main_container, fragment, PROFILE_FRAGMENT).addToBackStack(PROFILE_FRAGMENT).commit();
            transaction.commit();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    public static Context getContext() {
        return context;
    }

    private boolean checkAndRequestPermissions() {

        int permissionReadExternalStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteExternalStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(LOG_TAG, "[permission test]Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();

                // Initialize the map with both permissions
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            ) {
                        Log.d(LOG_TAG, "[permission test]all permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(LOG_TAG, "[permission test]Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                            showDialogOK("all Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    public void getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    wifiMacAddr = "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                wifiMacAddr = res1.toString();
            }
        } catch (Exception exception) {
            Log.d(LOG_TAG, exception.getMessage());
        }

//        btMacAddr = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
    }

    private void getScreenInfo() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    @Override
    public void onStart(){
        super.onStart();

        registerReceiver();

        PreferenceHelper.setPreferenceBooleanValue(PreferenceHelper.IF_USER_FOREGROUND, true);
        UserAccountManager userAccountManager = new UserAccountManager(this);
        Log.d("UserSwitchReceiver", userAccountManager.getCurrentUserNumber() + " create app");
    }

    @Override
    public void onResume() {
        super.onResume();
        getMacAddress();

        setFragment();
    }

    private void registerReceiver() {
        userSwitchReceiver = new UserSwitchReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_BACKGROUND);
        filter.addAction(Intent.ACTION_USER_FOREGROUND);
        registerReceiver(userSwitchReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();

        PreferenceHelper.setPreferenceStringValue(PreferenceHelper.USER_MAIN_PAGE, currentPage);
    }

    @Override
    public void onDestroy() {
        if (null != userSwitchReceiver) {
            unregisterReceiver(userSwitchReceiver);
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // Leave empty if you want nothing to happen on back press.
    }
}
