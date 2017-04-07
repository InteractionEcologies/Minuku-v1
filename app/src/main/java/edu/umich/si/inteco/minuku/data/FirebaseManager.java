package edu.umich.si.inteco.minuku.data;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by tsung on 2017/4/7.
 */

public class FirebaseManager {

    private DatabaseReference databaseReference;

    public FirebaseManager(DatabaseReference databaseReference){
        this.databaseReference = databaseReference;
    }


}
