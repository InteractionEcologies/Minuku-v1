package edu.umich.si.inteco.minuku.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private UserSettingsDBHelper userSettingsDBHelper;

    // Functions
    private UserIconReference userIconReference;
    private ArrayList<User> userList;

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
                BackgroundMail.newBuilder(context)
                        .withUsername("tsungwei50521@gmail.com")
                        .withPassword("A8016168a")
                        .withMailto("parentingtech@umich.edu")
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject("Minuku")
                        .withBody("Minuku service started.")
                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                            @Override
                            public void onSuccess() {
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
            }
        });

        setUserIconView();
    }

    private void setUserIconView() {
        if (null == userIconReference)
            userIconReference = new UserIconReference(context);
        if (null == userSettingsDBHelper)
            userSettingsDBHelper = new UserSettingsDBHelper(context);
        userList = userSettingsDBHelper.getAllUserList();
        gridLayout.removeAllViews();
        for (int i = 0; i < userList.size(); i++) {
            final User user = userList.get(i);
            UserIcon userIcon = new UserIcon(context, user);
            ImageButton ib = userIcon.getIbUser();
            ib.setClickable(false);
            gridLayout.addView(userIcon.getView());
        }
    }

}
