package edu.umich.si.inteco.minuku.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.context.ContextStateManagers.PhoneStatusManager;
import edu.umich.si.inteco.minuku.data.FirebaseManager;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.util.PreferenceHelper;
import edu.umich.si.inteco.minuku.util.RecordingAndAnnotateManager;
import edu.umich.si.inteco.minuku.util.ScheduleAndSampleManager;

/**
 * Created by Tsung Wei Ho on 2017/3/11.
 */

public class UserPresentBroadcastReceiver extends BroadcastReceiver {

    private String LOG_TAG = "UsrPresentBR";
    private FirebaseManager firebaseMgr;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            if (null != MainActivity.getContext()) {

                // App shows up only when it's in family mode
                if (PreferenceHelper.getPreferenceBoolean(PreferenceHelper.USER_MODE_SELECTION, true)) {
                    UserSettingsDBHelper userSettingsDBHelper = new UserSettingsDBHelper(context);
                    userSettingsDBHelper.setAllUserUnSelected();
                    Intent intent1 = new Intent(context, MainActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }
            }
        }

        /*Device is shutting down. This is broadcast when the device
         * is being shut down (completely turned off, not sleeping)
         * */
        else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            PhoneStatusManager phoneStatusManager = new PhoneStatusManager(context);
            JSONObject shutDownDoc = RecordingAndAnnotateManager.generateShutDownActionDocument(phoneStatusManager.generateShutDownActionRecord());

            PreferenceHelper.setPreferenceStringValue(PreferenceHelper.USER_SHUT_DOWN_LOG, shutDownDoc.toString());
            PreferenceHelper.setPreferenceBooleanValue(PreferenceHelper.IF_SHUT_DOWN_UPLOADED, false);

            ConnectivityManager conMngr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                Network[] networks = conMngr.getAllNetworks();
                NetworkInfo activeNetwork;

                for (Network network : networks) {
                    activeNetwork = conMngr.getNetworkInfo(network);

                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        boolean isWifi = activeNetwork.isConnected();

                        if (isWifi) {
                            firebaseMgr = new FirebaseManager(context);
                            firebaseMgr.uploadDocument(shutDownDoc);

                            Log.d(LOG_TAG, "[UserPresentBroadcastReceiver] device shut down action uploaded");
                            PreferenceHelper.setPreferenceBooleanValue(PreferenceHelper.IF_SHUT_DOWN_UPLOADED, true);
                        }
                    }
                }
            }
            Log.d(LOG_TAG, "[UserPresentBroadcastReceiver] device shut down");
        }
    }
}
