package edu.umich.si.inteco.minuku;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

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

    // Function
    private String defaultFragment = OPENPAGE_FRAGMENT;
    private FragmentManager fragmentManager;

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
        }
        btMacAddr = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
