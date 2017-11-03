package com.example.ateg.intentexperiments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.util.Objects;

import static junit.framework.Assert.assertTrue;

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

    public void exportDb(final Context context, final ExportSettings exportSettings) {

        new AsyncTask<Context, Void, Void>() {
            UnfamiliarIntentJsonException unfamiliarIntentJsonException = null;

            @Override
            protected Void doInBackground(Context... contexts) {
                File file = exportSettings.getFile();

                LoggingUtilities loggingUtilities = new LoggingUtilities(contexts[0], file);

                IntentRepository intentRepository = null;

                if (exportSettings.getFormat() == ExportSettings.Format.JSON) {
                    intentRepository = new IntentJsonRepository(loggingUtilities);
                } else if (exportSettings.getFormat() == ExportSettings.Format.TEXT) {
                    intentRepository = new IntentTextRepository(loggingUtilities);
                }

                try {
                    intentWrapperServices.export(intentRepository, null, exportSettings);
                } catch (RuntimeException ex) {
                    Throwable throwable = ex.getCause();

                    if (throwable instanceof java.lang.InstantiationException) {
                        String message = throwable.getMessage();
                        unfamiliarIntentJsonException
                                = new UnfamiliarIntentJsonException(message, throwable);
                    } else
                        throw ex;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (unfamiliarIntentJsonException != null) {
                    getView().showError(unfamiliarIntentJsonException);
                } else {
                    ExportSettings.Destination destination = exportSettings.getDestination();

                    if (Objects.equals(ExportSettings.Destination.LOCAL, destination)) {
                        getView().announceExportComplete(exportSettings.getFile());
                    } else if (Objects.equals(ExportSettings.Destination.SEND, destination)) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(exportSettings.getFile()));
                        context.startActivity(intent);
                    }
                }
            }
        }.execute(context);
    }
}
