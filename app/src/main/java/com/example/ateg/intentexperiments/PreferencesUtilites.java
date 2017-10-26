package com.example.ateg.intentexperiments;

/**
 * Created by ATeg on 10/20/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesUtilites  {

    public static final String PREFERENCES_KEY = "com.example.ateg.intentexperiments.SHARED_PREFERENCES";
    public static final String AUTO_EXAMINE_KEY = "com.example.ateg.intentexperiments.AUTO_EXAMINE_KEY";
    public static final String AUTO_SAVE_KEY = "com.example.ateg.intentexperiments.AUTO_SAVE_KEY";
    public static final String SHOW_EXAMINE_BUTTON_KEY = "com.example.ateg.intentexperiments.SHOW_EXAMINE_BUTTON_KEY";
    public static final String DEFAULT_FILE_NAME_KEY = "com.example.ateg.intentexperiments.DEFAULT_FILE_NAME_KEY";

    private final Dialog dialog;
    private Context context;

    public PreferencesUtilites(Context context, Dialog dialog){
        this.dialog = dialog;
        this.context = context;
    }

    public void savePreferences(View view) {
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        CheckBox autoExamineCheckbox
                = view.findViewById(R.id.settings_auto_examine_checkBox);
        editor.putBoolean(AUTO_EXAMINE_KEY, autoExamineCheckbox.isChecked());

        CheckBox checkBox = view.findViewById(R.id.settings_auto_save_checkBox);
        boolean autoSave = checkBox.isChecked();
        editor.putBoolean(AUTO_SAVE_KEY, autoSave);

        CheckBox showExamineButton
                = view.findViewById(R.id.settings_show_examine_button_checkBox);
        editor.putBoolean(SHOW_EXAMINE_BUTTON_KEY, showExamineButton.isChecked());

        EditText defaultFileName
                = view.findViewById(R.id.settings_default_file_name_editText);
        editor.putString(DEFAULT_FILE_NAME_KEY, defaultFileName.getText().toString());

        editor.commit();

        dialog.dismiss();
    }
}
