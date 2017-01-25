package edu.umich.si.inteco.minuku.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.umich.si.inteco.minuku.Constants;
import edu.umich.si.inteco.minuku.context.ContextManager;
import edu.umich.si.inteco.minuku.context.ContextStateManagers.ContextStateManager;
import edu.umich.si.inteco.minuku.Fragments.MongoLabHelper;
import edu.umich.si.inteco.minuku.data.RemoteDBHelper;
import edu.umich.si.inteco.minuku.model.Condition;
import edu.umich.si.inteco.minuku.model.Configuration;
import edu.umich.si.inteco.minuku.model.Questionnaire.EmailQuestionnaireTemplate;
import edu.umich.si.inteco.minuku.model.Situation;
import edu.umich.si.inteco.minuku.model.LoggingTask;
import edu.umich.si.inteco.minuku.model.Notification;
import edu.umich.si.inteco.minuku.model.ProbeObjectControl.ActionControl;
import edu.umich.si.inteco.minuku.model.Question;
import edu.umich.si.inteco.minuku.model.Questionnaire.QuestionnaireTemplate;
import edu.umich.si.inteco.minuku.model.Criteria.StateValueCriterion;
import edu.umich.si.inteco.minuku.model.Criteria.TimeCriterion;
import edu.umich.si.inteco.minuku.model.StateMappingRule;
import edu.umich.si.inteco.minuku.model.actions.Action;
import edu.umich.si.inteco.minuku.model.actions.AnnotateAction;
import edu.umich.si.inteco.minuku.model.actions.AnnotateRecordingAction;
import edu.umich.si.inteco.minuku.model.actions.GenerateEmailQuestionnaireAction;
import edu.umich.si.inteco.minuku.model.actions.GeneratingQuestionnaireAction;
import edu.umich.si.inteco.minuku.model.actions.MonitoringSituationAction;
import edu.umich.si.inteco.minuku.model.actions.SavingRecordAction;

public class ConfigurationManager {

	private static final String LOG_TAG = "ConfigurationManager";

	private static final String TEST_FILE_NAME = "new_study.json";

	public static final String CONFIGURATION_FILE_NAME = Constants.CONFIGURATION_FILE_NAME_PARTI;

	//device checking
	public static boolean MINUKU_SERVICE_CHECKIN_ENABLED = true;
	public static boolean MINUKU_SERVICE_CHECKIN_GOOGLE_ANALYTICS_ENABLED = true;
	public static boolean MINUKU_SERVICE_CHECKIN_MONGODB_ENABLED = true;


	public static final String CONFIGURATION_PROPERTIES_ID = "Id";
	public static final String CONFIGURATION_PROPERTIES_STUDY = "Study";
	public static final String CONFIGURATION_PROPERTIES_VERSION = "Version";
	public static final String CONFIGURATION_PROPERTIES_NAME = "Name";
	public static final String CONFIGURATION_PROPERTIES_CONTENT = "Content";
	public static final String CONFIGURATION_PROPERTIES_SOURCE = "Source";
	public static final String CONFIGURATION_PROPERTIES_CONFIGURATION = "Configuration";
	public static final String CONFIGURATION_PROPERTIES_DESCRIPTION = "Description";
	public static final String TASK_PROPERTIES_TIMESTAMP_STRING = "Timestamp_string";
	public static final String TASK_PROPERTIES_CREATED_TIME = "Created_time";
	public static final String TASK_PROPERTIES_START_TIME = "Start_time";
	public static final String TASK_PROPERTIES_END_TIME = "End_time";

	public static final String CONFIGURATION_CATEGORY_CONDITIONS = "Conditions";
	public static final String CONFIGURATION_CATEGORY_ACTION = "Actions";
	public static final String CONFIGURATION_CATEGORY_TASK = "Tasks";
	public static final String CONFIGURATION_CATEGORY_SITUATION = "Situations";
	public static final String CONFIGURATION_CATEGORY_LOGGING = "Logging";
	public static final String CONFIGURATION_CATEGORY_BACKGROUND_LOGGING = "BackgroundLogging";
	public static final String CONFIGURATION_CATEGORY_BACKEND = "Backend";


	public static final String CONFIGURATION_CATEGORY_QUESTIONNAIRE = "Questionnaires";
	public static final String CONFIGURATION_CATEGORY_CONTEXTSOURCE_SETTING = "ContextSourceSetting";
	public static final String CONFIGURATION_CATEGORY_CONTEXTSOURCE_STATE= "ContextSourceState";

    public static final String SERVICE_SETTING_STOP_SERVICE_DURING_MIDNIGHT = "StopServiceDuringMidNight";

	//properties
	public static final String CONDITION_PROPERTIES_STATE = "State";
	public static final String CONDITION_PROPERTIES_SOURCE = "Source";
	public static final String CONDITION_PROPERTIES_RELATIONSHIP = "Relationship";
	public static final String CONDITION_PROPERTIES_PARAMETERS = "Params";
	public static final String CONDITION_PROPERTIES_TARGETVALUE ="TargetValue";
	public static final String CONDITION_PROPERTIES_MEASURE ="Measure";
	public static final String CONDITION_PROPERTIES_VALUE_CRITERION ="Value_Criteria";
	public static final String CONDITION_PROPERTIES_TIME_CRITERION ="Time_Criteria";

	//backgroundRecording
	public static final String BACKGROUND_LOGGING_PROPERTIES_LOGGING_RATE = "Logging_rate";
	public static final String BACKGROUND_LOGGING_PROPERTIES_LOGGING_TASKS = "Logging_tasks";
	public static final String BACKGROUND_LOGGING_PROPERTIES_LOGGING_ENABLED = "Enabled";


	//backend
	public static final String BACKEND_PROPERTIES_SERVICE_NAME = "Service";
	public static final String BACKEND_PROPERTIES_DATABASE_TYPE = "Database_type";
	public static final String BACKEND_PROPERTIES_DATABASE_NAME = "Database_name";
	public static final String BACKEND_PROPERTIES_SERVER_URL = "Server_url";
	public static final String BACKEND_PROPERTIES_SERVER_API = "Server_api";


	//within content

	/**CONTEXT SOURCE PROPERTIES**/
	public static final String CONTEXTSOURCE_SETTING_PROPERTIES_SAMPLING_RATE= "Sampling_rate";

	/**ACTION PROPERTIES**/
	public static final String ACTION_PROPERTIES_ID = "Id";
	public static final String ACTION_PROPERTIES_TYPE= "Type";
	public static final String ACTION_PROPERTIES_NAME= "Name";
	public static final String ACTION_PROPERTIES_EXECUTION_STYLE= "Execution_style";
	public static final String ACTION_PROPERTIES_CONTROL= "Control";
	public static final String ACTION_PROPERTIES_CONTINUITY= "Continuity";
	public static final String ACTION_PROPERTIES_MONITORING_SITUATION = "Monitoring_situation";
	public static final String ACTION_PROPERTIES_LOGGING_TASKS= "Logging_tasks";
	public static final String ACTION_PROPERTIES_QUESTIONNAIRE_ID= "Questionnaire_id";
	public static final String ACTION_PROPERTIES_NOTIFICATION = "Notification";
	public static final String ACTION_PROPERTIES_LAUNCH= "Launch";

