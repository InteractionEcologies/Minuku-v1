package edu.umich.si.inteco.minuku.data;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import org.json.JSONObject;

/**
 * Created by tsung on 2017/4/7.
 */

public class FirebaseManager {
    private String LOG_TAG = "FirebaseManager";

    private DatabaseReference databaseReference;
    private String[] dataParams = {"_id", "others", "user_id", "data_type", "timestamp_hour", "records"};

    public FirebaseManager(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public void uploadDocument(JSONObject document) {
        try {
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[0]).setValue(document.getString(dataParams[0]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[1]).setValue(document.getString(dataParams[1]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[2]).setValue(document.getString(dataParams[2]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[3]).setValue(document.getString(dataParams[3]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[4]).setValue(document.getString(dataParams[4]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[5]).setValue(document.getString(dataParams[5]));
            Log.d(LOG_TAG, document.getString("_id"));
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }
}
