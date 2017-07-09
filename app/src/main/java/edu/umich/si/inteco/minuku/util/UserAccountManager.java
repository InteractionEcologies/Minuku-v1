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

    public String getCurrentAccount() {
        ArrayList playAccounts = new ArrayList();
        Account[] accounts = AccountManager.get(context).getAccounts();
        if (accounts != null && accounts.length > 0) {
            for (Account account : accounts) {
                Log.d("asdasdas", account.name + "");
                if (account.type.equals("com.google")) {
                    playAccounts.add(account.name);
                }
            }
        }

        Log.d("asdasdas", playAccounts + "");

        if (playAccounts.size() > 0) {
            return playAccounts.get(0).toString();
        } else {
            return "Can't retrieve user account info.";
        }
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
