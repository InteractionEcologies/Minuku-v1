package edu.umich.si.inteco.minuku.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.LoginActivity;
import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.adapters.UserIconAdapter;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.model.User;
import edu.umich.si.inteco.minuku.util.AnimUtilities;

/**
 * Created by tsung on 2017/3/8.
 */

public class LoginFragment extends Fragment {

    // Main context and view
    private Context context;
    private View view;

    // UI widgets
    private Button btnFinish, btnAddUser;
    private ListView lvUsers;
    private UserIconAdapter userIconAdapter;

    // Functions
    private UserSettingsDBHelper userSettingsDBHelper;
    private int limitOfUsers = 8;
    private AnimUtilities animUtilities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = LoginActivity.getContext();
        view = inflater.inflate(R.layout.fragment_login, container, false);

        init();
        return view;
    }

    private void init() {
        animUtilities = new AnimUtilities(context);
        userSettingsDBHelper = new UserSettingsDBHelper(context);

        btnFinish = (Button) view.findViewById(R.id.fragment_login_btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userSettingsDBHelper.getTotalNumOfUser() > 0 && !userSettingsDBHelper.getIfColumnContainNull()) {
                    ((LoginActivity) LoginActivity.getContext()).setFragment(LoginActivity.ENTERID_FRAGMENT);
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Column contains Empty value")
                            .setMessage("Please check you have at least one user and no column contains empty value.")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }
        });

        btnAddUser = (Button) view.findViewById(R.id.fragment_login_btnAdd);
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

        lvUsers = (ListView) view.findViewById(R.id.fragment_login_lv);
        setListView();
    }

    public void setListView() {
        userIconAdapter = new UserIconAdapter(context, userSettingsDBHelper.getAllUserList());
        lvUsers.setAdapter(userIconAdapter);
    }
}
