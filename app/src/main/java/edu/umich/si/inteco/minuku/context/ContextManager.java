package edu.umich.si.inteco.minuku.context;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.umich.si.inteco.minuku.Constants;
import edu.umich.si.inteco.minuku.context.ContextStateManagers.ActivityRecognitionManager;
import edu.umich.si.inteco.minuku.context.ContextStateManagers.LocationManager;
import edu.umich.si.inteco.minuku.context.ContextStateManagers.PhoneSensorManager;
import edu.umich.si.inteco.minuku.context.ContextStateManagers.PhoneStatusManager;
import edu.umich.si.inteco.minuku.context.ContextStateManagers.TransportationModeManager;
import edu.umich.si.inteco.minuku.context.ContextStateManagers.UserInteractionManager;
import edu.umich.si.inteco.minuku.data.DataHandler;
import edu.umich.si.inteco.minuku.data.LocalDBHelper;
import edu.umich.si.inteco.minuku.model.Condition;
import edu.umich.si.inteco.minuku.model.Event;
import edu.umich.si.inteco.minuku.model.ProbeObject;
import edu.umich.si.inteco.minuku.model.State;
import edu.umich.si.inteco.minuku.model.TriggerLink;
import edu.umich.si.inteco.minuku.model.record.ActivityRecord;
import edu.umich.si.inteco.minuku.model.record.Record;
import edu.umich.si.inteco.minuku.util.LogManager;
import edu.umich.si.inteco.minuku.util.TriggerManager;

public class ContextManager {

    private static final String LOG_TAG = "ContextManager";

    //whether we want a background recording
    protected static boolean sIsSavingRecordingDefault = true;

    //flag of whether ContextManager is currently extracting Context
    protected static boolean sIsExtractingContext = false;

    //flag of whether ContextManager needs to extractContext
    protected static boolean sIsExtractingContextEnabled = true;

    //flag of whether BackgroundRecording is enabled
    protected static boolean sIsBackgroundRecordingEnabled  = false;

    //mContext is MinukuService
	private Context mContext;

    public static final int BACKGROUND_RECORDING_INITIAL_DELAY = 0;
    public static final int CONTEXT_MANAGER_REFRESH_FREQUENCY = 5 ;


    /**RecordPool is a List for temporarily storing records that will be stored into the database or files later**/
    private static ArrayList<Record> mRecordPool;

    //the threshold of life of a record
    public static int RECORD_PRESERVATION_THRESHOLD_IN_MILLISECONDS = 2 *
            Constants.MILLISECONDS_PER_SECOND * Constants.SECONDS_PER_MINUTE;   //2 minutes

    public static final String CONTEXT_STATE_MANAGER_ACTIVITY_RECOGNITION = "ActivityRecognition";
    public static final String CONTEXT_STATE_MANAGER_LOCATION = "Location";
    public static final String CONTEXT_STATE_MANAGER_PHONE_SENSOR = "PhoneSensor";
    public static final String CONTEXT_STATE_MANAGER_PHONE_STATUS = "PhoneStatus";
    public static final String CONTEXT_STATE_MANAGER_TRANSPORTATION = "Transportation";
    public static final String CONTEXT_STATE_MANAGER_USER_INTERACTION = "UserInteraction";


    /*RECORD TYPE NAME*/
    public static final String CONTEXT_RECORD_TYPE_LOCATION_NAME = "Location";
    public static final String CONTEXT_RECORD_TYPE_ACTIVITY_NAME = "Activity";
    public static final String CONTEXT_RECORD_TYPE_SENSOR_NAME= "Sensor";
    public static final String CONTEXT_RECORD_TYPE_GEOFENCE_NAME= "Geofence";
    public static final String CONTEXT_RECORD_TYPE_APPLICATION_ACTIVITY_NAME= "AppActivity";

