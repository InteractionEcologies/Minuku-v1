package edu.umich.si.inteco.minuku.constants;

public class Constants {

    /**
     * Minuku name
     **/
    public static final String MINUKU_PREFIX = "Minuku";

    /**
     * Foreground Service
     **/
    public static final String MINUKU_SERVICE_CHECKING_ISALIVE = "MinukuServiceCheckingIsAlive";

    public interface ACTION {
        String PACKAGE_NAME = "edu.umich.si.inteco.minuku";
        String MAIN_ACTION = PACKAGE_NAME + ".action.main";
        String PREV_ACTION = PACKAGE_NAME + ".action.prev";
        String PLAY_ACTION = PACKAGE_NAME + ".action.play";
        String NEXT_ACTION = PACKAGE_NAME + ".action.next";
        String STARTFOREGROUND_ACTION = PACKAGE_NAME + ".action.startforeground";
        String STOPFOREGROUND_ACTION = PACKAGE_NAME + ".action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }

    /**
     * for testing
     **/
    public static boolean isTestingActivity = true;
    public static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";
    public static final String TEST_DATABASE_NAME = "SensingStudyDatabase";

    /***
     * Main Activity Tab Name
     **/
    public static final String MAIN_ACTIVITY_TAB_HOME = "Home";
    public static final String MAIN_ACTIVITY_TAB_DAILY_REPORT = "Daily Report";
    public static final String MAIN_ACTIVITY_TAB_RECORD = "Record";
    public static final String MAIN_ACTIVITY_TAB_RECORDINGS = "Recordings";
    public static final String MAIN_ACTIVITY_TAB_TASKS = "Profile";

    /**
     * for dangerous permission
     **/
    public static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 0;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 1;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 3;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CONTACTS = 4;
    public static final int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 5;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 7;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_RECORD_AUDIO = 8;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 9;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 10;
    public static final int MY_PERMISSIONS_REQUEST_READ_CALL_LOG = 11;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_CALL_LOG = 12;
    public static final int MY_PERMISSIONS_REQUEST_ADD_VOICEMAIL = 13;
    public static final int MY_PERMISSIONS_REQUEST_USE_SIP = 14;
    public static final int MY_PERMISSIONS_REQUEST_PROCESS_OUTGOING_CALLS = 15;
    public static final int MY_PERMISSIONS_REQUEST_BODY_SENSORS = 16;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 17;
    public static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 18;
    public static final int MY_PERMISSIONS_REQUEST_READ_SMS = 19;
    public static final int MY_PERMISSIONS_REQUEST_RECEIVE_WAP_PUSH_SMS = 20;
    public static final int MY_PERMISSIONS_REQUEST_RECEIVE_MMS = 21;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 22;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 23;


    /**
     * for labeling Study
     **/
    //1: participatory
    //2: in situ
    //3: post hoc

    public static final String PARTICIPATORY_LABELING_CONDITION = "Participatory Labeling";
    public static final String IN_STIU_LABELING_CONDITION = "In Situ Labeling";
    public static final String POST_HOC_LABELING_CONDITION = "Post Hoc Labeling";
    public static final String HYRBID_LABELING_CONDITION = "Hybrid Labeling";
    public static final String NORMAL_CONDITION = "Normal";


    public static final String CONFIGURATION_FILE_NAME_POST_HOC = "post_hoc_study.json";
    public static final String CONFIGURATION_FILE_NAME_IN_SITU = "in_situ_study.json";
    public static final String CONFIGURATION_FILE_NAME_PARTI = "participatory_study.json";

    //Web serivce
//    public static final String WEB_SERVICE_URL_QUERY = "https://inteco.cloudapp.net:5001/query";
//    public static final String WEB_SERVICE_URL_POST_SESSION = "https://inteco.cloudapp.net:5001/postsession";
//    public static final String WEB_SERVICE_URL_POST_BACKGROUND_RECORDING = "https://inteco.cloudapp.net:5001/postbackgroundrecording";
//    public static final String WEB_SERVICE_URL_REQUEST_SENDING_EMAIL = "https://inteco.cloudapp.net:5001/request_sending_email";
//    public static final String WEB_SERVICE_URL_DEVICE_CHECKING = "http://inteco.cloudapp.net:5010/isalive";


