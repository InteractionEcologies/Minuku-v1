package edu.umich.si.inteco.minuku.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Process;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.MainActivity;

/**
 * Created by tsung on 2017/7/8.
 */

public class UserAccountManager {

    private Context context;

    public UserAccountManager(Context context) {
        this.context = context;
    }

    public String getCurrentUserNumber() {
        long userSerialNumber = 0;
        UserHandle uh = Process.myUserHandle();
        UserManager um = (UserManager) context.getSystemService(Context.USER_SERVICE);
        if (null != um) {
            userSerialNumber = um.getSerialNumberForUser(uh);
        }

        return userSerialNumber + "";
    }
}
