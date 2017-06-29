package edu.umich.si.inteco.minuku.receivers;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.json.JSONObject;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.constants.UserIconReference;
import edu.umich.si.inteco.minuku.context.ContextStateManagers.PhoneStatusManager;
import edu.umich.si.inteco.minuku.data.FirebaseManager;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.model.User;
import edu.umich.si.inteco.minuku.model.Views.UserIcon;
import edu.umich.si.inteco.minuku.util.PreferenceHelper;
import edu.umich.si.inteco.minuku.util.RecordingAndAnnotateManager;

/**
 * Created by Tsung Wei Ho on 2017/3/11.
 */

public class UserPresentBroadcastReceiver extends BroadcastReceiver {

    private String LOG_TAG = "UsrPresentBR";
    private Context context;

    // Functions
    private FirebaseManager firebaseMgr;
    private UserSettingsDBHelper userSettingsDBHelper;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            // App shows up only when it's in family mode
            if (PreferenceHelper.getPreferenceBoolean(PreferenceHelper.USER_MODE_SELECTION, true)) {
                this.context = context;
                new TaskSetAllUserUnselected().execute();
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
                            firebaseMgr.uploadShutdownDocument(shutDownDoc);

                            Log.d(LOG_TAG, "[UserPresentBroadcastReceiver] device shut down action uploaded");
                        }
                    }
                }
            }

            Log.d(LOG_TAG, "[UserPresentBroadcastReceiver] device shut down");
        }
    }

    private class TaskSetAllUserUnselected extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            userSettingsDBHelper = new UserSettingsDBHelper(context);
        }

        @Override
        protected Boolean doInBackground(Void... v) {
            userSettingsDBHelper.setAllUserUnSelected();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
