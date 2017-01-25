package edu.umich.si.inteco.minuku.Fragments;

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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.services.HomeScreenIconService;

public class ProfileFragment extends Fragment {

    private static Context context;
    private View rootView;
    //UI Widgets
    private Spinner spNumOfPeople;
    private ImageButton ibRole1, ibRole2, ibRole3, ibRole4;
    private ArrayList<ImageButton> ibRoles;
    private Button btnSave;
    private String[] arraySpinner;
    private View selectedView1, selectedView2, selectedView3, selectedView4;
    private ArrayList<View> selectedViews;
    //functions
    private ProfileButtonListener profileButtonListener;
    private SharedPreferences sharedpreferences;
    public static final String SELECTEDUSER = "SELECTEDUSER";
    public static final String NUMOFUSER = "NUMOFUSER";
    public static final String NAMEOFUSER = "NAMEOFUSER";
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
        spNumOfPeople = (Spinner) rootView.findViewById(R.id.fragment_profile_spinner);
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
                    for (int index = 0; index < ibRoles.size(); index++) {
                        ibRoles.get(index).setEnabled(false);
                    }
                } else {
                    for (int index = 0; index < ibRoles.size(); index++) {
                        ibRoles.get(index).setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numOfPeopleUsing = 1;
                spNumOfPeople.setSelection(numOfPeopleUsing - 1);
            }
        });
        btnSave = (Button) rootView.findViewById(R.id.fragment_profile_btnSave);
        ibRole1 = (ImageButton) rootView.findViewById(R.id.fragment_profile_role_icon1);
        ibRole2 = (ImageButton) rootView.findViewById(R.id.fragment_profile_role_icon2);
        ibRole3 = (ImageButton) rootView.findViewById(R.id.fragment_profile_role_icon3);
        ibRole4 = (ImageButton) rootView.findViewById(R.id.fragment_profile_role_icon4);
        ibRoles = new ArrayList<ImageButton>() {{
            add(ibRole1);
            add(ibRole2);
            add(ibRole3);
            add(ibRole4);
        }};
        selectedView1 = (View) rootView.findViewById(R.id.fragment_profile_role_selectView1);
        selectedView2 = (View) rootView.findViewById(R.id.fragment_profile_role_selectView2);
        selectedView3 = (View) rootView.findViewById(R.id.fragment_profile_role_selectView3);
        selectedView4 = (View) rootView.findViewById(R.id.fragment_profile_role_selectView4);
        selectedViews = new ArrayList<View>() {{
            add(selectedView1);
            add(selectedView2);
            add(selectedView3);
            add(selectedView4);
        }};
        tv1 = (TextView) rootView.findViewById(R.id.fragment_profile_wifimac);
        tv2 = (TextView) rootView.findViewById(R.id.fragment_profile_btmac);
        tv1.setText("WiFi MAC Address: " + MainActivity.wifiMacAddr);
        tv2.setText("Bluetooth MAC Address: " + MainActivity.btMacAddr);
        btnStart = (Button) rootView.findViewById(R.id.fragment_profile_btnStart);
        btnStart.setOnClickListener(profileButtonListener);
        setAllBtnListeners();
    }

    private void setAllBtnListeners() {
        for (int index = 0; index < ibRoles.size(); index++) {
            ibRoles.get(index).setOnClickListener(profileButtonListener);
        }
        btnSave.setOnClickListener(profileButtonListener);
    }

    private void setAllSelectedViewColor(int color) {
        for (int index = 0; index < ibRoles.size(); index++) {
            selectedViews.get(index).setBackgroundColor(color);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedpreferences = context.getSharedPreferences(SELECTEDUSER, Context.MODE_PRIVATE);
        currentSelected = sharedpreferences.getInt(NUMOFUSER, 1);
        nameOfUser = sharedpreferences.getString(NAMEOFUSER, "");
        numOfPeopleUsing = nameOfUser.split(",").length;
        spNumOfPeople.setSelection(numOfPeopleUsing - 1);
        Log.d("asfasf56", nameOfUser + ", number: " + nameOfUser.split(",").length);
        for (int index = 0; index < nameOfUser.split(",").length; index++) {
            selectedViews.get(index).setBackgroundColor(getResources().getColor(R.color.brightblue));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void saveUserSelected() {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(NAMEOFUSER, nameOfUser);
        editor.commit();
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
                case R.id.fragment_profile_role_icon1:
                    setSelectedTabColor(selectedView1, 0);
                    break;
                case R.id.fragment_profile_role_icon2:
                    setSelectedTabColor(selectedView2, 1);
                    break;
                case R.id.fragment_profile_role_icon3:
                    setSelectedTabColor(selectedView3, 2);
                    break;
                case R.id.fragment_profile_role_icon4:
                    setSelectedTabColor(selectedView4, 3);
                    break;
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
                    Log.d("afasfas", "afasf");
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

    private void startHomeScreenIcon(){
        requestPermission(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD);
        requestPermission(OVERLAY_PERMISSION_REQ_CODE_CHATHEAD_MSG);
        context.startService(new Intent(context, HomeScreenIconService.class));
    }

    private void showHomeIconMsg(){
        java.util.Date now = new java.util.Date();
        String str = "test by henry  " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);

        Intent it = new Intent(context, HomeScreenIconService.class);
        it.putExtra("Hello", str);
        context.startService(it);
    }

    private void requestPermission(int requestCode){
        if (!(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)) {
            if (!Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                startActivityForResult(intent, requestCode);
            }
        }
    }
}