    public static final String RECORD_SHORTNAME_LOCATION_LATITUDE = "lat";
    public static final String RECORD_SHORTNAME_LOCATION_LONGITUDE = "lng";
    public static final String RECORD_SHORTNAME_LOCATION_ACCURACY = "acc";
    public static final String RECORD_SHORTNAME_ACTIVITY_ACTIVITY = "activity";
    public static final String RECORD_SHORTNAME_ACTIVITY_CONFIDENCE = "confidence";
    public static final String RECORD_SHORTNAME_APPLICATION_ACTIVITY = "appActivity";
    public static final String RECORD_SHORTNAME_APPLICATION_PACKAGE = "appPackage";


    public static final int CONTEXT_RECORD_TYPE_LOCATION = 1;
    public static final int CONTEXT_RECORD_TYPE_ACTIVITY = 2;
    public static final int CONTEXT_RECORD_TYPE_SENSOR= 3;
    public static final int CONTEXT_RECORD_TYPE_GEOFENCE= 4;
    public static final int CONTEXT_RECORD_TYPE_APPLICATION_ACTIVITY = 5;

    
    /**Turning on or off sensor**/
    public static boolean accelerometerSensorIsEnabled = true;
    public static boolean gravitySensorIsEnabled = true;
    public static boolean gyrscopeSensorIsEnabled = true;
    public static boolean linearAcceleerationSensorIsEnabled = true;
    public static boolean rotationVectorSensorIsEnabled = true;
    
    public static boolean magneticFieldSensorIsEnabled = true;    
    public static boolean orientationSensorIsEnabled = true;
    public static boolean proximitySensorIsEnabled = true;
    
    public static boolean ambientTemperatureSensorIsEnabled = true;
    public static boolean lightSensorIsEnabled = true;
    public static boolean pressureSensorIsEnabled = true;
    public static boolean relativeHumiditySensorIsEnabled = true;   

    private static int testActivityRecordIndex = 0;

    public static ArrayList<Integer> RECORD_TYPE_LIST;


    //handle the local SQLite operation
  	private static LocalDBHelper mLocalDBHelpder;

    /***sensor values***/
    private float mAccelationSquareRoot;

    private ActivityRecognitionManager mActivityRecognitionManager;

    //inspect transportation mode of the user
    private TransportationModeManager mTransportationModeManager;

    private GeofenceManager mGeofenceManager;

    // the location update manager
    private LocationManager mLocationManager;

    //the manager that manages the status of the phone (network, battery)
    private PhoneStatusManager mPhoneStatusManager;

    private UserInteractionManager mUserInteractionManager;

    private MobilityManager mMobilityManager;

    private int testCount = 0;

    private final ScheduledExecutorService mScheduledExecutorService;

	public ContextManager(Context context){

		mContext = context;

		mLocalDBHelpder = new LocalDBHelper(mContext, Constants.TEST_DATABASE_NAME);
        //initiate the RecordPool
        mRecordPool = new ArrayList<Record>();

        mScheduledExecutorService = Executors.newScheduledThreadPool(CONTEXT_MANAGER_REFRESH_FREQUENCY);

        RECORD_TYPE_LIST = new ArrayList<Integer>();
        
        //add the record types into the list
        initiateRecordTypeList();

        //initiate Context Source Managers
        mLocationManager = new LocationManager(mContext);

        mActivityRecognitionManager = new ActivityRecognitionManager(mContext);

        mTransportationModeManager = new TransportationModeManager(mContext);

        mPhoneStatusManager = new PhoneStatusManager(mContext);

        mUserInteractionManager = new UserInteractionManager(mContext);

        mMobilityManager = new MobilityManager(mContext, this);


      //  mActivityRecognitionRequester = new ActivityRecognitionRequester(mContext);

      //  mActivityRecognitionRemover = new ActivityRecognitionRemover(mContext);

	}

    /**
     * we start the main function of ContextManager here: extracting information and monitoring states
     */
    public void startContextManager() {

        Log.d(LOG_TAG, "[startContextManager]");

        /**if extractign contextual information is enabled, extract information**/
        if (sIsExtractingContextEnabled) {
            startExtractingContext();
        }

        startContextManagerMainThread();

    }

