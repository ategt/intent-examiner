package com.example.ateg.intentexperiments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

/**
 * Created by ATeg on 10/20/2017.
 */

class MainPresenter extends BasePresenter<MainView>{

    private IntentWrapperServices intentWrapperServices;
    private PreferencesServices preferencesServices;

    public MainPresenter(MainView viewInstance,
                         IntentWrapperServices intentWrapperServices,
                         PreferencesServices preferencesServices) {
        super(viewInstance);
        this.intentWrapperServices = intentWrapperServices;
        this.preferencesServices = preferencesServices;
        init();
    }

    private void init() {
        new AsyncTask<IntentWrapperServices, Void, Void>() {
            @Override
            protected Void doInBackground(IntentWrapperServices... intentWrapperServices) {
                for (IntentWrapperServices intentWrapperServices1 : intentWrapperServices){
                    intentWrapperServices1.init();
                }
                return null;
            }
        }.execute(intentWrapperServices);
    }

    public void resetPreferences() {
        PreferencesUtilites preferencesUtilites = new PreferencesUtilites(
                PreferencesUtilites.getDefaultPreferences(((Fragment)getView()).getActivity()));
        preferencesUtilites.resetPreferences();

        loadPreferences();

    }

    public void loadPreferences() {
        new AsyncTask<SharedPreferences, Void, Preferences>() {
            @Override
            protected Preferences doInBackground(SharedPreferences... sharedPreferences) {
                return new PreferencesUtilites(sharedPreferences[0]).getPreferences();
            }

            @Override
            protected void onPostExecute(Preferences preferences) {
                getView().updatePreferences(preferences);
            }
        }.execute(PreferencesUtilites.getDefaultPreferences(((Fragment)getView()).getActivity()));
    }

    public void considerAutoClick() {
        Preferences preferences = preferencesServices.load();

        if (preferences.isAutoExamine()){
            examineIntent();
        }
    }

    public void examineIntent() {
        new AsyncTask<IntentWrapperServices, Void, String>() {
            @Override
            protected String doInBackground(IntentWrapperServices... intentWrapperServices) {
                Log.d("dibg", " - 1");
                IntentWrapperServices intentWrapperServices1 = intentWrapperServices[0];
                Log.d("dibg", " - 2");
                IntentWrapper intentWrapper = intentWrapperServices1.buildIntentWrapper();
                Log.d("dibg", " - 3");
                ExamineServices examineServices = new ExamineServices();
                Log.d("dibg", " - 4");
                String rd = examineServices.examineIntent(intentWrapper);
                Log.d("dibg", " - 5");
                return rd;
                //return new ExamineServices().examineIntent(.buildIntentWrapper());
            }

            @Override
            protected void onPostExecute(String stringifiedIntent) {
                getView().populateMainView(stringifiedIntent);
            }
        }.execute(intentWrapperServices);
        //String stringifiedIntent = new ExamineServices().examineIntent(intentWrapperServices.buildIntentWrapper());
    }

    public void exportDb(File dest) {
        new AsyncTask<File, Void, File>() {
            @Override
            protected File doInBackground(File... files) {
                return intentWrapperServices.export(files[0], null, false);
            }

            @Override
            protected void onPostExecute(File logFileWritten) {
                getView().announceExportComplete(logFileWritten);
            }
        }.execute(dest);
        //File logFileWritten = intentWrapperServices.export(dest, null, false);

    }
}
