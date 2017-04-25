package edu.umich.si.inteco.minuku.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.umich.si.inteco.minuku.LoginActivity;
import edu.umich.si.inteco.minuku.R;

/**
 * Created by Michael Ho on 4/19/2017.
 */

public class OpenpageFragment extends Fragment {

    // Main context and view
    private Context context;
    private View view;

    // UI Views
    private Button btnAdult, btnChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = LoginActivity.getContext();
        view = inflater.inflate(R.layout.fragment_openpage, container, false);

        init();
        return view;
    }

    private void init() {
        btnAdult = (Button) view.findViewById(R.id.fragment_openpage_btnAdult);
        btnAdult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to some other pages
                ((LoginActivity) LoginActivity.getContext()).setFragment(LoginActivity.ADENTERID_FRAGMENT);
            }
        });

        btnChild = (Button) view.findViewById(R.id.fragment_openpage_btnChild);
        btnChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity) LoginActivity.getContext()).setFragment(LoginActivity.LOGIN_FRAGMENT);
            }
        });
    }
}
