package edu.umich.si.inteco.minuku.fragments;

/**
 * Created by tsung on 2017/1/24.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.services.HomeScreenIconService;

public class ProfileFragment extends Fragment {

    private static Context context;
    private View rootView;
    //UI Widgets
    private Spinner spNumOfPeople;
    private Button btnSave;
    private String[] arraySpinner;
    private GridLayout gridLayout;
    //functions
    private ProfileButtonListener profileButtonListener;
    private UserSettingsDBHelper userSettingsDBHelper;
    private int numOfPeopleUsing = 1;
    private int currentSelected = 0;
    private String nameOfUser = "";
    public static int OVERLAY_PERMISSION_REQ_CODE_CHATHEAD = 1234;
    public static int OVERLAY_PERMISSION_REQ_CODE_CHATHEAD_MSG = 5678;

    //WiFi: b4:ce:f6:9d:d0:18
    //BT: 80:7A:BF:06:5B:01
    //temporary use
    private TextView tv1, tv2;
    private Button btnStart;
    private boolean showMessage = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        return rootView;
    }

    private void init() {
        profileButtonListener = new ProfileButtonListener();
        userSettingsDBHelper = new UserSettingsDBHelper(context);
        spNumOfPeople = (Spinner) rootView.findViewById(R.id.fragment_profile_spinner);
        gridLayout = (GridLayout) rootView.findViewById(R.id.fragment_profile_gridLayout);
        this.arraySpinner = new String[]{"1", "2", "3", "4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, arraySpinner);
        spNumOfPeople.setAdapter(adapter);
        spNumOfPeople.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numOfPeopleUsing = position + 1;
                if (position == 3) {
                    setAllSelectedViewColor(getResources().getColor(R.color.brightblue));
//                    for (int index = 0; index < ibRoles.size(); index++) {
//                        ibRoles.get(index).setEnabled(false);
//                    }
                } else {
//                    for (int index = 0; index < ibRoles.size(); index++) {
//                        ibRoles.get(index).setEnabled(true);
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numOfPeopleUsing = 1;
                spNumOfPeople.setSelection(numOfPeopleUsing - 1);
            }
        });
        btnSave = (Button) rootView.findViewById(R.id.fragment_profile_btnSave);
        btnSave.setOnClickListener(profileButtonListener);
        tv1 = (TextView) rootView.findViewById(R.id.fragment_profile_wifimac);
        tv2 = (TextView) rootView.findViewById(R.id.fragment_profile_btmac);
        tv1.setText("WiFi MAC Address: " + MainActivity.wifiMacAddr);
        tv2.setText("Bluetooth MAC Address: " + MainActivity.btMacAddr);
        btnStart = (Button) rootView.findViewById(R.id.fragment_profile_btnStart);
        btnStart.setOnClickListener(profileButtonListener);
    }

    private void setAllSelectedViewColor(int color) {
//        for (int index = 0; index < ibRoles.size(); index++) {
//            selectedViews.get(index).setBackgroundColor(color);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        spNumOfPeople.setSelection(userSettingsDBHelper.getCurrentNumOfUser() - 1);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void saveUserSelected() {

    }

    private void setSelectedTabColor(View selectedView, int user) {
        if (currentSelected >= numOfPeopleUsing) {
            setAllSelectedViewColor(getResources().getColor(R.color.transparent));
            selectedView.setBackgroundColor(getResources().getColor(R.color.brightblue));
            currentSelected = 1;
            nameOfUser = user + "";
        } else {
            selectedView.setBackgroundColor(getResources().getColor(R.color.brightblue));
            currentSelected++;
            nameOfUser += "," + user;
        }
        btnSave.setEnabled(true);
    }

    public class ProfileButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fragment_profile_btnSave:
                    if (currentSelected == numOfPeopleUsing) {
                        saveUserSelected();
                        btnSave.setEnabled(false);
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("Error")
                                .setMessage("Please select " + numOfPeopleUsing + " people using this tablet.")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                    }
                    break;
                case R.id.fragment_profile_btnStart:
                    startHomeScreenIcon();
                    if (showMessage) {
                        showHomeIconMsg();
                        showMessage = false;
                    } else {
                        showMessage = true;
                    }
                    break;
            }
        }
    }

    private void startHomeScreenIcon() {
        requestPermission(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD);
        requestPermission(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD_MSG);
        context.startService(new Intent(context, HomeScreenIconService.class));
    }

    private void showHomeIconMsg() {
        java.util.Date now = new java.util.Date();
        String str = "test by henry  " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);

        Intent it = new Intent(context, HomeScreenIconService.class);
        it.putExtra("Hello", str);
        context.startService(it);
    }

    private void requestPermission(int requestCode) {
        if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                startActivityForResult(intent, requestCode);
            }
        }
    }
}
