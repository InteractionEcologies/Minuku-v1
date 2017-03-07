package edu.umich.si.inteco.minuku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.umich.si.inteco.minuku.adapters.UserIconAdapter;
import edu.umich.si.inteco.minuku.constants.UserIconReference;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.model.User;

/**
 * Created by tsung on 2017/2/6.
 */

public class LoginActivity extends Activity {

    public static Context context;
    private UserSettingsDBHelper userSettingsDBHelper;
    private int limitOfUsers = 8;
    // UI widgets
    private Button btnLogin, btnAddUser;
    private ListView lvUsers;
    private UserIconAdapter userIconAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        init();
    }

    private void init() {
        userSettingsDBHelper = new UserSettingsDBHelper(this);
        btnLogin = (Button) findViewById(R.id.activity_login_btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundMail.newBuilder(LoginActivity.this)
                        .withUsername("tsungwei50521@gmail.com")
                        .withPassword("A8016168a")
                        .withMailto("parentingteen@umich.edu")
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject("Minuku")
                        .withBody("Minuku service started.")
                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                            @Override
                            public void onSuccess() {
                                // TODO
                            }
                        })
                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                            @Override
                            public void onFail() {
                                // TODO
                            }
                        })
                        .send();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnAddUser = (Button) findViewById(R.id.activity_login_btnAdd);
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userSettingsDBHelper.getTotalNumOfUser() < limitOfUsers) {
                    userSettingsDBHelper.insertDB(new User());
                    setListView();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Reach the User Limit")
                            .setMessage("The user limit for this device is " + limitOfUsers + ".")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }
        });
        lvUsers = (ListView) findViewById(R.id.activity_login_lv);
        setListView();
    }

    public static Context getContext() {
        return context;
    }

    public void setListView() {
        userIconAdapter = new UserIconAdapter(context, userSettingsDBHelper.getAllUserList());
        lvUsers.setAdapter(userIconAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
