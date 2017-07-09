package edu.umich.si.inteco.minuku.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.constants.Constants;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.services.MinukuMainService;
import edu.umich.si.inteco.minuku.util.LogManager;

public class BootCompleteReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "BootCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

            Log.d(LOG_TAG, "Successfully receive reboot request");

            /**start the contextManager service**/
            if (!MinukuMainService.isServiceRunning()) {
                Log.d(LOG_TAG, "[test service running]  going start the probe service isServiceRunning:" + MinukuMainService.isServiceRunning());
                Intent sintent = new Intent();
                sintent.setClass(context, MinukuMainService.class);
                sintent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);

                //start the Minuku service
                context.startService(intent);
            }

            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            UserSettingsDBHelper userSettingsDBHelper = new UserSettingsDBHelper(context);
            userSettingsDBHelper.setAllUserUnSelected();
            context.startActivity(intent1);

            LogManager.log(LogManager.LOG_TYPE_SYSTEM_LOG,
                    LogManager.LOG_TAG_ALARM_RECEIVED,
                    "Alarm Received:\t" + "BootComplete" + "\t" + "restart Service");
        }

    }

}
