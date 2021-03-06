package com.example.ateg.intentexperiments;

/**
 * Created by ATeg on 10/20/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesUtilities {

    public static final String PREFERENCES_KEY = "com.example.ateg.intentexperiments.SHARED_PREFERENCES";
    public static final String AUTO_EXAMINE_KEY = "com.example.ateg.intentexperiments.AUTO_EXAMINE_KEY";
    public static final String AUTO_SAVE_KEY = "com.example.ateg.intentexperiments.AUTO_SAVE_KEY";
    public static final String AUTO_LOG_KEY = "com.example.ateg.intentexperiments.AUTO_LOG_KEY";
    public static final String FILTER_AUTO_LOG_KEY = "com.example.ateg.intentexperiments.FILTER_AUTO_LOG_KEY";
    public static final String CLICK_ANYWHERE_KEY = "com.example.ateg.intentexperiments.CLICK_ANYWHERE_KEY";
    public static final String SHOW_EXAMINE_BUTTON_KEY = "com.example.ateg.intentexperiments.SHOW_EXAMINE_BUTTON_KEY";
    public static final String DEFAULT_FILE_NAME_KEY = "com.example.ateg.intentexperiments.DEFAULT_FILE_NAME_KEY";

    private SharedPreferences sharedPreferences;

    public PreferencesUtilities(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public static SharedPreferences getDefaultPreferences(Context context){
        return context.getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
    }

    public void savePreferences(Preferences preferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(AUTO_EXAMINE_KEY, preferences.isAutoExamine());
        editor.putBoolean(AUTO_SAVE_KEY, preferences.isAutoSave());
        editor.putBoolean(AUTO_LOG_KEY, preferences.isAutoLog());
        editor.putBoolean(FILTER_AUTO_LOG_KEY, preferences.isFilterEmpties());
        editor.putBoolean(CLICK_ANYWHERE_KEY, preferences.isClickAnywhere());
        editor.putBoolean(SHOW_EXAMINE_BUTTON_KEY, preferences.isShowExamineButton());
        editor.putString(DEFAULT_FILE_NAME_KEY, preferences.getDefaultFileName());

        editor.commit();
    }

    public static String getDefaultFileName(Context context) {
        return context.getSharedPreferences(
                PreferencesUtilities.PREFERENCES_KEY,
                Context.MODE_PRIVATE)
                .getString(PreferencesUtilities.DEFAULT_FILE_NAME_KEY,
                        context.getString(R.string.default_file_name));
    }

    public void resetPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public Preferences getPreferences(){
        Preferences preferences = new Preferences();

        Boolean autoExamine = sharedPreferences
                .getBoolean(PreferencesUtilities.AUTO_EXAMINE_KEY, preferences.isAutoExamine());

        preferences.setAutoExamine(autoExamine);
        preferences.setAutoSave(sharedPreferences.getBoolean(PreferencesUtilities.AUTO_SAVE_KEY, preferences.isAutoSave()));
        preferences.setAutoLog(sharedPreferences.getBoolean(PreferencesUtilities.AUTO_LOG_KEY, preferences.isAutoLog()));
        preferences.setFilterEmpties(sharedPreferences.getBoolean(PreferencesUtilities.FILTER_AUTO_LOG_KEY, preferences.isFilterEmpties()));
        preferences.setClickAnywhere(sharedPreferences.getBoolean(PreferencesUtilities.CLICK_ANYWHERE_KEY, preferences.isClickAnywhere()));
        preferences.setShowExamineButton(sharedPreferences.getBoolean(PreferencesUtilities.SHOW_EXAMINE_BUTTON_KEY, preferences.isShowExamineButton()));
        preferences.setDefaultFileName(sharedPreferences.getString(PreferencesUtilities.DEFAULT_FILE_NAME_KEY, preferences.getDefaultFileName()));

        return preferences;
    }
}