    public boolean isExtractingContext() {
        return sIsExtractingContext;
    }

    public void setExtractingContext(boolean flag) {
        sIsExtractingContext = flag;
    }

    public boolean isExtractingContextEnabled() {
        return sIsExtractingContextEnabled;
    }

    public void setExtractingContextEnabled(boolean flag) {
        sIsExtractingContextEnabled = flag;
    }


    public static void initiateRecordTypeList() {

        RECORD_TYPE_LIST.add(CONTEXT_RECORD_TYPE_LOCATION);
        RECORD_TYPE_LIST.add(CONTEXT_RECORD_TYPE_ACTIVITY);
        RECORD_TYPE_LIST.add(CONTEXT_RECORD_TYPE_APPLICATION_ACTIVITY);

        //TODO: add more record type
    }

    public static void addRecordToPool(Record record){

        if(mRecordPool!=null){
            mRecordPool.add(record);
            //Log.d(LOG_TAG, "In the RecordPool, there are currently " + mRecordPool.size() + "records");
        }else
            return;
    }

    public static ArrayList<Record> getRecordPool (){
        return mRecordPool;
    }


    public LocationManager getLocationManager(){
        if (mLocationManager==null){
            mLocationManager = new LocationManager(mContext);
        }
        return mLocationManager;
    }


    public void requesLocationUpdate () {
        if (mLocationManager!=null){
            Log.d(LOG_TAG, "[startRequestingLocation] start to request location udpate");
            mLocationManager.requestLocationUpdate();
        }
    }

    public void removeLocationUpdate () {
        if (mLocationManager!=null){
            Log.d(LOG_TAG, "[stopRequestingActivityRecognition] stop to request location");
            mLocationManager.removeLocationUpdate();
        }
    }


    /***
     * Requesting and Removing Activity Recognition Service
     */
    private void requestActivityRecognitionUpdate(){
        Log.d(LOG_TAG, "[startRequestingActivityRecognition] start to request activity udpate");
        mActivityRecognitionManager.requestActivityRecognitionUpdates();
    }

    private void removeActivityRecognitionUpdate(){
        Log.d(LOG_TAG, "[stopRequestingActivityRecognition] stop to request activity udpate");
        //if Google Play service is available, stop the update
        // Pass the remove request to the remover object (the intent is the same as the request intent)
        mActivityRecognitionManager.removeActivityRecognitionUpdates();
    }


    /**functions called by the ContextManager**/


    public void startExtractingContext(){

        //TODO: ContextManager register each context source manager to extract contextual information

        //if this.sIsExtractingContextEnabled is false, we don't extract context
        if (!sIsExtractingContextEnabled){
            return;
        }

        //TODO: depending on the source requested in the configuration, determine the source to use

        //get location information
        requesLocationUpdate();

        //get activity information
        requestActivityRecognitionUpdate();

        //registerSensors();



        //get geofence transitions
        //startRequestingGeofence();

        //set sIsExtractingContext true to indicate that ContextManager is currently extracting
        // contextual information
        this.sIsExtractingContext = true;

    }


    public void stopExtractingContext(){

        Log.d("LOG_TAG", "[stopExtractingContext]");
        this.sIsExtractingContext = false;

        //unregister sensors
       // unRegisterSensors();

        //remove location update
        removeLocationUpdate();

        //remove activity update
        removeActivityRecognitionUpdate();

    }


    /**
     * The function starts a thread to run background recording to save records.
     */
    public void startContextManagerMainThread() {
        mScheduledExecutorService.scheduleAtFixedRate(
                ContextManagerRunnable,
                BACKGROUND_RECORDING_INITIAL_DELAY,
                CONTEXT_MANAGER_REFRESH_FREQUENCY,
                TimeUnit.SECONDS);
    }


