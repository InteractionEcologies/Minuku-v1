package edu.umich.si.inteco.minuku.fragments;

/**
 * Created by tsung on 2017/1/24.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.constants.UserIconReference;
import edu.umich.si.inteco.minuku.data.FirebaseManager;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.model.User;
import edu.umich.si.inteco.minuku.model.Views.UserIcon;
import edu.umich.si.inteco.minuku.util.NotificationHelper;
import edu.umich.si.inteco.minuku.util.PreferenceHelper;
import edu.umich.si.inteco.minuku.util.RecordingAndAnnotateManager;

public class ProfileFragment extends Fragment {
    private static String LOG_TAG = "ProfileFragment";

    private static Context context;
    private View rootView;

    //UI Widgets
    private Spinner spNumOfPeople;
    private Button btnSave;
    private GridLayout gridLayout;
    private TextView tvTitle, tvRunBack;
    private AlertDialog syncDialog;

    //functions
    private ProfileButtonListener profileButtonListener;
    private UserSettingsDBHelper userSettingsDBHelper;
    private UserIconReference userIconReference;
    private FirebaseManager firebaseMgr;
    private ArrayList<String> arraySpinner;
    private ArrayList<User> userList;
    private ArrayList<Integer> userIdList;
    private int numOfPeopleUsing = 100;
    private int currentSelected = 0;
    private String FILE_NAME = "logs.json";
    private File FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/user_logs/");

    //WiFi: b4:ce:f6:9d:d0:18
    //BT: 80:7A:BF:06:5B:01
    //temporary use
    private ImageButton btnSync;
    private boolean showMessage = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = MainActivity.getContext();
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        return rootView;
    }

    private void init() {
        profileButtonListener = new ProfileButtonListener();
        userSettingsDBHelper = new UserSettingsDBHelper(context);

        // find views
        gridLayout = (GridLayout) rootView.findViewById(R.id.fragment_profile_gridLayout);

        // Set title caption
        tvTitle = (TextView) rootView.findViewById(R.id.fragment_profile_tv_title);
        if (!PreferenceHelper.getPreferenceBoolean(PreferenceHelper.USER_MODE_SELECTION, true)) {
            tvTitle.setText("Please let this app run in the background. \n You are in mobile mode now.");
        }
        tvRunBack = (TextView) rootView.findViewById(R.id.fragment_profile_tvRunBack);

        // Spinner is not used for current version, will be removed in release version
        spNumOfPeople = (Spinner) rootView.findViewById(R.id.fragment_profile_spinner);
        this.arraySpinner = new ArrayList<String>();
        for (int i = 1; i <= userSettingsDBHelper.getTotalNumOfUser(); i++) {
            arraySpinner.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, arraySpinner);
        spNumOfPeople.setAdapter(adapter);
        spNumOfPeople.setSelection(userSettingsDBHelper.getCurrentNumOfUser() - 1);
        spNumOfPeople.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numOfPeopleUsing = position + 1;
                if (numOfPeopleUsing == userSettingsDBHelper.getTotalNumOfUser()) {
                    userSettingsDBHelper.setAllUserSelected();
                    currentSelected = numOfPeopleUsing;
                }

                new TaskSetUserIconView().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numOfPeopleUsing = 1;
                spNumOfPeople.setSelection(numOfPeopleUsing - 1);
            }
        });

        // Spinner is not used for current version, will be removed in release version
        spNumOfPeople.setVisibility(View.GONE);

        // Will be removed in release version, the system is auto saved
        btnSave = (Button) rootView.findViewById(R.id.fragment_profile_btnSave);
        btnSave.setOnClickListener(profileButtonListener);
        btnSave.setVisibility(View.GONE);

        // Button for testing backend
        btnSync = (ImageButton) rootView.findViewById(R.id.fragment_profile_btnSync);
        btnSync.setOnClickListener(profileButtonListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        tvRunBack.setText("Loading...");
        tvRunBack.setVisibility(View.GONE);
        new TaskSetUserIconView().execute();
    }

    private class TaskSetUserIconView extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (null == userIconReference)
                userIconReference = new UserIconReference(context);
            if (null == userSettingsDBHelper)
                userSettingsDBHelper = new UserSettingsDBHelper(context);

            gridLayout.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... v) {
            userList = userSettingsDBHelper.getAllUserListSortByAge();
            userIdList = userSettingsDBHelper.getAllIdListSortByAge();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            gridLayout.removeAllViews();
            for (int i = 0; i < userList.size(); i++) {
                final String id = userIdList.get(i).toString();
                final User user = userList.get(i);
                UserIcon userIcon = new UserIcon(context, id, user);
                gridLayout.addView(userIcon.getView());
            }

            setglAnimToVisible(gridLayout);
        }
    }

    public void setglAnimToVisible(final GridLayout gridLayout) {
        gridLayout.setVisibility(View.VISIBLE);
        Animation am = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        am.setDuration(500);
        gridLayout.setAnimation(am);
        am.startNow();
    }

    @Override
    public void onPause() {
        super.onPause();

        tvRunBack.setText("Prepare to run in the background...");
        gridLayout.setVisibility(View.GONE);
        tvRunBack.setVisibility(View.VISIBLE);
    }

    private void setSelectedTabColor(String id, User user) {
        // Work with spinner, can be removed with spinner
        if (currentSelected >= numOfPeopleUsing) {
            userSettingsDBHelper.setAllUserUnSelected();
            currentSelected = 1;
        } else {
            currentSelected++;
        }

        if (null == userSettingsDBHelper)
            userSettingsDBHelper = new UserSettingsDBHelper(context);

        if ("1".equalsIgnoreCase(user.getIfSelected())) {
            user.setIfSelected(false);
        } else {
            user.setIfSelected(true);
        }

        userSettingsDBHelper.updateDB(id, user);
        new TaskSetUserIconView().execute();
    }

    private class ProfileButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fragment_profile_btnSave:
                    if (currentSelected == numOfPeopleUsing) {
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
                case R.id.fragment_profile_btnSync:
                    showSyncDialog();
                    break;
                case R.id.dialog_sync_btn:
                    syncDialog.dismiss();
                    showWarningDialog();
                    break;
            }
        }
    }

    private void showSyncDialog() {
        LayoutInflater li = LayoutInflater.from(context);
        View v = li.inflate(R.layout.dialog_sync, null);
        syncDialog = new AlertDialog.Builder(context).create();
        Button sync = (Button) v.findViewById(R.id.dialog_sync_btn);
        sync.setOnClickListener(profileButtonListener);
        syncDialog.setView(v);
        syncDialog.show();
    }

    private void showWarningDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("ARE YOU SURE?")
                .setMessage("IF YES, MAKE SURE YOU ARE CONNECTED TO WIFI. OTHERWISE IT WILL USE A VERY LARGE AMOUNT OF YOUR DATA PLAN!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new TaskSendLogs().execute();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    File file = null;

    private class TaskSendLogs extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (!FILE_PATH.exists()) {
                FILE_PATH.mkdir();
            }

            file = new File(FILE_PATH, FILE_NAME);
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Create file failed: " + e.toString());
            }
            JSONArray documents = new JSONArray(RecordingAndAnnotateManager.getBackgroundRecordingDocuments(0));

            try {
                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);
                outputStreamWriter.write(documents.toString());
                outputStreamWriter.close();
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "File write failed: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            BackgroundMail.newBuilder(context)
                    .withUsername("minukudata@gmail.com")
                    .withPassword("Ilove2sleep")
                    .withType(BackgroundMail.TYPE_PLAIN)
                    .withMailto("Minukudata@umich.edu")
//                    .withMailto("twho@umich.edu")
                    .withBody("Study id: " + PreferenceHelper.getPreferenceString(PreferenceHelper.USER_ID, "Unknown study id") +"\n"
                            + "Device id: " + PreferenceHelper.getPreferenceString(PreferenceHelper.DEVICE_ID, "Unknown device id") +"\n\n"
                            + " User logs")
                    .withSubject("Family App Logger User Logs")
                    .withAttachments(file.getPath())
                    .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                        @Override
                        public void onSuccess() {
                            NotificationHelper.createSendEmailResultNotification(true);
                        }
                    })
                    .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                        @Override
                        public void onFail() {
                            NotificationHelper.createSendEmailResultNotification(false);
                            Log.d(LOG_TAG + " BackgroundEmail", "Fail to send background email");
                        }
                    })
                    .send();
        }
    }
}