    /**
     * Google Play Service
     **/

    public static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";


    //    public static final String WEB_SERVICE_URL_POST_FILES = "http://inteco.cloudapp.net:5001/";
    public static final String WEB_SERVICE_URL_POST_FILES = "https://inteco.cloudapp.net:5001/postlog";


    public static String CURRENT_STUDY_CONDITION = NORMAL_CONDITION;

    //this is the id for the labling study.
    public static final int LABELING_STUDY_ID = 1;

    //if researchers want to record records in the background for the purpose of monitoring, the session number by default is 0
    public static final int BACKGOUND_RECORDING_SESSION_ID = 1;    //the id column in the session table is auto-incremental, so the minimum is 1
    public static final int BACKGOUND_RECORDING_TASK_ID = 0;    //task with id 0 means it's the background recording.
    public static final String BACKGOUND_RECORDING_TASK_NAME = "Background_Recording";
    public static final String BACKGOUND_RECORDING_TASK_DESCRIPTION = "The task is Minuku's background logging";
    public static final int BACKGOUND_RECORDING_NO_STUDY_ID = 0;


    /**
     * participant
     **/
    public static String DEVICE_ID = "NA";
    public static String USER_ID = "NA";

    //action alarm
    public static final String ACTION_ALARM = "edu.umich.si.inteco.minuku.actionAlarm";
    //  public static final String INTENT_ACTION_CONNECTIVITY_CHANGE = "edu.umich.si.inteco.captureprobe.intent.action.connectivityChange";


    /**
     * constant
     **/
    public static final long MILLISECONDS_PER_SECOND = 1000;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int HOURS_PER_DAY = 24;
    public static final long MILLISECONDS_PER_DAY = HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;
    public static final long MILLISECONDS_PER_HOUR = MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;
    public static final long MILLISECONDS_PER_MINUTE = SECONDS_PER_MINUTE * MILLISECONDS_PER_SECOND;
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss Z";
    public static final String DATE_FORMAT_NOW_NO_ZONE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_NOW_DAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_NOW_HOUR = "yyyy-MM-dd HH";
    public static final String DATE_FORMAT_NOW_HOUR_MIN = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_HOUR_MIN_SECOND = "HH:mm:ss";
    public static final String DATE_FORMAT_FOR_ID = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_HOUR_MIN = "HH:mm";
    public static final String DATE_FORMAT_HOUR = "HH";
    public static final String DATE_FORMAT_MIN = "mm";
    public static final String DATE_FORMAT_DATE_TEXT = "MMM dd";
    public static final String DATE_FORMAT_DATE_TEXT_HOUR_MIN = "MMM dd HH:mm";
    public static final String DATE_FORMAT_DATE_TEXT_HOUR_MIN_SEC = "MMM dd  HH:mm:ss";
    public static final int DATA_FORMAT_TYPE_NOW = 0;
    public static final int DATA_FORMAT_TYPE_DAY = 1;
    public static final int DATA_FORMAT_TYPE_HOUR = 2;
    public static final String DELIMITER = ";;;";
    public static final String ACTIVITY_DELIMITER = ":";
    public static final String CONTEXT_SOURCE_DELIMITER = ":";
    public static final String DELIMITER_IN_COLUMN = "::";

    /**
     * File Path
     **/

    public static String PACKAGE_DIRECTORY_PATH = "/Android/data/edu.umich.si.inteco.minuku/";
    public static String PACKAGE_DIRECTORY_NARRATIVE_PATH = "/Android/data/com.narrative.main/cache/";


    /**
     * Configurable parameters
     **/

	/*Context Extracotr*/
    public static int CONTEXTEXTRACTOR_SAMPLING_RATE = 1;
    public static float NULL_NUMERIC_VALUE = -9999;


}
