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
import android.os.AsyncTask;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import edu.umich.si.inteco.minuku.MainActivity;
import edu.umich.si.inteco.minuku.R;
import edu.umich.si.inteco.minuku.constants.UserIconReference;
import edu.umich.si.inteco.minuku.data.MongoDBHelper;
import edu.umich.si.inteco.minuku.data.UserSettingsDBHelper;
import edu.umich.si.inteco.minuku.model.User;
import edu.umich.si.inteco.minuku.model.Views.UserIcon;
import edu.umich.si.inteco.minuku.services.HomeScreenIconService;
import edu.umich.si.inteco.minuku.util.DatabaseNameManager;
import edu.umich.si.inteco.minuku.util.LogManager;
import edu.umich.si.inteco.minuku.util.RecordingAndAnnotateManager;
import edu.umich.si.inteco.minuku.util.ScheduleAndSampleManager;

public class ProfileFragment extends Fragment {
    private static String LOG_TAG = "ProfileFragment";

    private static Context context;
    private View rootView;
    //UI Widgets
    private Spinner spNumOfPeople;
    private Button btnSave;
    private GridLayout gridLayout;
    //functions
    private ProfileButtonListener profileButtonListener;
    private UserSettingsDBHelper userSettingsDBHelper;
    private UserIconReference userIconReference;
    private ArrayList<String> arraySpinner;
    private ArrayList<User> userList;
    private ArrayList<Integer> userIdList;
    private int numOfPeopleUsing = 1;
    private int currentSelected = 1;
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
        context = MainActivity.getContext();
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        return rootView;
    }

    private void init() {
        profileButtonListener = new ProfileButtonListener();
        userSettingsDBHelper = new UserSettingsDBHelper(context);
        spNumOfPeople = (Spinner) rootView.findViewById(R.id.fragment_profile_spinner);
        gridLayout = (GridLayout) rootView.findViewById(R.id.fragment_profile_gridLayout);
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
                setUserIconView();
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
        setUserIconView();
    }

    public void setUserIconView() {
        if (null == userIconReference)
            userIconReference = new UserIconReference(context);
        if (null == userSettingsDBHelper)
            userSettingsDBHelper = new UserSettingsDBHelper(context);
        userList = userSettingsDBHelper.getAllUserList();
        userIdList = userSettingsDBHelper.getAllIdList();
        gridLayout.removeAllViews();
        for (int i = 0; i < userList.size(); i++) {
            final String id = userIdList.get(i).toString();
            final User user = userList.get(i);
            UserIcon userIcon = new UserIcon(context, user);
            ImageButton ib = userIcon.getIbUser();
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelectedTabColor(id, user);
                }
            });
            gridLayout.addView(userIcon.getView());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setSelectedTabColor(String id, User user) {
        if (currentSelected >= numOfPeopleUsing) {
            userSettingsDBHelper.setAllUserUnSelected();
            currentSelected = 1;
        } else {
            currentSelected++;
        }
        user.setIfSelected(true);
        userSettingsDBHelper.updateDB(id, user);
        setUserIconView();
        btnSave.setEnabled(true);
    }

    public class ProfileButtonListener implements View.OnClickListener {

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
                case R.id.fragment_profile_btnStart:
                    ArrayList<JSONObject> documents = RecordingAndAnnotateManager.getBackgroundRecordingDocuments(0);
//                    startHomeScreenIcon();
//                    if (showMessage) {
//                        showHomeIconMsg();
//                        showMessage = false;
//                    } else {
//                        showMessage = true;
//                    }
                    for (int i = 0; i < documents.size(); i++) {
                        String json = documents.get(i).toString();

                        Log.d(LOG_TAG, "[testbackend][syncWithRemoteDatabase] background document post to mongolab");

                        String postURL = MongoDBHelper.postDocumentURL(ProjectDatabaseName, DatabaseNameManager.MONGODB_COLLECTION_BACKGROUNDLOGGING);

//                    Log.d (LOG_TAG, "[testbackend][syncWithRemoteDatabase] background document " + postURL  + " with json: " + json);

                        System.out.println("[testbackend]" + json);

                        new HttpAsyncPostJsonTask().execute(postURL,
                                json,
                                DATA_TYPE_BACKGROUND_LOGGING,
                                ScheduleAndSampleManager.getTimeString(0));
                    }

                    break;
            }
        }
    }

    public static final String DATA_TYPE_BACKGROUND_LOGGING = "background_recording";
    public static String ProjectDatabaseName = DatabaseNameManager.DATABASE_NAME_MINUKU;

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

    // TODO for test only

    //use HTTPAsyncTask to post data
    private static class HttpAsyncPostJsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = null;
            String url = params[0];
            String data = params[1];
            String dataType = params[2];
            String lastSyncTime = params[3];

            postJSON(url, data, dataType, lastSyncTime);

            return result;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Log.d(LOG_TAG, "get http post result" + result);

        }
    }

    public static final int HTTP_TIMEOUT = 10000; // millisecond
    public static final int SOCKET_TIMEOUT = 20000;

    public static String postJSON(String address, String json, String dataType, String lastSyncTime) {

        Log.d(LOG_TAG, "[postJSON] testbackend post data to " + address);

        LogManager.log(LogManager.LOG_TYPE_FILE_UPLOAD_LOG, "POSTJSON", json);

        InputStream inputStream = null;
        String result = "";

        try {

            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d(LOG_TAG, "[postJSON] testbackend connecting to " + address);

            if (url.getProtocol().toLowerCase().equals("https")) {
                Log.d(LOG_TAG, "[postJSON] [using https]");
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }


            SSLContext sc;
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());

            conn.setReadTimeout(HTTP_TIMEOUT);
            conn.setConnectTimeout(SOCKET_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json);
            wr.close();

            LogManager.log(LogManager.LOG_TYPE_SYSTEM_LOG,
                    LogManager.LOG_TAG_POST_DATA,
                    "Post:\t" + dataType + "\t" + "for lastSyncTime:" + lastSyncTime);

            int responseCode = conn.getResponseCode();

            if (responseCode >= 400)
                inputStream = conn.getErrorStream();
            else
                inputStream = conn.getInputStream();

//            inputStream = conn.getInputStream();
            result = convertInputStreamToString(inputStream);

            Log.d(LOG_TAG, "[postJSON] the result response code is " + responseCode);
            Log.d(LOG_TAG, "[postJSON] the result is " + result);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
//            Log.d(LOG_TAG, "[syncWithRemoteDatabase] " + line);
            result += line;
        }

        inputStream.close();
        return result;

    }

    public static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {

        X509TrustManager easyTrustManager = new X509TrustManager() {

            public void checkClientTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
                // Oh, I am easy!
            }

            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
                // Oh, I am easy!
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{easyTrustManager};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
