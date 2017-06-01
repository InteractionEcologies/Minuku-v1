package edu.umich.si.inteco.minuku.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;

/**
 * Created by Tsung Wei Ho on 2017/3/11.
 */

public class UserPresentBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            if (null != MainActivity.getContext()) {
                UserSettingsDBHelper userSettingsDBHelper = new UserSettingsDBHelper(context);
                userSettingsDBHelper.setAllUserUnSelected();
                Intent intent1 = new Intent(context, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        }

        /*Device is shutting down. This is broadcast when the device
         * is being shut down (completely turned off, not sleeping)
         * */
        else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {

        }
    }
}
