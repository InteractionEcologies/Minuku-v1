package edu.umich.si.inteco.minuku.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import edu.umich.si.inteco.minuku.util.PreferenceHelper;
import edu.umich.si.inteco.minuku.util.UserAccountManager;

/**
 * Created by tsung on 2017/7/9.
 */

public class UserSwitchReceiver extends BroadcastReceiver {
    private static final String TAG = "UserSwitchReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        UserAccountManager userAccountManager = new UserAccountManager(context);
        boolean userSentBackground = intent.getAction().equals(Intent.ACTION_USER_BACKGROUND);
        boolean userSentForeground = intent.getAction().equals(Intent.ACTION_USER_FOREGROUND);
        Log.d(TAG, userAccountManager.getCurrentUserNumber() + "Switch received. User sent background = " + userSentBackground + "; User sent foreground = " + userSentForeground + ";");

        int user = intent.getExtras().getInt("android.intent.extra.user_handle");
        Log.d(TAG, userAccountManager.getCurrentUserNumber() + " user = " + user);

        PreferenceHelper.setPreferenceBooleanValue(PreferenceHelper.IF_USER_FOREGROUND, userSentForeground);
    }
}
