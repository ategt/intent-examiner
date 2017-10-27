package com.example.ateg.intentexperiments;

/**
 * Created by ATeg on 10/20/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesUtilites  {

    public static final String PREFERENCES_KEY = "com.example.ateg.intentexperiments.SHARED_PREFERENCES";
    public static final String AUTO_EXAMINE_KEY = "com.example.ateg.intentexperiments.AUTO_EXAMINE_KEY";
    public static final String AUTO_SAVE_KEY = "com.example.ateg.intentexperiments.AUTO_SAVE_KEY";
    public static final String SHOW_EXAMINE_BUTTON_KEY = "com.example.ateg.intentexperiments.SHOW_EXAMINE_BUTTON_KEY";
    public static final String DEFAULT_FILE_NAME_KEY = "com.example.ateg.intentexperiments.DEFAULT_FILE_NAME_KEY";

    //private final Dialog dialog;
    //private Context context;
    private SharedPreferences sharedPreferences;

    public PreferencesUtilites(SharedPreferences sharedPreferences){
        this.sharedPreferences = sharedPreferences;
    }

    public static SharedPreferences getDefaultPreferences(Context context){
        return context.getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
    }

    public void savePreferences(Preferences preferences) {
//        SharedPreferences sharedPreferences
//                = context.getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        //Dialog dialog = view.getRootView().findViewById();

        SharedPreferences.Editor editor = sharedPreferences.edit();

//        CheckBox autoExamineCheckbox
//                = dialog.findViewById(R.id.settings_auto_examine_checkBox);
        editor.putBoolean(AUTO_EXAMINE_KEY, preferences.isAutoExamine());

        editor.putBoolean(AUTO_SAVE_KEY, preferences.isAutoSave());

        editor.putBoolean(SHOW_EXAMINE_BUTTON_KEY, preferences.isShowExamineButton());

        editor.putString(DEFAULT_FILE_NAME_KEY, preferences.getDefaultFileName());

        editor.commit();
    }

    public static String getDefaultFileName(Context context) {
        return context.getSharedPreferences(
                PreferencesUtilites.PREFERENCES_KEY,
                Context.MODE_PRIVATE)
                .getString(PreferencesUtilites.DEFAULT_FILE_NAME_KEY,
                        context.getString(R.string.default_file_name));
    }

    public void resetPreferences() {
//        SharedPreferences sharedPreferences
//                = context.getSharedPreferences(PREFERENCES_KEY, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

//    public void loadPreferences() {
//
////        SharedPreferences sharedPreferences
////                = context
////                .getSharedPreferences(PreferencesUtilites.PREFERENCES_KEY, context.MODE_PRIVATE);
//
//        CheckBox autoExamineCheckbox
//                = dialog.findViewById(R.id.settings_auto_examine_checkBox);
//        Boolean autoExamine = sharedPreferences.getBoolean(PreferencesUtilites.AUTO_EXAMINE_KEY, autoExamineCheckbox.isChecked());
//        autoExamineCheckbox.setChecked(autoExamine);
//
//        CheckBox checkBox = dialog.findViewById(R.id.settings_auto_save_checkBox);
//        boolean autoSave = checkBox.isChecked();
//        autoSave = sharedPreferences.getBoolean(PreferencesUtilites.AUTO_SAVE_KEY, autoSave);
//        checkBox.setChecked(autoSave);
//
//        CheckBox showExamineButtonCheckBox
//                = dialog.findViewById(R.id.settings_show_examine_button_checkBox);
//        Boolean showExamineButton
//                = sharedPreferences.getBoolean(PreferencesUtilites.SHOW_EXAMINE_BUTTON_KEY, showExamineButtonCheckBox.isChecked());
//        showExamineButtonCheckBox.setChecked(showExamineButton);
//
//        EditText defaultFileNameEditText
//                = dialog.findViewById(R.id.settings_default_file_name_editText);
//        String defaultFileName = sharedPreferences.getString(PreferencesUtilites.DEFAULT_FILE_NAME_KEY, defaultFileNameEditText.getText().toString());
//        defaultFileNameEditText.setText(defaultFileName);
//    }

    public Preferences getPreferences(){
        Preferences preferences = new Preferences();

//        SharedPreferences sharedPreferences
//                = context
//                .getSharedPreferences(PreferencesUtilites.PREFERENCES_KEY, context.MODE_PRIVATE);

        Boolean autoExamine = sharedPreferences.getBoolean(PreferencesUtilites.AUTO_EXAMINE_KEY, preferences.isAutoExamine());
        preferences.setAutoExamine(autoExamine);

        preferences.setAutoSave(sharedPreferences.getBoolean(PreferencesUtilites.AUTO_SAVE_KEY, preferences.isAutoSave()));

        preferences.setShowExamineButton(sharedPreferences.getBoolean(PreferencesUtilites.SHOW_EXAMINE_BUTTON_KEY, preferences.isShowExamineButton()));

        preferences.setDefaultFileName(sharedPreferences.getString(PreferencesUtilites.DEFAULT_FILE_NAME_KEY, preferences.getDefaultFileName()));

        return preferences;
    }
}