	//for Annotate action
	public static final String ACTION_PROPERTIES_ANNOTATE= "Annotate";
	public static final String ACTION_PROPERTIES_ANNOTATE_MODE = "Mode";
	public static final String ACTION_PROPERTIES_ANNOTATE_RECORDING_TYPE = "Recording_type";
	public static final String ACTION_PROPERTIES_VIZUALIZATION_TYPE = "Viz_type";
	public static final String ACTION_PROPERTIES_ANNOTATE_ALLOW_ANNOTATE_IN_PROCESS = "Allow_annotate_in_process";
	public static final String ACTION_PROPERTIES_ANNOTATE_REVIEW_RECORDING = "Review_recording";
	//recording needs user's permission
	public static final String ACTION_PROPERTIES_RECORDING_STARTED_BY_USER  = "Recording_started_by_user";
	//continuity property
	public static final String ACTION_CONTINUITY_PROPERTIES_RATE = "Rate";
	public static final String ACTION_CONTINUITY_PROPERTIES_DURATION = "Duration";

	//notificaiton property
	public static final String ACTION_PROPERTIES_NOTIFICATION_TITLE = "Title";
	public static final String ACTION_PROPERTIES_NOTIFICATION_MESSAGE = "Message";
	public static final String ACTION_PROPERTIES_NOTIFICATION_LAUNCH = "Launch";
	public static final String ACTION_PROPERTIES_NOTIFICATION_TYPE = "Type";

	//within control:trigger
	public static final String ACTION_PROPERTIES_TRIGGER= "Trigger";
	public static final String ACTION_TRIGGER_CLASS_PROPERTIES= "Class";
	public static final String ACTION_TRIGGER_PROPERTIES_SAMPLING_RATE= "Sampling_rate";


	//within control: schedule
	public static final String ACTION_PROPERTIES_SCHEDULE= "Schedule";
	private static Context mContext;
	private static ContextManager mContextManager;
	private static ArrayList<Configuration> mCongiurationList;
	
	public ConfigurationManager(Context context, ContextManager contextManager){

		mContextManager = contextManager;
		mContext = context;
		mCongiurationList = new ArrayList<Configuration>();
		loadConfiguration();
	}


    /**
     * this function allow remotely updating the configuration in the future
     */
    public void updateConfiguration() {

        //update configuration
        //modify the configuration stored in the sharedpreference


        //reload configuration
        loadConfiguration();
    }
	
