package edu.umich.si.inteco.minuku.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.util.PreferenceHelper;

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
        if (null != MainActivity.getContext())
            initFirebase();
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
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uploadDocument(JSONObject document) {
        try {
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[0]).setValue(document.getString(dataParams[0]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[1]).setValue(document.getString(dataParams[1]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[2]).setValue(document.getString(dataParams[2]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[3]).setValue(document.getString(dataParams[3]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[4]).setValue(document.getString(dataParams[4]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[5]).setValue(document.getString(dataParams[5]));
            databaseReference.child(document.getString(dataParams[0])).child(dataParams[6]).setValue(MainActivity.wifiMacAddr + MainActivity.btMacAddr);
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }
}