    /**
     * This function receives notifications from ContextSTateManager about a value change of a state,
     * It then examines any events of which the conditions involve the state, detemrining whether the
     * event occurs.
     * @param state
     */
    public static void examineEventConditions(State state) {

        /**get all the events that use the state **/
        ArrayList <Event> evtList = state.getEventList();

        //for each event, get all of the conditions, and check whether the condition has been met.
        for (int i=0; i< evtList.size(); i++) {

            Event event = evtList.get(i);
            /** an event is a list of conditions. An event occurs only when all conditions are met **/
            boolean pass = true;

            ArrayList<Condition> conditions = event.getConditionList();

            //we use "&" operation for all condition. As long as there is one false for one condition
            //pass is false.
                for (int j=0 ; j<conditions.size(); j++){
                    Condition condition = conditions.get(j);
                    //the final pass is true only when all the conditions are true.
                    pass = pass & state.getValue().equals(condition.getStateTargetValue());
                }

            /** for any event for which the conditions are true, we let TriggerManager to see which action/action control to trigger.**/

            //if the conditions of the event is satisfied.
            if (pass) {

                //log when an event is detected
                LogManager.log(LogManager.LOG_TYPE_SYSTEM_LOG,
                        LogManager.LOG_TAG_EVENT_DETECTED,
                        "Event detected:\t" + event.getId() + "\t" + event.getName());

                //check the triggerlinks of the current event to see if it would trigger anything.
                Log.d(LOG_TAG, "[examineEventConditions] The event " + event.getId() + "  condition is satisfied, check its triggerLinks! "
                        + " the event has " + event.getTriggerLinks().size() + " triggerlinks ");

                //the event will trigger something, we call TriggerManager to manage its trigger.
                if (event.getTriggerLinks().size() > 0) {
                    TriggerManager.executeTriggers(event.getTriggerLinks());
                }
            }

        }


    }

    public void stopContextManagerMainThread() {
       // mScheduledExecutorService.shutdown();
    }

    Runnable ContextManagerRunnable = new Runnable() {
        @Override
        public void run() {
            try{

                testCount +=1;


                Log.d(LOG_TAG, "[testCount]"  + testCount);

                /** test transporation : feed datain to the datapool**/

                 //REPLAY ACTIIVITY LOG
/*
                    if (testActivityRecordIndex<TransportationModeManager.getActivityRecords().size()){
                        Log.d(LOG_TAG, "[testing transportation] Feed the " + testActivityRecordIndex + " record :"
                        + TransportationModeManager.getActivityRecords().get(testActivityRecordIndex).getProbableActivities()
                        + TransportationModeManager.getActivityNameFromType(TransportationModeManager.getActivityRecords().get(testActivityRecordIndex).getProbableActivities().get(0).getType())  );

                        ActivityRecognitionManager.setProbableActivities(
                                TransportationModeManager.getActivityRecords().get(testActivityRecordIndex).getProbableActivities());

                        ActivityRecognitionManager.setLatestDetectionTime(
                                TransportationModeManager.getActivityRecords().get(testActivityRecordIndex).getTimestamp());
                    }

                    testActivityRecordIndex+=1;
*/
                //Recording is one of the types of actions that users need to put into the configuration.
                //However, now we want to enable background recording so that we can monitor events.
                //eventually. If researachers do not monitor anything, this flag should be false.
                if (sIsBackgroundRecordingEnabled){
                    DataHandler.SaveRecordsToLocalDatabase(ContextManager.getRecordPool(), Constants.BACKGOUND_RECORDING_SESSION_ID);
                }


                /* update transportation mode. Transporation Manager will use the latet activity label
                 * saved in the ActivityRecognitionManager to infer the user's current transportation mode
                 * **/

                ActivityRecord record = (ActivityRecord) ActivityRecognitionManager.getLastSavedRecord();
                if (record!=null){
                    int transportationMode= mTransportationModeManager.examineTransportation(record);
                }

                /* after the transportationModeManager generate a transportation label, we update Mobility
                 * of the user. The mobility information, right now,  will be used to control the
                 * frequency of location udpate to save battery life**/
                MobilityManager.updateMobility();


                String travelHistoryMessage="NA";
                /*we create a travel log here*/

                if (ActivityRecognitionManager.getProbableActivities()!=null &&
                        LocationManager.getCurrentLocation()!=null ){
                    travelHistoryMessage= MobilityManager.getMobility() + "\t" +
                            TransportationModeManager.getConfirmedActvitiyString() + "\t" +
                            "FSM:" + TransportationModeManager.getCurrentStateString() + "\t" +
                            ActivityRecognitionManager.getProbableActivities().toString() + "\t" +
                            LocationManager.getCurrentLocation().getLatitude() + "," +
                            LocationManager.getCurrentLocation().getLongitude() + "," +
                            LocationManager.getCurrentLocation().getAccuracy();
                }

                Log.d(LOG_TAG, "travel history message:" + travelHistoryMessage);

                //create travel history file
                LogManager.log(LogManager.LOG_TYPE_TRAVEL_LOG,
                        LogManager.LOG_TAG_TRAVEL_HISTORY,
                        //content of the log
                        travelHistoryMessage
                        );

            }catch (IllegalArgumentException e){
                //Log.e(LOG_TAG, "Could not unregister receiver " + e.getMessage()+"");
            }

        }
    };

