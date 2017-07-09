package edu.umich.si.inteco.minuku.receivers;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
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
import edu.umich.si.inteco.minuku.data.FirebaseManager;
import edu.umich.si.inteco.minuku.util.PreferenceHelper;
import edu.umich.si.inteco.minuku.util.RecordingAndAnnotateManager;

/**
 * Created by Armuro on 7/11/14.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {

    /**
     * Tag for logging.
     */
    private static final String LOG_TAG = "ConnectivityChange";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(LOG_TAG, "[ConnectivityChangeReceiver]syncWithRemoteDatabase connectivity change");

        if (null == MainActivity.getContext()) {
            return;
        }

        ConnectivityManager conMngr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Network[] networks = conMngr.getAllNetworks();

            NetworkInfo activeNetwork;

            boolean isWifi = false;

            for (Network network : networks) {
                activeNetwork = conMngr.getNetworkInfo(network);

                if (null == activeNetwork)
                    return;

                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifi = activeNetwork.isConnected();

                    if (isWifi) {

                        final FirebaseManager firebaseMgr = new FirebaseManager(context);

                        if (!PreferenceHelper.getPreferenceBoolean(PreferenceHelper.IF_SHUT_DOWN_UPLOADED, true)) {
                            try {
                                JSONObject jsonData = new JSONObject(PreferenceHelper.getPreferenceString(PreferenceHelper.USER_SHUT_DOWN_LOG, "NA"));
                                firebaseMgr.uploadShutdownDocument(jsonData);

                                Log.d(LOG_TAG, "[ConnectivityChangeReceiver] device shut down action uploaded");
                            } catch (Exception e) {
                                Log.d(LOG_TAG, e.getMessage());
                            }

                        }


                        Log.d(LOG_TAG, "[ConnectivityChangeReceiver]syncWithRemoteDatabase connect to wifi");

                        //if we only submit the data over wifh. this should be configurable
//                        if (RemoteDBHelper.getSubmitDataOnlyOverWifi()) {
//                            Log.d(LOG_TAG, "[ConnectivityChangeReceiver]syncWithRemoteDatabase only submit over wifi");
//                            RemoteDBHelper.syncWithRemoteDatabase();
//
//                        }

                        new Thread(new Runnable() {
                            public void run() {
                                ArrayList<JSONObject> documents = RecordingAndAnnotateManager.getBackgroundRecordingDocuments(PreferenceHelper.getPreferenceLong(PreferenceHelper.DATABASE_LAST_FIB_SYNC_TIME, 0));

                                try {
                                    for (int i = 0; i < documents.size(); i++) {
                                        firebaseMgr.uploadDocument(documents.get(i));
                                    }
                                } catch (Exception e) {
                                    Log.d(LOG_TAG, e.getMessage());
                                }
                            }
                        }).start();


                    }
                }
            }


        } else {

            if (conMngr != null) {

                NetworkInfo[] info = conMngr.getAllNetworkInfo();
                NetworkInfo activeNetworkWifi = conMngr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo activeNetworkMobile = conMngr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


                if (null == activeNetworkWifi)
                    return;

                boolean isWiFi = activeNetworkWifi.getType() == ConnectivityManager.TYPE_WIFI;
                boolean isMobile = activeNetworkMobile.getType() == ConnectivityManager.TYPE_MOBILE;


                if (info != null) {

                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
//                            Log.d(LOG_TAG, "[ConnectivityChangeReceiver"+
//                                    " NETWORKNAME: " + anInfo.getTypeName());

                        }
                    }
                }

                if (activeNetworkWifi != null) {

                    boolean isConnectedtoWifi = activeNetworkWifi != null &&
                            activeNetworkWifi.isConnected();
                    boolean isConnectedtoMobile = activeNetworkWifi != null &&
                            activeNetworkMobile.isConnected();


                    boolean isWifiAvailable = activeNetworkWifi.isAvailable();
                    boolean isMobileAvailable = activeNetworkMobile.isAvailable();

                    if (isWiFi) {

                        Log.d(LOG_TAG, "[ConnectivityChangeReceiver]syncWithRemoteDatabase connect to wifi");

                        //if we only submit the data over wifh. this should be configurable
//                        if (RemoteDBHelper.getSubmitDataOnlyOverWifi()) {
//                            Log.d(LOG_TAG, "[ConnectivityChangeReceiver]syncWithRemoteDatabase only submit over wifi");
//                            RemoteDBHelper.syncWithRemoteDatabase();
//
//                        }


                    }
                }
            }

        }

    }


}
