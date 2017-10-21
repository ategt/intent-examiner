package com.example.ateg.intentexperiments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.ateg.intentexperiments.FileSelector.FileOperation;
import com.example.ateg.intentexperiments.FileSelector.FileSelector;
import com.example.ateg.intentexperiments.FileSelector.OnHandleFileListener;

import java.io.File;

public class MainFragment extends BaseFragment<MainPresenter> implements MainView {

    private static final String TEXT_WINDOW_VALUE = "com.example.ateg.intentexperiments.TEXT_WINDOW_VALUE";

    final String[] mFileFilter = {".txt", "*.*"};

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            CharSequence textWindowValue = savedInstanceState.getCharSequence(TEXT_WINDOW_VALUE);
            TextView textViewAs = (TextView) getCreatedView().findViewById(R.id.central_textView);
            textViewAs.setText(textWindowValue);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.settings_dialog);
                dialog.setTitle(R.string.settings_dialog_title);
                dialog.setCancelable(true);

                SharedPreferences sharedPreferences
                        = getActivity().getSharedPreferences(SettingsOnAcceptListener.PREFERENCES_KEY, getActivity().MODE_PRIVATE);

                CheckedTextView autoExamineCheckbox
                        = dialog.findViewById(R.id.settings_auto_examine_checkBox);
                Boolean autoExamine = sharedPreferences.getBoolean(SettingsOnAcceptListener.AUTO_EXAMINE_KEY, autoExamineCheckbox.isChecked());
                autoExamineCheckbox.setChecked(autoExamine);

                CheckBox checkBox = dialog.findViewById(R.id.settings_auto_save_checkBox);
                boolean autoSave = checkBox.isChecked();
                autoSave = sharedPreferences.getBoolean(SettingsOnAcceptListener.AUTO_SAVE_KEY, autoSave);
                checkBox.setChecked(autoSave);

                CheckBox showExamineButtonCheckBox
                        = dialog.findViewById(R.id.settings_show_examine_button_checkBox);
                Boolean showExamineButton
                        = sharedPreferences.getBoolean(SettingsOnAcceptListener.SHOW_EXAMINE_BUTTON_KEY, showExamineButtonCheckBox.isChecked());
                showExamineButtonCheckBox.setChecked(showExamineButton);

                EditText defaultFileNameEditText
                        = dialog.findViewById(R.id.settings_default_file_name_editText);
                String defaultFileName = sharedPreferences.getString(SettingsOnAcceptListener.DEFAULT_FILE_NAME_KEY, defaultFileNameEditText.getText().toString());
                defaultFileNameEditText.setText(defaultFileName);

                Button acceptButton = (Button) dialog.findViewById(R.id.settings_accept_button);
                acceptButton.setOnClickListener(new SettingsOnAcceptListener(getActivity(), dialog));

                Button resetButton = dialog.findViewById(R.id.settings_reset_button);
                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences
                                = getActivity().getSharedPreferences("IntentPreferences", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                    }
                });

                Button cancelButton = dialog.findViewById(R.id.settings_cancel_button);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return true;
            case R.id.save_to_file:
                TextView textView = (TextView) getCreatedView().findViewById(R.id.central_textView);
                File logFile = saveTextViewContentToFile(textView,
                        LoggingUtilities.establishLogFile(getActivity(), Environment.DIRECTORY_DOCUMENTS));

                fileSaveCompleteSnackBar(textView, logFile);

                return true;
            case R.id.save_to_file_as:
                File[] files = ContextCompat.getExternalFilesDirs(getActivity(), Environment.DIRECTORY_DOCUMENTS);

                File storageDir = files[files.length - 1];

                new FileSelector(getActivity(),
                        FileOperation.SAVE,
                        new OnHandleFileListener() {
                            @Override
                            public void handleFile(String filePath) {
                                TextView textViewAs = (TextView) getCreatedView().findViewById(R.id.central_textView);
                                File logFileAs = saveTextViewContentToFile(textViewAs, new File(filePath));

                                fileSaveCompleteSnackBar(textViewAs, logFileAs);
                            }
                        },
                        mFileFilter,
                        new File(storageDir, LoggingUtilities.getDefaultFileName(getActivity())))
                        .show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView textViewAs = (TextView) getCreatedView().findViewById(R.id.central_textView);
        outState.putCharSequence(TEXT_WINDOW_VALUE, textViewAs.getText());
    }

    private void fileSaveCompleteSnackBar(TextView textViewAs, final File logFileAs) {
        Snackbar.make(textViewAs, "File Saved", Snackbar.LENGTH_INDEFINITE)
                .setAction("Open", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File tempFile = copyToTempFile(logFileAs);
                        Intent intent = intentToOpenFile(tempFile);
                        startActivity(intent);
                    }
                }).show();
    }

    private File saveTextViewContentToFile(TextView textView, File destinationFile) {
        String examinationReport = textView.getText().toString();
        LoggingUtilities loggingUtilities = new LoggingUtilities(getActivity(), destinationFile);
        loggingUtilities.updateTextFile(examinationReport);
        return loggingUtilities.getLogFile();
    }

    @NonNull
    private Intent intentToOpenFile(File tempFile) {
        Uri fileUri = FileProvider.getUriForFile(
                getActivity(),
                "com.example.ateg.intentexperiments.FileProvider",
                tempFile);

        Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(fileUri,
                getActivity().getContentResolver().getType(fileUri));
        return intent;
    }

    @NonNull
    private File copyToTempFile(File logFile) {
        File tempFile = new File(getActivity().getCacheDir(), "temp.txt");

        new LoggingUtilities(getActivity(), tempFile)
                .updateTextFile(LoggingUtilities.readFile(getActivity(), logFile));

        tempFile.deleteOnExit();
        return tempFile;
    }

    @Override
    public void showError(Throwable ex) {

    }

    @Override
    public void showLoading(Integer id) {

    }

    @Override
    protected int layout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setUi(View v) {

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        getActivity().setActionBar(toolbar);


    }

    @Override
    protected void init() {
    }

    @Override
    protected void populate() {
        Boolean autoExamine = getActivity().getSharedPreferences(SettingsOnAcceptListener.PREFERENCES_KEY, Context.MODE_PRIVATE)
                .getBoolean(SettingsOnAcceptListener.AUTO_EXAMINE_KEY, false);
        if (autoExamine) {
            new ExamineServices(getActivity()).examineIntent(getView(), getActivity().getIntent());
        }
    }

    @Override
    protected void setListeners() {
        FloatingActionButton fab = (FloatingActionButton) getCreatedView().findViewById(R.id.action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ExamineServices(getActivity()).examineIntent(view, getActivity().getIntent());
            }
        });
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }
}