	/**
	 * When the app is back to active, the app loads configurations from the database
	 */
	public void loadConfiguration() {

        //save string of configuration
        String configurationsStr = "";

        Log.d(LOG_TAG, "[loadConfiguration] 1");


        /** first check if there's a configuraiton in the SharedPreference or not**/
        if ( !PreferenceHelper.getPreferenceString(PreferenceHelper.CONFIGURATIONS, "NA").equals("NA")) {

            Log.d(LOG_TAG, "[loadConfiguration] load configuration from SharedPreference" );
            //there's a configuration already in the SharedPreference, we just load the json file
            //then save the configuration into SharedPreference
            configurationsStr = PreferenceHelper.getPreferenceString(PreferenceHelper.CONFIGURATIONS, "NA");

        }
        else {
            //there has not been a configuration in the SharedPreference, i.e. we need to load from the file and then
            //save it in the SharedPreference

            //load the configuration file
            String filename = TEST_FILE_NAME;

            Log.d(LOG_TAG, "[loadConfiguration] no configuration in the database, load configuration from file.." + filename);
            configurationsStr = new FileHelper(mContext).loadFileFromAsset(filename);
            Log.d(LOG_TAG, "[loadConfiguration] file content is " + configurationsStr);


            //TODO: uncomment this: for testing purpose we need to comment out this so that we can alwasy load configuration from the file
//            PreferenceHelper.setPreferenceBooleanValue(PreferenceHelper.CONFIGURATIONS, configurationsStr);
        }


        Log.d(LOG_TAG, "[loadConfiguration] the configuraiton string being loaded is: " + configurationsStr);

        /**2. then we load the content of the configuration**/
        //
        try {
            //A confuguration file contains one or multiple study, in a JSONArray format. Each JSONObject is a
            // study
            JSONArray studyJSONArray = new JSONArray(configurationsStr);

            for (int i=0; i< studyJSONArray.length(); i++){

                JSONObject studyJSON = studyJSONArray.getJSONObject(i);

                //get the properties of the current study
                int study_id = studyJSON.getInt(CONFIGURATION_PROPERTIES_ID);
                String study_name = studyJSON.getString(CONFIGURATION_PROPERTIES_NAME);
                Log.d(LOG_TAG, "[loadConfiguration]  Now reading the study " + study_id + " : " + study_name);


                //now get configuration JSON
                JSONObject configJSON = studyJSON.getJSONObject(CONFIGURATION_PROPERTIES_CONFIGURATION);

                //get properties of the config
                int id = configJSON.getInt(CONFIGURATION_PROPERTIES_ID);
                int version = configJSON.getInt(CONFIGURATION_PROPERTIES_VERSION);
                String name = configJSON.getString(CONFIGURATION_PROPERTIES_NAME);
                JSONObject content = configJSON.getJSONObject(CONFIGURATION_PROPERTIES_CONTENT);

				//create configuration object
				Configuration config = new Configuration (id, study_id, version, name, content) ;
				mCongiurationList.add(config);

                //Load the content of the configuration
                loadConfigurationContent(config);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.e(LOG_TAG, "[loadConfiguration] Error in ConfigurationStr");
        }
		
	}


	
	/**
	 * The Configuration has a source in JSON format. The function takse a JSON and configurate the app
	 */
	//TODO: we need to change the configutaiton.
	public void loadConfigurationContent(Configuration config) {


		//TODO: just for testing condition and staterule, we create some here.
		//loadTestStateRule();

		//source is in JSON format
		JSONObject content = config.getContent();
		
		Log.d(LOG_TAG, "[loadConfigurationContent]testbackend  load the configuration content of study " + config.getStudyId());

        //load configuration settings
        try {
            if (content.has(ConfigurationManager.SERVICE_SETTING_STOP_SERVICE_DURING_MIDNIGHT)){
                boolean stopServiceDuringMidNight = content.getBoolean(ConfigurationManager.SERVICE_SETTING_STOP_SERVICE_DURING_MIDNIGHT);
                Log.d(LOG_TAG, "stop service at night is" + stopServiceDuringMidNight);
				//write into the preference
				PreferenceHelper.setPreferenceBooleanValue(ConfigurationManager.SERVICE_SETTING_STOP_SERVICE_DURING_MIDNIGHT, stopServiceDuringMidNight);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


		/** Backend **/
		try {

			if (content.has(ConfigurationManager.CONFIGURATION_CATEGORY_BACKEND)){
				JSONObject backendJSON = content.getJSONObject(CONFIGURATION_CATEGORY_BACKEND);
                Log.d(LOG_TAG, "[loadConfigurationContent]testbackend  load the configuration content of study " +backendJSON);
                loadBackendFromJSON(config, backendJSON, config.getStudyId());
            }


		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** load Logging **/
		try {
			if (content.has(ConfigurationManager.CONFIGURATION_CATEGORY_LOGGING)){
				JSONArray loggingJSON = content.getJSONArray(ConfigurationManager.CONFIGURATION_CATEGORY_LOGGING);
				loadLoggingFromJSON(loggingJSON, config.getStudyId());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** load background recording setting **/
		try {
			if (content.has(ConfigurationManager.CONFIGURATION_CATEGORY_BACKGROUND_LOGGING)){
				JSONObject backgroundLogging = content.getJSONObject(ConfigurationManager.CONFIGURATION_CATEGORY_BACKGROUND_LOGGING);
				loadBackgroundLoggingFromJSON(backgroundLogging, config.getStudyId());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** load contexsource mapping **/
		try {
			if (content.has(ConfigurationManager.CONFIGURATION_CATEGORY_CONTEXTSOURCE_STATE)){
				JSONArray statemapping = content.getJSONArray(ConfigurationManager.CONFIGURATION_CATEGORY_CONTEXTSOURCE_STATE);
				loadContextSourceStateMapping(statemapping, config.getStudyId());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/** load contexsource setting **/
		try {
			if (content.has(ConfigurationManager.CONFIGURATION_CATEGORY_CONTEXTSOURCE_SETTING)){
				JSONArray csSetting = content.getJSONArray(ConfigurationManager.CONFIGURATION_CATEGORY_CONTEXTSOURCE_SETTING);
				//TODO: load contextsourcesetting
//				loadContextSourceSettingFromJSON(csSetting, config.getStudyId());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		/** load situations **/
		try {
			if (content.has(ConfigurationManager.CONFIGURATION_CATEGORY_SITUATION)){
                JSONArray situationJSON = content.getJSONArray(ConfigurationManager.CONFIGURATION_CATEGORY_SITUATION);
				loadSituationFromJSON(situationJSON, config.getStudyId());
            }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		/** load actions **/
		try {
            if (content.has(ConfigurationManager.CONFIGURATION_CATEGORY_ACTION)){
                JSONArray actionsJSON = content.getJSONArray(ConfigurationManager.CONFIGURATION_CATEGORY_ACTION);
                loadActionsFromJSON (actionsJSON, config.getStudyId());
            }

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/** load questionnaires**/
		try {

            if (content.has(ConfigurationManager.CONFIGURATION_CATEGORY_QUESTIONNAIRE)){
                JSONArray questionnairesJSON = content.getJSONArray(ConfigurationManager.CONFIGURATION_CATEGORY_QUESTIONNAIRE);
                loadQuestionnairesFromJSON (questionnairesJSON, config.getStudyId());
            }

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	/**
	 * if the study wants background recording separate from the logging task, we need to read the
	 * configuration
	 * @param backgroundLogging
	 * @param study_id
	 */
	public static void loadBackgroundLoggingFromJSON(JSONObject backgroundLogging, int study_id) {

		Log.d(LOG_TAG, "[testBackgroundLogging] load backgroundlogging of study " + study_id);

		try {

			boolean enabled = backgroundLogging.getBoolean(BACKGROUND_LOGGING_PROPERTIES_LOGGING_ENABLED);
			String logging_tasks = backgroundLogging.getString(BACKGROUND_LOGGING_PROPERTIES_LOGGING_TASKS);
			int rate = backgroundLogging.getInt(BACKGROUND_LOGGING_PROPERTIES_LOGGING_RATE);

			//find which logging task the action is associated with
			String [] ids = logging_tasks.split(",");

			//TODO: logging task for background recoridng should be saved in Preference

			//set enabled
			ContextManager.getBackgroundLoggingSetting().setEnabled(enabled);

			//set rate
			ContextManager.getBackgroundLoggingSetting().setLoggingRate(rate);

			//associate situation ids to the monitoring action.
			for (int j=0; j<ids.length; j++){
				int id = Integer.parseInt(ids[j]);
				//add loggingtask ids to background recording
				ContextManager.getBackgroundLoggingSetting().addLoggingTask(id);
			}



			Log.d(LOG_TAG, "[testBackgroundLogging] load backgroundlogging enabled: " +
					ContextManager.getBackgroundLoggingSetting().isEnabled() +
                    "logging tasks: " + ContextManager.getBackgroundLoggingSetting().getLoggingTasks()
					+ " logging: " + ContextManager.getBackgroundLoggingSetting().getLoggingTasks() + " rate: " +
					ContextManager.getBackgroundLoggingSetting().getLoggingRate());


		}
		catch (JSONException e1) {
			e1.printStackTrace();
		}

	}


	/**
	 * if the study wants background recording separate from the logging task, we need to read the
	 * configuration
	 * @param backendJSON
	 * @param study_id
	 */
	public static void loadBackendFromJSON(Configuration config, JSONObject backendJSON, int study_id) {

		try {

            Log.d(LOG_TAG, "[testbackend] server setting:   " +backendJSON);

            String databaseType  = backendJSON.getString(BACKEND_PROPERTIES_DATABASE_TYPE);
			String databaseName  = backendJSON.getString(BACKEND_PROPERTIES_DATABASE_NAME);
			String serviceName  = backendJSON.getString(BACKEND_PROPERTIES_SERVICE_NAME);
			String serviceURL  = backendJSON.getString(BACKEND_PROPERTIES_SERVER_URL);
            String serviceAPI = backendJSON.getString(BACKEND_PROPERTIES_SERVER_API);

            //add this to the configuration. Each study may have its own server
            //TODO: separate settings for different studies
            config.setBackendDatabaseName(databaseName);
            config.setBackendDatabaseType(databaseType);
            config.setBackendService(serviceName);
            config.setBackendServiceURL(serviceURL);

            RemoteDBHelper.setServerChoice(serviceName);
            RemoteDBHelper.setProjectDatabaseName(databaseName);


            //assign API key to the right service
            if (serviceName.equals(RemoteDBHelper.REMOTE_SERVER_MONGOLAB)){
                MongoLabHelper.setMongolabApikey(serviceAPI);
            }else if (serviceName.equals(RemoteDBHelper.REMOTE_SERVER_MICROSOFTAZZURE)) {

            }else if (serviceName.equals(RemoteDBHelper.REMOTE_SERVER_MICROSOFTAZZURE)) {

            }else if (serviceName.equals(RemoteDBHelper.REMOTE_SERVER_GOOGLEAPPENGINE)) {

            }else if (serviceName.equals(RemoteDBHelper.REMOTE_SERVER_AMAZON)) {

            }

			Log.d(LOG_TAG, "[testbackend] server setting:   " + RemoteDBHelper.REMOTE_SERVER_CHOICE  + " : " + RemoteDBHelper.ProjectDatabaseName
			 + "API: " + serviceAPI);


		}
		catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * this funciton looks for "Logging" in the study configuration and loads the configuration for each logging task
	 * @param loggingJSONArray
	 * @param study_id
	 */
	public static void loadLoggingFromJSON(JSONArray loggingJSONArray, int study_id) {

		Log.d(LOG_TAG, "[loadLoggingFromJSON] load logging of study " + study_id);

		for (int i =0; i<loggingJSONArray.length(); i++) {

			JSONObject loggingJSON = null;
			try {

				loggingJSON  = loggingJSONArray.getJSONObject(i);

				//target source in the logging task
				String source = loggingJSON.getString(CONFIGURATION_PROPERTIES_SOURCE);
				int id = loggingJSON.getInt(CONFIGURATION_PROPERTIES_ID );

				//create logging task object
				LoggingTask loggingTask = new LoggingTask(id, source);

				//add logging task to ContextManager.
				ContextManager.addLoggingTask(loggingTask);

			}
			catch (JSONException e1) {
				e1.printStackTrace();
			}


		}

	}


	public void loadContextSourceStateMapping(JSONArray stateMappingJSONArray, int study_id) {

		Log.d(LOG_TAG, "[test SMR] load the contextsource state mapping of study " + study_id);

		for (int i =0; i<stateMappingJSONArray.length(); i++) {

			try {

				/** A stateMapping consists of a set of value crteria and time criteria, which when are met, create a state of the source **/
				JSONObject stateMappingJSON = stateMappingJSONArray.getJSONObject(i);

				int id = stateMappingJSON.getInt(CONFIGURATION_PROPERTIES_ID);
				String stateValue = stateMappingJSON.getString(CONDITION_PROPERTIES_STATE);
				String source = stateMappingJSON.getString(CONDITION_PROPERTIES_SOURCE);

				String contextStateManagerName = ContextManager.getContextStateManagerName(source);
				int sourceType = ContextManager.getSourceTypeFromName(contextStateManagerName, source);

				/** 1 Read StateValueCriteria for each Condition **/
				JSONArray valueCriteria = stateMappingJSON.getJSONArray(CONDITION_PROPERTIES_VALUE_CRITERION);

				//create a list of criterion (criteria) to save all the criteria
				ArrayList<StateValueCriterion> critera  = new ArrayList<StateValueCriterion>();

				//analyze criteria in the JSONArray and create objects to save them
				for (int j=0; j<valueCriteria.length(); j++ ){

					//a stat emapping has  a set of criteria. Only all of the criteria are met we change the value of the state.
					JSONObject valueCriterionJSON = valueCriteria.getJSONObject(j);

					StateValueCriterion stateValueCriterion = new StateValueCriterion();

					/** we first set defaul values in case there's no specified measure and relationship**/

					/** by default (if users don't specify any measure), we assume users want the latest value**/
					String measure = ContextStateManager.CONTEXT_SOURCE_MEASURE_LATEST_ONE;

					/**by default, the user wants relationship be "equal." **/
					String relationship = ContextStateManager.STATE_MAPPING_RELATIONSHIP_EQUAL;

					/** now we get the actual measure **/
					//if the user does specify the measure, we use that measure
					if (valueCriterionJSON.has(CONDITION_PROPERTIES_MEASURE)){
						//we conver the string into int
						measure = valueCriterionJSON.getString(CONDITION_PROPERTIES_MEASURE);
						stateValueCriterion.setMeasure(measure);
					}

					if (valueCriterionJSON.has(CONDITION_PROPERTIES_RELATIONSHIP)){
						//we conver the string into a int number
						relationship = valueCriterionJSON.getString(CONDITION_PROPERTIES_RELATIONSHIP);
						stateValueCriterion.setRelationship(relationship);

					}

					// target value
					stateValueCriterion.setTargetValue(valueCriterionJSON.get(CONDITION_PROPERTIES_TARGETVALUE));


					// some measure may have additional parameters (e.g. Locaiton has a reference point)
					if (valueCriterionJSON.has(CONDITION_PROPERTIES_PARAMETERS)) {

						JSONArray params = valueCriterionJSON.getJSONArray(CONDITION_PROPERTIES_PARAMETERS);

						for (int k=0; k<params.length(); k++){
							stateValueCriterion.addParameter(params.getString(k));
						}

					}

					Log.d(LOG_TAG, "[test SMR] statevaluecriterio:  " +  source  + " " +stateValueCriterion.getMeasure()+ " : " + stateValueCriterion.getRelationship()
							+ " target value: " + stateValueCriterion.getTargetValue().toString());

					if (stateValueCriterion.getParameters()!=null)
						Log.d(LOG_TAG, "[test SMR] statevalue criterioa parameter " + stateValueCriterion.getParameters().toString());


					//after reading all criteria, we add it.
					critera.add(stateValueCriterion);

				}


				//add criteria to the stateMappingRule
				StateMappingRule stateMappingRule = new StateMappingRule(id, contextStateManagerName, sourceType, source, critera, stateValue);



				/** 2. Read TimeCriteria for Condition, if there's one  **/
				if (stateMappingJSON.has(CONDITION_PROPERTIES_TIME_CRITERION)){

					ArrayList<TimeCriterion> timeCriteria = new ArrayList<TimeCriterion>();
					try {

						//time criterion specificies how recently Minuku observes that state and how long it observes the state.
						JSONArray timeCriteriaJSONArray = stateMappingJSON.getJSONArray(CONDITION_PROPERTIES_TIME_CRITERION);

						for (int k = 0; k < timeCriteriaJSONArray.length(); k++){

							JSONObject timeCriterion = timeCriteriaJSONArray.getJSONObject(k);

							String measure = timeCriterion.getString(CONDITION_PROPERTIES_MEASURE);
							String relationship = timeCriterion.getString(CONDITION_PROPERTIES_RELATIONSHIP);
							float value = Float.parseFloat(timeCriterion.getString(CONDITION_PROPERTIES_TARGETVALUE))  ;

							Log.d(LOG_TAG, "[test SMR] the condition of the time criteria is  measure: " + measure + " " + relationship + " " + value);

							timeCriteria.add( new TimeCriterion(measure, relationship, value));
						}
					}catch (JSONException e2) {
						e2.printStackTrace();
					}

					//add timecriteria to the condition
					stateMappingRule.setTimeCriteria(timeCriteria);
				}

				//add statemappingRule
				ContextManager.addStateMappingRule(stateMappingRule);

			}
			catch (JSONException e1) {
				e1.printStackTrace();
			}

		}

	}



	/**
	 * users only list contextsources that are not using the default setting
	 * @param settingJSONArray
	 * @param study_id
	 */
	public void loadContextSourceSettingFromJSON(JSONArray settingJSONArray, int study_id) {

		Log.d(LOG_TAG, "[loadContextSourceSettingFromJSON] load the contextsourcesetting of study " + study_id);

		for (int i =0; i<settingJSONArray.length(); i++) {

			JSONObject settingJSON = null;

			try {

				settingJSON  = settingJSONArray.getJSONObject(i);

				//every source setting should specify a source. If a source is not listed, we use the default
				String source = settingJSON.getString(CONFIGURATION_PROPERTIES_SOURCE);

				//the defaul is -1, i.e. using the default value.
				int sampling_rate = -1;

				//get sampling rate
				if (settingJSON.has(CONTEXTSOURCE_SETTING_PROPERTIES_SAMPLING_RATE)){
					sampling_rate = settingJSON.getInt(CONTEXTSOURCE_SETTING_PROPERTIES_SAMPLING_RATE);
				}

				//update the setting based on the name of the source
				mContextManager.configureContextStateSource(source, sampling_rate * Constants.MILLISECONDS_PER_SECOND);

			}
			catch (JSONException e1) {
				e1.printStackTrace();
			}


		}

	}


	/**
	 *
	 * @param situationsJSON
	 * @param study_id
	 */
	public static void loadSituationFromJSON(JSONArray situationsJSON, int study_id){


		for (int i = 0; i < situationsJSON.length(); i++){
			
			Situation situation = null;
			JSONObject situationJSON = null;
			
			try {
				situationJSON = situationsJSON.getJSONObject(i);
				
				int id= situationJSON.getInt(CONFIGURATION_PROPERTIES_ID);
				String name = situationJSON.getString(CONFIGURATION_PROPERTIES_NAME);
				String description = situationJSON.getString(CONFIGURATION_PROPERTIES_DESCRIPTION);

				//creat the situation object
				situation = new Situation(id, name, study_id);
				situation.setDescription(description);

				ArrayList<Condition> conditions = new ArrayList<Condition>();

				String conditionStr  = situationJSON.getString(CONFIGURATION_CATEGORY_CONDITIONS);

				String [] ids = conditionStr.split(",");

				//associate situation ids to the monitoring action.
				for (int j=0; j<ids.length; j++){

					int statemappingId = Integer.parseInt(ids[j]);

					Log.d(LOG_TAG, "[test SMR] sitution " + situation.getName() + " trying to find condition for mapping rule " + statemappingId);

					for (int k=0; k<ContextManager.getStateMappingRuleList().size(); k++) {

						StateMappingRule rule= ContextManager.getStateMappingRuleList().get(k);
						//find the statemappingrule so taht we can see which state the condition should be monitoring
						if (rule.getId()==statemappingId){
							//find the statemapping rule

							Condition condition = new Condition();
							condition.setStateName(rule.getName());
							condition.setSourceType(rule.getSourceType());
							condition.setSource(rule.getSource());
							condition.setStateTargetValue(rule.getStateValue());
							condition.setStateMappingRule(rule);

							conditions.add(condition);
//							Log.d(LOG_TAG, "[test SMR] situation  " + situation.getName() +
//									" add condition " + condition.getStateName()  + " source " + condition.getSourceType()
//							  + " " + condition.getStateTargetValue());

						}

					}
				}

				situation.setConditionList(conditions);


			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			/** after creating the situation object, add situation to situationList, and to the databasse..**/
			//add to the list
			ContextManager.addSituation(situation);

		}//end of reading situationJSONArray
	}
	
	
	/**
	 *
     */
	public static void loadActionsFromJSON(JSONArray actionJSONArray, int study_id) {

		Log.d(LOG_TAG, " [loadActionsFromJSON] there are "  +   actionJSONArray.length() + " actions" );
		
		//load details for each action
		for (int i = 0; i < actionJSONArray.length(); i++){
			
			JSONObject actionJSON = null;
			JSONObject controlJSON = null;				
			
			Action action=null;
			
			try {
				
				//get JSON for action and schedule within the action
				actionJSON = actionJSONArray.getJSONObject(i);

				/** 1. first create action and schedule object based on the required field, then set propoerties based on the schedule type**/
				
				//get action required fields
				int action_id= actionJSON.getInt(ACTION_PROPERTIES_ID);
				String type = actionJSON.getString(ACTION_PROPERTIES_TYPE);
				String execution_style = actionJSON.getString(ACTION_PROPERTIES_EXECUTION_STYLE);
				controlJSON = actionJSON.getJSONObject(ACTION_PROPERTIES_CONTROL);
				String name = actionJSON.getString(ACTION_PROPERTIES_NAME);
				
				Log.d(LOG_TAG, "[test sampling][loadActionsFromJSON] examine action" + " action: " + action_id + " , for type " + type
						+ " execution style " + execution_style );
				
				
				action = new Action (action_id, name, type, execution_style, study_id);

				/** 2 We generate actions based on the type. Different actions have different properties**/
				//Action of phone questionnaire
				if (type.equals(ActionManager.ACTION_TYPE_QUESTIONNAIRE)){
					
					int questionnaire_id = actionJSON.getInt(ACTION_PROPERTIES_QUESTIONNAIRE_ID);
					GeneratingQuestionnaireAction a = new GeneratingQuestionnaireAction (action_id, name, type,execution_style, study_id);
					a.setQuestionnaireId(questionnaire_id);
					action = a; 
					Log.d(LOG_TAG, "[test sampling] the aciton" + action.getId() + " questionnaire id:  " + a.getQuestionnaireId());
					
				}
				//Action of email questionnaire
                else if (type.equals(ActionManager.ACTION_TYPE_EMAIL_QUESTIONNAIRE)){

                    int questionnaire_id = actionJSON.getInt(ACTION_PROPERTIES_QUESTIONNAIRE_ID);
                    GenerateEmailQuestionnaireAction a = new GenerateEmailQuestionnaireAction (action_id, name, type,execution_style, study_id);
                    a.setQuestionnaireId(questionnaire_id);
                    action = a;

                    Log.d(LOG_TAG, "[loadActionsFromJSON] examine action" + " action: " + action_id + " , for type " + type
                            + " execution style " + execution_style );

                }
				
				////Action of monitoring situations. We associate situation ids with the action.
				else if (type.equals(ActionManager.ACTION_TYPE_MONITORING_SITUATION)){
					
					String monitor_situation_ids = actionJSON.getString(ACTION_PROPERTIES_MONITORING_SITUATION);
					String [] ids = monitor_situation_ids.split(",");
					
					MonitoringSituationAction a = new MonitoringSituationAction (action_id, name, type, execution_style, study_id);

					//associate situation ids to the monitoring action.
					for (int j=0; j<ids.length; j++){
						int id = Integer.parseInt(ids[j]);
						a.addMonitoredCircumstance(id);
						Log.d(LOG_TAG, " [loadActionsFromJSON] the aciton" + action.getId() + " monitors situation  "  +  id);

					}
					
					action  = a;

				}

                //Action of saving record
                else if (type.equals(ActionManager.ACTION_TYPE_SAVING_RECORD)) {

					String logging_task_ids = actionJSON.getString(ACTION_PROPERTIES_LOGGING_TASKS);
					//find which logging task the action is associated with
					String [] ids = logging_task_ids.split(",");

                    SavingRecordAction a = new SavingRecordAction(action_id,name, type,execution_style, study_id );

					//associate situation ids to the monitoring action.
					for (int j=0; j<ids.length; j++){
						int id = Integer.parseInt(ids[j]);
						a.addLoggingTask(id);
						Log.d(LOG_TAG, " [loadActionsFromJSON] the aciton" + action.getId() + " is associated with loggin task  "  +  id);

					}

                    action = a;
                }

                //Action of annotating data (no saving records)
                else if (type.equals(ActionManager.ACTION_TYPE_ANNOTATE)) {

                    JSONObject annotateJSON = actionJSON.getJSONObject(ACTION_PROPERTIES_ANNOTATE);

                    String mode = annotateJSON.getString(ACTION_PROPERTIES_ANNOTATE_MODE);
                    String vizType = annotateJSON.getString(ACTION_PROPERTIES_VIZUALIZATION_TYPE);
                    String reviewRecordingMode = annotateJSON.getString(ACTION_PROPERTIES_ANNOTATE_REVIEW_RECORDING);

                    AnnotateAction a = new AnnotateAction (action_id, name, type, execution_style, study_id, mode, vizType, reviewRecordingMode);
                    action = a;
                }

                //Action of saving and apply annotation together.
				//TODO: check if we can make this cleaner.
                else if (type.equals(ActionManager.ACTION_TYPE_ANNOTATE_AND_RECORD)) {

                    JSONObject annotateJSON = actionJSON.getJSONObject(ACTION_PROPERTIES_ANNOTATE);

                    String mode = annotateJSON.getString(ACTION_PROPERTIES_ANNOTATE_MODE);
                    String recordingType = annotateJSON.getString(ACTION_PROPERTIES_ANNOTATE_RECORDING_TYPE);
                    String vizType = annotateJSON.getString(ACTION_PROPERTIES_VIZUALIZATION_TYPE);
                    boolean allowAnnotateInProcess = annotateJSON.getBoolean(ACTION_PROPERTIES_ANNOTATE_ALLOW_ANNOTATE_IN_PROCESS);
                    String reviewRecordingMode = annotateJSON.getString(ACTION_PROPERTIES_ANNOTATE_REVIEW_RECORDING);
                    boolean recordingStartByUser = annotateJSON.getBoolean(ACTION_PROPERTIES_RECORDING_STARTED_BY_USER);

                    AnnotateRecordingAction annotateRecordingAction =
                            new AnnotateRecordingAction(
                                    action_id,
                                    name,
                                    type,
                                    execution_style,
                                    study_id,
                                    mode,
                                    recordingType,
                                    vizType,
                                    allowAnnotateInProcess,
                                    reviewRecordingMode,
                                    recordingStartByUser);

                    Log.d(LOG_TAG, "[loadActionsFromJSON] the action is annotateRecording, mode: "
                            + mode + " recordType: " + recordingType + " vizType " + vizType + " allowannotate " + allowAnnotateInProcess + " review recording: " + reviewRecordingMode
                             + " recording start by user " + recordingStartByUser
                            );


                    action  = annotateRecordingAction;

                }


				/**4. examine whether the action is continuous or not**/
				if (actionJSON.has(ACTION_PROPERTIES_CONTINUITY)){
					
					JSONObject continuityJSON = actionJSON.getJSONObject(ACTION_PROPERTIES_CONTINUITY);
					Log.d(LOG_TAG, "[loadActionsFromJSON] the continuityJSON:  " + continuityJSON.toString());
					
					float rate= (float) continuityJSON.getDouble(ACTION_CONTINUITY_PROPERTIES_RATE);
					int duration = continuityJSON.getInt(ACTION_CONTINUITY_PROPERTIES_DURATION);
					
					action.setActionDuration(duration);
					action.setActionRate(rate);
					action.setContinuous(true);
					Log.d(LOG_TAG, "[loadActionsFromJSON] the action " + action.getId() + " is continuous " + action.isContinuous() + " rate: "
							+ action.getActionRate()  +" duration  " + action.getActionDuration());
					
					
					
				}else {
					action.setContinuous(false);
					Log.d(LOG_TAG, "[loadActionsFromJSON] the action " + action.getId() + " is not continuous " + action.isContinuous() );
				}
				
			
				
				/**5. check whether there are notification of the action **/			
				if (actionJSON.has(ACTION_PROPERTIES_NOTIFICATION)){

                    //notification is an array. It may have a typical notification and a ongoing notifications
                    JSONArray notiJSONArray = actionJSON.getJSONArray(ACTION_PROPERTIES_NOTIFICATION);

                    for (int j=0; j<notiJSONArray.length(); j++){

                        JSONObject notiJSONObject  = notiJSONArray.getJSONObject(j);

						//notification has type, launch style, title, and message.
						//TODO: improve the notification style.
                        String notiType = notiJSONObject.getString(ACTION_PROPERTIES_NOTIFICATION_TYPE);
                        String notiLaunch = notiJSONObject.getString(ACTION_PROPERTIES_NOTIFICATION_LAUNCH);
                        String notiTitle = notiJSONObject.getString(ACTION_PROPERTIES_NOTIFICATION_TITLE);
                        String notiMessage = notiJSONObject.getString(ACTION_PROPERTIES_NOTIFICATION_MESSAGE);

                        Notification notification = new Notification(notiLaunch, notiType, notiTitle, notiMessage);

						//add notification to the action
                        action.addNotification(notification);
                    }


				}				
				
				/** 6 load controls to actions**/					
				loadActionControlsFromJSON (controlJSON, action);

			}catch (JSONException e1) {

				e1.printStackTrace();
			}

			
			if (action!=null){					
				//add action into the list
				ActionManager.getActionList().add(action);
			}
		}//for each action
	}
	
	
	/**
	 * 
	 * @param conditionJSONArray
	 * @return
	 */
//	public static ArrayList<Condition> loadConditionsFromJSON(JSONArray conditionJSONArray) {
//
//
//		 ArrayList<Condition> conditions = new  ArrayList<Condition>();
//
//
////			Log.d(LOG_TAG, "[test situation] the conditions of the current situation is:  " + conditionJSONArray.toString());
//
//			for (int i = 0; i < conditionJSONArray.length(); i++){
//
//				try {
//
//					//a condition is statified when a specified value of the a state meet the criteria
//					JSONObject conditionJSON = conditionJSONArray.getJSONObject(i);
//
//
////					String stateValue = conditionJSON.getString(CONDITION_PROPERTIES_STATE);
////					String source = conditionJSON.getString(CONDITION_PROPERTIES_SOURCE);
////
////
////					//create condition object
////					Condition condition = new Condition(source,  stateValue);
////
////
//////					/** 1 Read StateValueCriteria for each Condition **/
////					//a condition is met when a set of criteria is met
////					//a condition may have an additional set of time criteria.
////					JSONArray valueCriteria = conditionJSON.getJSONArray(CONDITION_PROPERTIES_VALUE_CRITERION);
////
////					//create a list of criterion (criteria) to save all the criteria
////					ArrayList<StateValueCriterion> critera  = new ArrayList<StateValueCriterion>();
////
////					//analyze criteria in the JSONArray and create objects to save them
////					for (int j=0; j<valueCriteria.length(); j++ ){
////
////						//a condition have a set of criteria.
////
////						JSONObject valueCriterionJSON = valueCriteria.getJSONObject(j);
////						StateValueCriterion stateValueCriterion = new StateValueCriterion();
////
////						//we specify default values for measures and relationships
////
////
////						/** by default (if users don't specify any measure), we assume users want the latest value**/
////						String measure = ContextStateManager.CONTEXT_SOURCE_MEASURE_LATEST_ONE;
////
////						/**by default, the user wants relationship be "equal." **/
////						String relationship = ContextStateManager.STATE_MAPPING_RELATIONSHIP_EQUAL;
////
////						/** now we get the measure **/
////						//if the user does specify the measure, we use that measure
////						if (valueCriterionJSON.has(CONDITION_PROPERTIES_MEASURE)){
////							//we conver the string into int
////							measure = valueCriterionJSON.getString(CONDITION_PROPERTIES_MEASURE);
////							stateValueCriterion.setMeasure(measure);
////						}
////
////						if (valueCriterionJSON.has(CONDITION_PROPERTIES_RELATIONSHIP)){
////							//we conver the string into a int number
////							relationship = valueCriterionJSON.getString(CONDITION_PROPERTIES_RELATIONSHIP);
////							stateValueCriterion.setRelationship(relationship);
////
////						}
////
////						/**create criterion object based on the type of target value**/
////
////						//if the target value is numeric, we use float number, otherwise we save it as a String value
//////						if (isNumeric(valueCriterionJSON.getString(CONDITION_PROPERTIES_TARGETVALUE))){
//////							float targetValue = (float)valueCriterionJSON.getDouble(CONDITION_PROPERTIES_TARGETVALUE);
//////							stateValueCriterion.setTargetValue(targetValue);
//////
//////
//////						}
//////						//if the target value is not a number, it's a string
//////						else{
//////							String targetValue = valueCriterionJSON.getString(CONDITION_PROPERTIES_TARGETVALUE);
//////							stateValueCriterion.setTargetValue(targetValue);
//////
//////						}
////
////						stateValueCriterion.setTargetValue(valueCriterionJSON.get(CONDITION_PROPERTIES_TARGETVALUE));
////
////
////						if (valueCriterionJSON.has(CONDITION_PROPERTIES_PARAMETERS)) {
////
////							JSONArray params = valueCriterionJSON.getJSONArray(CONDITION_PROPERTIES_PARAMETERS);
////
////							for (int k=0; k<params.length(); k++){
////								stateValueCriterion.addParameter(params.getString(k));
////							}
////
////						}
////
////						Log.d(LOG_TAG, "[test situation] statevaluecriterio:  " +  source  + " " +stateValueCriterion.getMeasure()+ " : " + stateValueCriterion.getRelationship()
////						 + " target value: " + stateValueCriterion.getTargetValue().toString());
////
////						if (stateValueCriterion.getParameters()!=null)
////							Log.d(LOG_TAG, "[test situation] statevalue criterioa parameter " + stateValueCriterion.getParameters().toString());
////
////
////						//after reading all criteria, we add it.
////						critera.add(stateValueCriterion);
////
////					}
//
////					/** 2. add criteria to the Condition **/
////					condition.setValueCriteria(critera);
//
//
////					/** 3. Read TimeeCriteria for Condition, if there's one  **/
////					if (conditionJSON.has(CONDITION_PROPERTIES_TIME_CRITERION)){
////
////						ArrayList<TimeCriterion> timeCriteria = new ArrayList<TimeCriterion>();
////						try {
////
////							//time criterion specificies how recently Minuku observes that state and how long it observes the state.
////							JSONArray timeCriteriaJSONArray = conditionJSON.getJSONArray(CONDITION_PROPERTIES_TIME_CRITERION);
////
////							for (int k = 0; k < timeCriteriaJSONArray.length(); k++){
////
////								JSONObject timeCriterion = timeCriteriaJSONArray.getJSONObject(k);
////
////								String measure = timeCriterion.getString(CONDITION_PROPERTIES_MEASURE);
////								String relationship = timeCriterion.getString(CONDITION_PROPERTIES_RELATIONSHIP);
////								float value = Float.parseFloat(timeCriterion.getString(CONDITION_PROPERTIES_TARGETVALUE))  ;
////
////								Log.d(LOG_TAG, "[test situation] the condition of the time criteria is  measure: " + measure + " " + relationship + " " + value);
////
////								timeCriteria.add( new TimeCriterion(measure, relationship, value));
////							}
////						}catch (JSONException e2) {
////							e2.printStackTrace();
////						}
////
////						//add timecriteria to the condition
////						condition.setTimeCriteria(timeCriteria);
////					}
////
////
////					//finally we add condition to the conditionlist
////					conditions.add(condition);
////
//				}catch (JSONException e) {
//
//					e.printStackTrace();
//				}
//
//			}//end of reading conditionJSONArray
//
//		//Log.d(LOG_TAG, "[loadConditionsFromJSON] the current situation has " + conditions.size() + " condition");
//		return conditions;
//
//	}



	
	/***
	 * read controlJSON and add control objects to the action
	 * @param controlJSON
	 * @param action
	 */
	public static void loadActionControlsFromJSON (JSONObject controlJSON, Action action) {

		/** if the action control is to START an action. Most action controls belong to this type. **/
		if (controlJSON.has(ActionManager.ACTION_CONTROL_TYPE_START_STRING)){

			//it's an array because there could be many ways to start an Action.
			JSONArray startJSONArray = null;

			try {
				startJSONArray = controlJSON.getJSONArray(ActionManager.ACTION_CONTROL_TYPE_START_STRING);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (startJSONArray!=null){

				//load all the possible ways of starting the action
				for (int i = 0; i < startJSONArray.length(); i++){
					
					JSONObject startJSONObject=null;

					try {
						startJSONObject = startJSONArray.getJSONObject(i);

						//instantiate an Action Control with type "Start"
						//set id based on the number of existing action contorl
						int id = ActionManager.getActionControlList().size()+1;
						
						//create an ActionControl object. When creating, the ActionControl will setup its own
						ActionControl ac = new ActionControl (id, startJSONObject, ActionManager.ACTION_CONTROL_TYPE_START, action);
						
						//add the ActionControl object to the list
						ActionManager.getActionControlList().add(ac);

						
						Log.d(LOG_TAG, "[loadActionControlsFromJSON]  the start acitonControl id is " + ac.getId() + " connects to action " + ac.getAction().getId() + " " + ac.getAction().getName() +
								" and has schedule : " + ac.getSchedule());

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}
		/** if the action control is to STOP an action **/
		if (controlJSON.has(ActionManager.ACTION_CONTROL_TYPE_STOP_STRING)) {
			
			JSONArray stopJSONArray;
			try {
				stopJSONArray = controlJSON.getJSONArray(ActionManager.ACTION_CONTROL_TYPE_STOP_STRING);
				
				for (int i = 0; i < stopJSONArray.length(); i++){
					
					JSONObject stopJSONObject = stopJSONArray.getJSONObject(i);
					//instantiate an Action Control with type "Stop"
					int id = ActionManager.getActionControlList().size()+1;
					ActionControl ac = new ActionControl (id, stopJSONObject, ActionManager.ACTION_CONTROL_TYPE_STOP, action);
					ActionManager.getActionControlList().add(ac);
					
					Log.d(LOG_TAG, "[loadActionControlsFromJSON]  the stop acitonControl id is " + ac.getId() + " connects to action " + ac.getAction().getId() + 
							" and has schedule : " + ac.getSchedule());
					
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//if an action control is to pause an action 
		if (controlJSON.has(ActionManager.ACTION_CONTROL_TYPE_PAUSE_STRING)) {
			
			JSONArray pauseJSONArray;		
			
			try {
				pauseJSONArray = controlJSON.getJSONArray(ActionManager.ACTION_CONTROL_TYPE_PAUSE_STRING);
				
				Log.d(LOG_TAG, "[loadActionControlsFromJSON] found pause JSON " +  pauseJSONArray);
				
				
				for (int i = 0; i < pauseJSONArray.length(); i++){
					
					JSONObject pauseJSONObject = pauseJSONArray.getJSONObject(i);
					//instantiate an Action Control with type "Pause"
					int id = ActionManager.getActionControlList().size()+1;
					ActionControl ac = new ActionControl (id, pauseJSONObject, ActionManager.ACTION_CONTROL_TYPE_PAUSE, action);
					ActionManager.getActionControlList().add(ac);
					
					Log.d(LOG_TAG, "[loadActionControlsFromJSON]  the pause acitonControl id is " + ac.getId() + " connects to action " + ac.getAction().getId() + 
							" and has schedule : " + ac.getSchedule());
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//if the action control is to resume an action 
		if (controlJSON.has(ActionManager.ACTION_CONTROL_TYPE_RESUME_STRING)) {
			
			JSONArray resumeJSONArray;
			try {
				resumeJSONArray = controlJSON.getJSONArray(ActionManager.ACTION_CONTROL_TYPE_RESUME_STRING);
				
				Log.d(LOG_TAG, "[loadActionControlsFromJSON] found resume JSON " +  resumeJSONArray);
				
				
				for (int i = 0; i < resumeJSONArray.length(); i++){
					
					JSONObject resumeJSONObject = resumeJSONArray.getJSONObject(i);
					//instantiate an Action Control with type "Resume"
					int id = ActionManager.getActionControlList().size()+1;
					ActionControl ac = new ActionControl (id, resumeJSONObject, ActionManager.ACTION_CONTROL_TYPE_RESUME, action);			
					ActionManager.getActionControlList().add(ac);
					
					Log.d(LOG_TAG, "[loadActionControlsFromJSON]  the resume acitonControl id is " + ac.getId() + " connects to action " + ac.getAction().getId() + 
							" and has schedule : " + ac.getSchedule());
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//if the action control is to cancel an action
		if (controlJSON.has(ActionManager.ACTION_CONTROL_TYPE_CANCEL_STRING)) {
			
			JSONArray cancelJSONArray;
			try {
				cancelJSONArray = controlJSON.getJSONArray(ActionManager.ACTION_CONTROL_TYPE_CANCEL_STRING);
				
				for (int i = 0; i < cancelJSONArray.length(); i++){
					
					JSONObject cancelJSONObject = cancelJSONArray.getJSONObject(i);
					//instantiate an Action Control with type "Cancel"
					int id = ActionManager.getActionControlList().size()+1;
					ActionControl ac = new ActionControl (id, cancelJSONObject, ActionManager.ACTION_CONTROL_TYPE_CANCEL, action);
					ActionManager.getActionControlList().add(ac);
					
					Log.d(LOG_TAG, "[loadActionControlsFromJSON]  the acitonControl id is " + ac.getId() + " connects to action " + ac.getAction().getId() + 
							" and has schedule : " + ac.getSchedule());
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Log.d(LOG_TAG, "[loadActionControlsFromJSON] after reading action " + action.getId() + " the service has " + ActionManager.getActionControlList().size() + " actioncontrols");
		
	}
	

	

	/**
	 * Load Questions from JSONArray
	 * @param questionnJSONArray
	 * @return
	 */
	private static ArrayList<Question> loadQuestionsFromJSON(JSONArray questionnJSONArray){
		
		
		Log.d(LOG_TAG, "the questionJSONArray is " + questionnJSONArray.toString());
		
		ArrayList<Question> questions = new ArrayList<Question>();
		
		for (int i = 0; i < questionnJSONArray.length(); i++){
			
			JSONObject questionJSON=null;
			Question question = null;

			try {
				
				questionJSON = questionnJSONArray.getJSONObject(i);
				
				
				int index = questionJSON.getInt(QuestionnaireManager.QUESTION_PROPERTIES_INDEX);
				String type = questionJSON.getString(QuestionnaireManager.QUESTION_PROPERTIES_TYPE);
				String question_text = questionJSON.getString(QuestionnaireManager.QUESTION_PROPERTIES_QUESTION_TEXT);
					
				
				question = new Question(index, question_text, type);
				
				Log.d (LOG_TAG, "[loadQuestionsFromJSON]  the question object is " + question.getIndex() + " text: " + question.getText() + " type " + question.getType());
					
				//get options (e.g. multi choice
				if (questionJSON.has(QuestionnaireManager.QUESTION_PROPERTIES_OPTION)){
					
					JSONArray optionJSONArray=null;		
					ArrayList<String> options = new ArrayList<String>();
					
					optionJSONArray = questionJSON.getJSONArray(QuestionnaireManager.QUESTION_PROPERTIES_OPTION);
					
					Log.d (LOG_TAG, "[loadQuestionsFromJSON] the question also has "  + optionJSONArray.length() + " options"); 
							
					for (int j=0; j<optionJSONArray.length(); j++){
						
						JSONObject optionJSON = optionJSONArray.getJSONObject(j);
						
						String option_text = optionJSON.getString(QuestionnaireManager.QUESTION_PROPERTIES_OPTION_TEXT);
						options.add(option_text);
						
						Log.d (LOG_TAG, " option " + j + " : "  + option_text); 
						
					}
					
					question.setOptions(options);

				}

				//other fields of the question...
				if (questionJSON.has(QuestionnaireManager.QUESTION_PROPERTIES_HAS_OTHER_FIELD)){

					question.setHasOtherField(questionJSON.getBoolean(QuestionnaireManager.QUESTION_PROPERTIES_HAS_OTHER_FIELD));
					
				}

                //the questions has dynamic content that needs to be extracted from the database
                if (questionJSON.has(QuestionnaireManager.QUESTION_PROPERTIES_DATA)){

                    question.setDataJSON(questionJSON.getJSONObject(QuestionnaireManager.QUESTION_PROPERTIES_DATA));

                }
				
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			questions.add(question);
			
		}
			
		
		
		
		
		
		return questions;
				
	}
	
	
	/**
	 * Load Questionnaire from JSON file
	 * @param study_id
	 */
	private static void loadQuestionnairesFromJSON(JSONArray questionnaireJSONArray, int study_id){
				
		Log.d(LOG_TAG, "loadQuestionnaireFromJSON there are "  +   questionnaireJSONArray.length() + " questionnaires" );

		//load details for each action
		for (int i = 0; i < questionnaireJSONArray.length(); i++){
			
			JSONObject questionnaireJSON;
			JSONArray questionsJSONArray;
			
			QuestionnaireTemplate questionnaireTemplate;
			ArrayList<Question> questions = new ArrayList<Question> ();
			
			try {

				questionnaireJSON = questionnaireJSONArray.getJSONObject(i);

				int id = questionnaireJSON.getInt(QuestionnaireManager.QUESTIONNAIRE_PROPERTIES_ID);
				String title = questionnaireJSON.getString(QuestionnaireManager.QUESTIONNAIRE_PROPERTIES_TITLE);
                String type = questionnaireJSON.getString(QuestionnaireManager.QUESTIONNAIRE_PROPERTIES_TYPE);
				questionsJSONArray = questionnaireJSON.getJSONArray(QuestionnaireManager.QUESTIONNAIRE_PROPERTIES_QUESTIONS);

                questionnaireTemplate = new QuestionnaireTemplate(id, title, study_id, type);

				if (questionnaireJSON.has(QuestionnaireManager.QUESTIONNAIRE_PROPERTIES_DESCRIPTION)){
					String description = questionnaireJSON.getString(QuestionnaireManager.QUESTIONNAIRE_PROPERTIES_DESCRIPTION);
					questionnaireTemplate.setDescription(description);					
				}

				//save to database


                //if the questionnaire is through email
				if (type.equals(QuestionnaireManager.QUESTIONNAIRE_TYPE_EMAIL)) {

                    EmailQuestionnaireTemplate template = new EmailQuestionnaireTemplate (id, title, study_id, type);

                    //the questionnaire shoud has "Email" field
                    JSONObject emailJSON = questionnaireJSON.getJSONObject(QuestionnaireManager.QUESTIONNAIRE_PROPERTIES_EMAIL);
                    JSONArray recipientsJSONArray = emailJSON.getJSONArray(QuestionnaireManager.QUESTIONNAIRE_EMAIL_PROPERTIES_RECIPIENTS);
                    String subject = emailJSON.getString(QuestionnaireManager.QUESTIONNAIRE_EMAIL_PROPERTIES_SUBJECT);

                    //get recipients from the JSONArray
                    ArrayList<String> recipients = new ArrayList<String>();
                    for (int j=0; j<recipientsJSONArray.length(); j++){
                        String recipient = recipientsJSONArray.getString(j);
                        recipients.add(recipient);
                    }

                    //convert arraylist to String[]
                    String [] re = new String [recipients.size()];
                    recipients.toArray(re);


                    //add information to the template
                    template.setSubject(subject);
                    template.setRecipients(re);

                    //referenc the template back to this emailTemplate
                    questionnaireTemplate = template;
                }


				//read Questions..
				questions = loadQuestionsFromJSON(questionsJSONArray);
				
				//add questions to the questionnaire
				questionnaireTemplate.setQuestions(questions);
				QuestionnaireManager.addQuestionnaireTemplate(questionnaireTemplate);

				
			} catch (JSONException e1) {

			}
			
			
		}

	}

	public static boolean isNumeric(String str)
	{
		try
		{
			double d = Double.parseDouble(str);
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
		return true;
	}


	
}
