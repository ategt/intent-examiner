package com.example.ateg.intentexperiments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.ateg.intentexperiments.FileSelector.Preferences;

/**
 * Created by ATeg on 10/20/2017.
 */

class MainPresenter extends BasePresenter<MainView>{
    public MainPresenter(MainView viewInstance) {
        super(viewInstance);
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
}
