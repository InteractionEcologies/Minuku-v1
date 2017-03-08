package edu.umich.si.inteco.minuku;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import edu.umich.si.inteco.minuku.fragments.EnterIdFragment;
import edu.umich.si.inteco.minuku.fragments.LoginFragment;

/**
 * Created by tsung on 2017/2/6.
 */

public class LoginActivity extends AppCompatActivity {
    private String TAG = "LoginActivity";
    public static Context context;

    // Function
    private String defaultFragment = LOGIN_FRAGMENT;
    private FragmentManager fragmentManager;
    public static final String LOGIN_FRAGMENT = "LoginFragment";
    public static final String ENTERID_FRAGMENT = "EnterIdFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        init();
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        setFragment(defaultFragment);
    }

    public void setFragment(String fragmentTag) {
        Fragment fragment = null;
        switch (fragmentTag) {
            case LOGIN_FRAGMENT:
                if (null != fragmentManager.findFragmentByTag(LOGIN_FRAGMENT)){
                    fragment = fragmentManager.findFragmentByTag(LOGIN_FRAGMENT);
                } else {
                    fragment = new LoginFragment();
                }
                break;
            case ENTERID_FRAGMENT:
                if (null != fragmentManager.findFragmentByTag(ENTERID_FRAGMENT)){
                    fragment = fragmentManager.findFragmentByTag(ENTERID_FRAGMENT);
                } else {
                    fragment = new EnterIdFragment();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static void closeKeyboard(Context context, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }
}
