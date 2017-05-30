package edu.umich.si.inteco.minuku.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umich.si.inteco.minuku.LoginActivity;
import edu.umich.si.inteco.minuku.R;

/**
 * Created by tsung on 2017/4/23.
 */

public class AdultEnterIdFragment extends Fragment {

    // Main context and view
    private Context context;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = LoginActivity.getContext();
        view = inflater.inflate(R.layout.fragment_adultenterid, container, false);

        init();
        return view;
    }

    private void init() {

    }
}
