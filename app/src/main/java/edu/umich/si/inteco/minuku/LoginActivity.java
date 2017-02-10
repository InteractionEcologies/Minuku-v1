package edu.umich.si.inteco.minuku;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by tsung on 2017/2/6.
 */

public class LoginActivity extends Activity{

    private Spinner spNumOfUsers;
//    try to use floating action button
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init(){
        btnLogin = (Button) findViewById(R.id.activity_login_btnLogin);
        spNumOfUsers = (Spinner) findViewById(R.id.activity_login_sp);
    }
}
