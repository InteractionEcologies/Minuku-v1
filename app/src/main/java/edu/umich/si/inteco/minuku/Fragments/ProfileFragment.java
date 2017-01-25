package edu.umich.si.inteco.minuku.Fragments;

/**
 * Created by tsung on 2017/1/24.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.R;

public class ProfileFragment extends Fragment {

    private static Context context;
    private View rootView;
    private Spinner spNumOfPeople;
    private ImageButton ibRole1;
    private ImageButton ibRole2;
    private ImageButton ibRole3;
    private ImageButton ibRole4;
    private Button btnSave;
    private String[] arraySpinner;

    private ProfileButtonListener profileButtonListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        return rootView;
    }

    private void init(){
        profileButtonListener = new ProfileButtonListener();
        spNumOfPeople = (Spinner) rootView.findViewById(R.id.fragment_profile_spinner);
        this.arraySpinner = new String[] {"1", "2", "3", "4", "5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, arraySpinner);
        spNumOfPeople.setAdapter(adapter);
        btnSave = (Button) rootView.findViewById(R.id.fragment_profile_btnSave);
        ibRole1 = (ImageButton) rootView.findViewById(R.id.fragment_profile_role_icon1);
        ibRole2 = (ImageButton) rootView.findViewById(R.id.fragment_profile_role_icon2);
        ibRole3 = (ImageButton) rootView.findViewById(R.id.fragment_profile_role_icon3);
        ibRole4 = (ImageButton) rootView.findViewById(R.id.fragment_profile_role_icon4);
        setAllBtnListeners();
    }

    private void setAllBtnListeners(){
        ibRole1.setOnClickListener(profileButtonListener);
        ibRole2.setOnClickListener(profileButtonListener);
        ibRole3.setOnClickListener(profileButtonListener);
        ibRole4.setOnClickListener(profileButtonListener);
        btnSave.setOnClickListener(profileButtonListener);
    }

    public class ProfileButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view){
            switch (view.getId()) {
                case R.id.fragment_profile_role_icon1:
                    break;
                case R.id.fragment_profile_role_icon2:
                    break;
                case R.id.fragment_profile_role_icon3:
                    break;
                case R.id.fragment_profile_role_icon4:
                    break;
                case R.id.fragment_profile_btnSave:
                    break;
            }
        }
    }
}
