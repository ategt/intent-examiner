package com.example.ateg.intentexperiments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
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

    public void exportDb(Context context, ExportSettings exportSettings) {

        File file = exportSettings.getFile();

        LoggingUtilities loggingUtilities = new LoggingUtilities(context, file);

        IntentRepository intentRepository = null;

        if (exportSettings.getFormat() == ExportSettings.Format.JSON) {
            intentRepository = new IntentJsonRespository(file, loggingUtilities);
        } else if (exportSettings.getFormat() == ExportSettings.Format.XML) {
            intentRepository = new IntentXMLRepository(file, loggingUtilities);
        } else if (exportSettings.getFormat() == ExportSettings.Format.TEXT) {
            intentRepository = new IntentTextRespository(file, loggingUtilities);
        }

        intentWrapperServices.export(intentRepository, null, exportSettings);

        ExportSettings.Destination destination = exportSettings.getDestination();

        if (Objects.equals(ExportSettings.Destination.LOCAL, destination)) {
            // Seems like something should happen here, but I can not thing of anything.
        } else if (Objects.equals(ExportSettings.Destination.SEND, destination)) {
            Intent intent = new Intent(Intent.ACTION_SEND);

            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            //intent.setType("APPLICATION/XML")
            context.startActivity(intent);
        }
    }
}
