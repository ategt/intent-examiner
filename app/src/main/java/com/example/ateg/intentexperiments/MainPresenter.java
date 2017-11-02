package com.example.ateg.intentexperiments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by ATeg on 10/20/2017.
 */

class MainPresenter extends BasePresenter<MainView> {

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
                for (IntentWrapperServices intentWrapperServices1 : intentWrapperServices) {
                    intentWrapperServices1.init();
                }
                return null;
            }
        }.execute(intentWrapperServices);
    }

    public void resetPreferences() {
        PreferencesUtilites preferencesUtilites = new PreferencesUtilites(
                PreferencesUtilites.getDefaultPreferences(((Fragment) getView()).getActivity()));
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
        }.execute(PreferencesUtilites.getDefaultPreferences(((Fragment) getView()).getActivity()));
    }

    public void considerAutoClick() {
        Preferences preferences = preferencesServices.load();

        if (preferences.isAutoExamine()) {
            examineIntent();
            getView().examineDone();
        }
    }

    public void examineIntent() {
        new AsyncTask<IntentWrapperServices, Void, String>() {
            @Override
            protected String doInBackground(IntentWrapperServices... intentWrapperServices) {
                return new ExamineServices().examineIntent(intentWrapperServices[0].buildIntentWrapper());
            }

            @Override
            protected void onPostExecute(String stringifiedIntent) {
                getView().populateMainView(stringifiedIntent);
                getView().examineDone();
            }
        }.execute(intentWrapperServices);
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
    }

    public void exportDb(ExportSettings exportSettings) {

        File file = exportSettings.getFile();

        LoggingUtilities loggingUtilities = new LoggingUtilities(file);

        IntentRepository intentRepository = null;

        if (exportSettings.getFormat() == ExportSettings.Format.JSON) {
            intentRepository = new IntentJsonRespository(file,);
        } else if (exportSettings.getScope() == ExportSettings.Scope.DIFF) {
            intentWrapperList = sourceIntentRepository.getDifferential();
        } else if (exportSettings.getScope() == ExportSettings.Scope.SINGLE) {
            List<IntentWrapper> list = new ArrayList<>();
            list.add(buildIntentWrapper());
            intentWrapperList = list;
        }

        intentWrapperServices.export(file, null, exportSettings);

        ExportSettings.Destination destination = exportSettings.getDestination();
asdf
        if(Objects.equals(ExportSettings.Destination.LOCAL, destination)){

        }
    }
}
