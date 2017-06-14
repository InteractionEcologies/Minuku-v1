package edu.umich.si.inteco.minuku.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.LoginActivity;
import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.constants.UserIconReference;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.model.User;
import edu.umich.si.inteco.minuku.model.Views.UserIcon;
import edu.umich.si.inteco.minuku.util.PreferenceHelper;

/**
 * Created by tsung on 2017/3/8.
 */

public class EnterIdFragment extends Fragment {
    // Main context and view
    private Context context;
    private View view;

    // UI Widgets
    private Button btnLogin, btnBack;
    private GridLayout gridLayout;
    private EditText edStudyId;

    // Functions
    private UserSettingsDBHelper userSettingsDBHelper;
    private UserIconReference userIconReference;
    private ArrayList<User> userList;
    private ArrayList<Integer> userIdList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = LoginActivity.getContext();
        view = inflater.inflate(R.layout.fragment_enterid, container, false);

        init();
        return view;
    }

    private void init() {
        userSettingsDBHelper = new UserSettingsDBHelper(context);

        // Find views
        edStudyId = (EditText) view.findViewById(R.id.fragment_enterid_edStudy);
        gridLayout = (GridLayout) view.findViewById(R.id.fragment_enterid_gridLayout);
        btnBack = (Button) view.findViewById(R.id.fragment_enterid_btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) LoginActivity.getContext()).setFragment(LoginActivity.LOGIN_FRAGMENT);
            }
        });

        btnLogin = (Button) view.findViewById(R.id.fragment_enterid_btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                .withMailto("parentingtech@umich.edu")
                if (!edStudyId.getText().toString().equalsIgnoreCase("")) {
                    BackgroundMail.newBuilder(context)
                            .withUsername("minukudata@gmail.com")
                            .withPassword("Ilove2sleep")
                            .withType(BackgroundMail.TYPE_PLAIN)
                            .withMailto("Minukudata@umich.edu")
//                            .withMailto("twho@umich.edu")
                            .withSubject("Family App Logger")
                            .withBody("Family App Logger service started.\n \n" + "UniqueId: " + LoginActivity.wifiMacAddr + LoginActivity.btMacAddr
                                    + "\n \n" + "Device: family tablet"
                                    + "\n \n" + "StudyId: " + edStudyId.getText().toString() + "\n \n"
                                    + getUserDataList(userSettingsDBHelper.getAllUserList()))
                            .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                @Override
                                public void onSuccess() {
                                    PreferenceHelper.setPreferenceStringValue(PreferenceHelper.DEVICE_ID, LoginActivity.wifiMacAddr + LoginActivity.btMacAddr);
                                    PreferenceHelper.setPreferenceStringValue(PreferenceHelper.USER_ID, edStudyId.getText().toString());
                                    PreferenceHelper.setPreferenceBooleanValue(PreferenceHelper.USER_SETUP_COMPLETED, true);
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
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

        setUserIconView();
    }

    private void setUserIconView() {
        if (null == userIconReference)
            userIconReference = new UserIconReference(context);

        if (null == userSettingsDBHelper)
            userSettingsDBHelper = new UserSettingsDBHelper(context);

        userSettingsDBHelper.setAllUserUnSelected();
        userList = userSettingsDBHelper.getAllUserList();
        userIdList = userSettingsDBHelper.getAllIdListSortByAge();
        gridLayout.removeAllViews();
        for (int i = 0; i < userList.size(); i++) {
            final User user = userList.get(i);
            final String id = userIdList.get(i).toString();

            UserIcon userIcon = new UserIcon(context, id, user);
            ImageButton ib = userIcon.getIbUser();
            ib.setClickable(false);
            gridLayout.addView(userIcon.getView());
        }
    }

    private String getUserDataList(ArrayList<User> userArrayList) {
        String userDataList = "";

        for (int i = userArrayList.size() - 1; i >= 0; i--) {
            userDataList += userArrayList.get(i).getUserNumber() + "-";
            userDataList += userArrayList.get(i).getUserName() + "-";
            userDataList += "Age:" + userArrayList.get(i).getUserAge() + "\n";
        }

        return userDataList;
    }

}
