package edu.umich.si.inteco.minuku.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.constants.Constants;
import edu.umich.si.inteco.minuku.util.PreferenceHelper;
import edu.umich.si.inteco.minuku.util.ScheduleAndSampleManager;

/**
 * Created by tsung on 2017/4/7.
 */

public class FirebaseManager {
    private String LOG_TAG = "FirebaseManager";

    private DatabaseReference databaseReference;
    private Context context;

    private String[] dataParams = {"_id", "others", "user_id", "data_type", "timestamp_hour", "records", "device_id"};
    private String email = "minukudata@gmail.com";
    private String password = "minukudata";

    // Firebase sign in
    private FirebaseAuth mAuth;

    public FirebaseManager(Context context) {
        this.context = context;

        // init remote database authentication
        if (null != MainActivity.getContext()){
            initFirebase();
        }
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(((MainActivity) MainActivity.getContext()), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "signInWithEmail:success");
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void uploadDocument(JSONObject document) {
        if (!checkIfWifi())
            return;

        try {
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[0]).setValue(document.getString(dataParams[0]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[1]).setValue(document.getString(dataParams[1]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[2]).setValue(document.getString(dataParams[2]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[3]).setValue(document.getString(dataParams[3]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[4]).setValue(document.getString(dataParams[4]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[5]).setValue(document.getString(dataParams[5]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[6]).setValue(MainActivity.wifiMacAddr + MainActivity.btMacAddr);

            setLastSeverSyncTime();
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    public void uploadShutdownDocument(JSONObject document) {
        if (!checkIfWifi())
            return;

        try {
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[0]).setValue(document.getString(dataParams[0]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[1]).setValue(document.getString(dataParams[1]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[2]).setValue(document.getString(dataParams[2]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[3]).setValue(document.getString(dataParams[3]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[4]).setValue(document.getString(dataParams[4]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[5]).setValue(document.getString(dataParams[5]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[6]).setValue(MainActivity.wifiMacAddr + MainActivity.btMacAddr);

            PreferenceHelper.setPreferenceBooleanValue(PreferenceHelper.IF_SHUT_DOWN_UPLOADED, true);
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    private boolean checkIfWifi() {
        boolean isWifi = false;

        Log.d(LOG_TAG, "[" + LOG_TAG + "]syncWithRemoteDatabase connectivity change");

        ConnectivityManager conMngr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = conMngr.getAllNetworks();
            NetworkInfo activeNetwork;

            for (Network network : networks) {
                activeNetwork = conMngr.getNetworkInfo(network);

                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifi = activeNetwork.isConnected();

                    if (isWifi) {
                        isWifi = true;
                    }
                }
            }

        } else {

            if (conMngr != null) {

                NetworkInfo[] info = conMngr.getAllNetworkInfo();
                NetworkInfo activeNetworkWifi = conMngr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo activeNetworkMobile = conMngr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                boolean isWiFi = activeNetworkWifi.getType() == ConnectivityManager.TYPE_WIFI;
                boolean isMobile = activeNetworkWifi.getType() == ConnectivityManager.TYPE_MOBILE;


                if (activeNetworkWifi != null) {

                    boolean isConnectedtoWifi = activeNetworkWifi != null &&
                            activeNetworkWifi.isConnected();
                    boolean isConnectedtoMobile = activeNetworkWifi != null &&
                            activeNetworkMobile.isConnected();


                    boolean isWifiAvailable = activeNetworkWifi.isAvailable();
                    boolean isMobileAvailable = activeNetworkMobile.isAvailable();

                    if (isWiFi) {
                        Log.d(LOG_TAG, "[" + LOG_TAG + "]syncWithRemoteDatabase connect to wifi");
                        isWifi = true;
                    }
                }
            }

        }

        return isWifi;
    }

    private void setLastSeverSyncTime() {
        // In format: 2016-09-27 12:00:00 -0400
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_NOW);
        try {
            Date lastSynhHour = sdf.parse(ScheduleAndSampleManager.getCurrentTimeHourString());
            PreferenceHelper.setPreferenceLongValue(PreferenceHelper.DATABASE_LAST_FIB_SYNC_TIME, lastSynhHour.getTime());
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }

    }
}