    public static String getSourceName(String contextStateManager, int sourceType){

        if (contextStateManager.equals(CONTEXT_STATE_MANAGER_ACTIVITY_RECOGNITION)){
            return ActivityRecognitionManager.getContextSourceNameFromType(sourceType);
        }
        else if (contextStateManager.equals(CONTEXT_STATE_MANAGER_TRANSPORTATION)){
            return TransportationModeManager.getContextSourceNameFromType(sourceType);
        }
        else if (contextStateManager.equals(CONTEXT_STATE_MANAGER_LOCATION)){
            return LocationManager.getContextSourceNameFromType(sourceType);
        }
        else if (contextStateManager.equals(CONTEXT_STATE_MANAGER_PHONE_SENSOR)){
            return PhoneSensorManager.getContextSourceNameFromType(sourceType);
        }
        else if (contextStateManager.equals(CONTEXT_STATE_MANAGER_PHONE_STATUS)){
            return PhoneStatusManager.getContextSourceNameFromType(sourceType);
        }
        else if (contextStateManager.equals(CONTEXT_STATE_MANAGER_USER_INTERACTION)){
            return UserInteractionManager.getContextSourceNameFromType(sourceType);
        }
        else{
            return  null;
        }
    }

    public static String getSensorTypeName(int recordType){

        switch(recordType){

            case CONTEXT_RECORD_TYPE_LOCATION:
                return CONTEXT_RECORD_TYPE_LOCATION_NAME;
            case CONTEXT_RECORD_TYPE_ACTIVITY:
                return CONTEXT_RECORD_TYPE_ACTIVITY_NAME;
            case CONTEXT_RECORD_TYPE_SENSOR:
                return CONTEXT_RECORD_TYPE_SENSOR_NAME;
            case CONTEXT_RECORD_TYPE_GEOFENCE:
                return CONTEXT_RECORD_TYPE_SENSOR_NAME;
            case CONTEXT_RECORD_TYPE_APPLICATION_ACTIVITY:
                return CONTEXT_RECORD_TYPE_APPLICATION_ACTIVITY_NAME;
        }
        return "unknown";
    }

    /**get the current time in milliseconds**/
    public static long getCurrentTimeInMillis(){
        //get timzone
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = Calendar.getInstance(tz);
        long t = cal.getTimeInMillis();
        return t;
    }

    /**get the current time in string (in the format of "yyyy-MM-dd HH:mm:ss" **/
    public static String getCurrentTimeString(){
        //get timzone
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = Calendar.getInstance(tz);

        SimpleDateFormat sdf_now = new SimpleDateFormat(Constants.DATE_FORMAT_NOW);
        String currentTimeString = sdf_now.format(cal.getTime());

        return currentTimeString;
    }


}