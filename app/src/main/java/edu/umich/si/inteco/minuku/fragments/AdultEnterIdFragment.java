package edu.umich.si.inteco.minuku.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.LoginActivity;
import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.MobileMainActivity;
import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.model.User;
import edu.umich.si.inteco.minuku.util.PreferenceHelper;

/**
 * Created by tsung on 2017/4/23.
 */

public class AdultEnterIdFragment extends Fragment {

    // Main context and view
    private Context context;
    private View view;

    // UI Views
    private Button btnLogin;
    private EditText edStudyId;

    // Database
    private UserSettingsDBHelper userSettingsDBHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = LoginActivity.getContext();
        view = inflater.inflate(R.layout.fragment_adultenterid, container, false);

        init();
        return view;
    }

    private void init() {
        userSettingsDBHelper = new UserSettingsDBHelper(context);

        edStudyId = (EditText) view.findViewById(R.id.fragment_adultenterid_edStudy);

        btnLogin = (Button) view.findViewById(R.id.fragment_adultenterid_btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edStudyId.getText().toString().equalsIgnoreCase("")) {
                    BackgroundMail.newBuilder(context)
                            .withUsername("minukudata@gmail.com")
                            .withPassword("Ilove2sleep")
                            .withType(BackgroundMail.TYPE_PLAIN)
                            .withMailto("Minukudata@umich.edu")
                            .withSubject("Family App Logger")
                            .withBody("Family App Logger service started.\n \n" + "UniqueId: " + LoginActivity.wifiMacAddr + LoginActivity.btMacAddr
                                    + "\n \n" + "Device: mobile device"
                                    + "\n \n" + "StudyId: " + edStudyId.getText().toString() + "\n \n"
                                    + getUserDataList(userSettingsDBHelper.getAllUserList()))
                            .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                @Override
                                public void onSuccess() {
                                    PreferenceHelper.setPreferenceStringValue(PreferenceHelper.DEVICE_ID, LoginActivity.wifiMacAddr + LoginActivity.btMacAddr);
                                    PreferenceHelper.setPreferenceStringValue(PreferenceHelper.USER_ID, edStudyId.getText().toString());
                                    PreferenceHelper.setPreferenceBooleanValue(PreferenceHelper.USER_SETUP_COMPLETED, true);
                                    Intent intent = new Intent(getActivity(), MobileMainActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            })
                            .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                @Override
                                public void onFail() {
                                    // TODO
                                }
                            })
                            .send();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Study id is required")
                            .setMessage("Please enter your study id to login.")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }
        });
    }

    private String getUserDataList(ArrayList<User> userArrayList) {
        String userDataList = "";

        for (int i = 0; i < userArrayList.size(); i++) {
            userDataList += (i + 1) + "-";
            userDataList += userArrayList.get(i).getUserName() + "-";
            userDataList += "Age:" + userArrayList.get(i).getUserAge() + "\n";
        }

        return userDataList;
    }
}
