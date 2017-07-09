package edu.umich.si.inteco.minuku;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umich.si.inteco.minuku.fragments.AdultEnterIdFragment;
import edu.umich.si.inteco.minuku.fragments.EnterIdFragment;
import edu.umich.si.inteco.minuku.fragments.LoginFragment;
import edu.umich.si.inteco.minuku.fragments.OpenpageFragment;
import edu.umich.si.inteco.minuku.util.PreferenceHelper;

/**
 * Created by tsung on 2017/2/6.
 */

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LoginActivity";
    public static Context context;

    // Permissions
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    // Function
    private String defaultFragment = OPENPAGE_FRAGMENT;
    private FragmentManager fragmentManager;
    private String currentPage = OPENPAGE_FRAGMENT;

    // Fragments in LoginActivity
    public static final String LOGIN_FRAGMENT = "LoginFragment";
    public static final String ENTERID_FRAGMENT = "EnterIdFragment";
    public static final String ADENTERID_FRAGMENT = "AdultEnterIdFragment";
    public static final String OPENPAGE_FRAGMENT = "OpenpageFragment";

    // Unique id setup
    public static String wifiMacAddr;
    public static String btMacAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;

        init();
    }

    private void init() {
        getMacAddress();
        checkIfCompleteSetup();

        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }

        fragmentManager = getSupportFragmentManager();
        setFragment(defaultFragment);
    }

    private void checkIfCompleteSetup() {
        if (PreferenceHelper.getPreferenceBoolean(PreferenceHelper.USER_SETUP_COMPLETED, false)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    private boolean checkAndRequestPermissions() {

        int permissionAccounts = ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionAccounts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.GET_ACCOUNTS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d(TAG, "[permission test]Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();

                // Initialize the map with both permissions
                perms.put(android.Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "[permission test]all permission granted");
                        // process the normal flow
                        // else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "[permission test]Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.GET_ACCOUNTS)) {

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
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
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

    public void setFragment(String fragmentTag) {
        Fragment fragment = null;
        switch (fragmentTag) {
            case LOGIN_FRAGMENT:
                if (null != fragmentManager.findFragmentByTag(LOGIN_FRAGMENT)) {
                    fragment = fragmentManager.findFragmentByTag(LOGIN_FRAGMENT);
                } else {
                    fragment = new LoginFragment();
                }
                break;
            case ENTERID_FRAGMENT:
                if (null != fragmentManager.findFragmentByTag(ENTERID_FRAGMENT)) {
                    fragment = fragmentManager.findFragmentByTag(ENTERID_FRAGMENT);
                } else {
                    fragment = new EnterIdFragment();
                }
                break;
            case OPENPAGE_FRAGMENT:
                if (null != fragmentManager.findFragmentByTag(OPENPAGE_FRAGMENT)) {
                    fragment = fragmentManager.findFragmentByTag(OPENPAGE_FRAGMENT);
                } else {
                    fragment = new OpenpageFragment();
                }
                break;
            case ADENTERID_FRAGMENT:
                if (null != fragmentManager.findFragmentByTag(ADENTERID_FRAGMENT)) {
                    fragment = fragmentManager.findFragmentByTag(ADENTERID_FRAGMENT);
                } else {
                    fragment = new AdultEnterIdFragment();
                }
                break;
        }

        currentPage = fragmentTag;

        try {
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.activity_login_container, fragment, fragmentTag).addToBackStack(fragmentTag).commit();
            transaction.commit();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public static Context getContext() {
        return context;
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
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        btMacAddr = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
    }

    @Override
    protected void onResume() {
        super.onResume();

        setFragment(PreferenceHelper.getPreferenceString(PreferenceHelper.USER_SETUP_PAGE, OPENPAGE_FRAGMENT));
    }

    @Override
    protected void onPause() {
        super.onPause();

        PreferenceHelper.setPreferenceStringValue(PreferenceHelper.USER_SETUP_PAGE, currentPage);
    }
}
