package com.example.ateg.intentexperiments;

import android.content.Context;

/**
 * Created by ATeg on 10/31/2017.
 */

public class PreferencesServices {

    private PreferencesUtilities preferencesUtilites;

    public PreferencesServices(Context context) {
        preferencesUtilites =
                new PreferencesUtilities(
                        context.getSharedPreferences(
                                PreferencesUtilities.PREFERENCES_KEY,
                                Context.MODE_PRIVATE));

    }

    public void save(Preferences preferences) {
        preferencesUtilites.savePreferences(preferences);
    }

    public Preferences load(){
        return preferencesUtilites.getPreferences();
    }

    public void reset(){
        preferencesUtilites.resetPreferences();
    }
}
