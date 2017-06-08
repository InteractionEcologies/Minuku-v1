package edu.umich.si.inteco.minuku.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import edu.umich.si.inteco.minuku.LoginActivity;
import edu.umich.si.inteco.minuku.MainActivity;

/**
 * Created by Armuro on 7/16/14.
 */
public class PreferenceHelper {

    private static final String LOG_TAG = "PreferenceHelper";

    //preferndce name
    public static final String PACKAGE_NAME = "edu.umich.si.inteco.minuku";

    public static final String CONFIGURATIONS = ".CONFIGURATIONS";
    public static final String SHARED_PREFERENCE_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES_NAME";
    public static final String DEVICE_ID = PACKAGE_NAME + ".DEVICE_ID";
    public static final String USER_ID = PACKAGE_NAME + ".USER_ID";
    public static final String SCHEDULE_REQUEST_CODE = PACKAGE_NAME + ".REQUEST_CODE";
    public static final String DATABASE_LAST_SEVER_SYNC_TIME = PACKAGE_NAME + ".LAST_SERVER_SYNC_TIME";
    public static final String USER_INTERACTION_IN_APP_USE_REQUESTED = PACKAGE_NAME + ".USER_INTERACTION_IN_APP_USER";
    public static final String USER_SETUP_PAGE = PACKAGE_NAME + ".USER_SETUP_PAGE";
    public static final String USER_SETUP_COMPLETED = PACKAGE_NAME + ".USER_SETUP_COMPLETED";
    public static final String USER_MODE_SELECTION = PACKAGE_NAME + ".USER_MODE_SELECTION";
    public static final String USER_MAIN_PAGE = PACKAGE_NAME + ".USER_MAIN_PAGE";

    /***
     * Shared preference for storing context related information
     */
    public static final String CONTEXT_GEOFENCE_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";


    private static Context mContext;

    public PreferenceHelper(Context context) {

        this.mContext = context;
    }

    public static void setPreferenceStringValue(String property, String value) {

        Log.d(LOG_TAG, "[setPreferenceBooleanValue] saving " + value + " to " + property);

        if (getPreference() != null) {
            SharedPreferences.Editor editor = getPreference().edit();
            editor.putString(property, value);
            editor.apply();
        }

    }

    public static void setPreferenceBooleanValue(String property, boolean value) {

        Log.d(LOG_TAG, "[setPreferenceBooleanValue] saving " + value + " to " + property);

        if (getPreference() != null) {
            SharedPreferences.Editor editor = getPreference().edit();
            editor.putBoolean(property, value);
            editor.apply();
        }

    }

    public static void setPreferenceLongValue(String property, long value) {

        Log.d(LOG_TAG, "[setPreferenceBooleanValue] saving " + value + " to " + property);

        if (getPreference() != null) {
            SharedPreferences.Editor editor = getPreference().edit();
            editor.putLong(property, value);
            editor.apply();
        }

    }

    public static long getPreferenceLong(String property, long defaultValue) {

        Log.d(LOG_TAG, "[setPreferenceLongValue] getting values from " + property);


        if (getPreference() != null) {
            return getPreference().getLong(property, defaultValue);
        } else
            return defaultValue;
    }

    public static String getPreferenceString(String property, String defaultValue) {

        Log.d(LOG_TAG, "[setPreferenceBooleanValue] getting values from " + property);


        if (getPreference() != null) {
            return getPreference().getString(property, defaultValue);
        } else
            return defaultValue;
    }

    public static boolean getPreferenceBoolean(String property, boolean defaultValue) {

        if (getPreference() != null) {
            return getPreference().getBoolean(property, defaultValue);
        } else
            return defaultValue;
    }

    public static SharedPreferences getPreference() {

        if (mContext != null) {
            return mContext.getApplicationContext().getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        } else {
            if (null != MainActivity.getContext()) {
                mContext = MainActivity.getContext();
            } else if (null != LoginActivity.getContext()) {
                mContext = LoginActivity.getContext();
            }

            return mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }

    }

}
