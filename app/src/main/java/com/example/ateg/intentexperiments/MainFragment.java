package com.example.ateg.intentexperiments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.ateg.intentexperiments.FileSelector.FileOperation;
import com.example.ateg.intentexperiments.FileSelector.FileSelector;
import com.example.ateg.intentexperiments.FileSelector.OnHandleFileListener;

import java.io.File;

public class MainFragment extends BaseFragment<MainPresenter> implements MainView {

    private static final String TEXT_WINDOW_VALUE = "com.example.ateg.intentexperiments.TEXT_WINDOW_VALUE";

    final String[] mFileFilter = {".txt", "*.*"};
    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            CharSequence textWindowValue = savedInstanceState.getCharSequence(TEXT_WINDOW_VALUE);
            TextView textViewAs = (TextView) getCreatedView().findViewById(R.id.central_textView);
            textViewAs.setText(textWindowValue);
        }

        Toolbar toolbar = getCreatedView().findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:

                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.settings_dialog);
                dialog.setTitle(R.string.settings_dialog_title);

                mPresenter.loadPreferences();

                Button acceptButton = (Button) dialog.findViewById(R.id.settings_accept_button);
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new PreferencesUtilites(PreferencesUtilites.getDefaultPreferences(getActivity()))
                                .savePreferences(buildPreferences(dialog));
                        dialog.dismiss();
                    }
                });

                Button resetButton = dialog.findViewById(R.id.settings_reset_button);
                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.resetPreferences();
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
            case R.id.action_export:

                File[] filesa = ContextCompat.getExternalFilesDirs(getActivity(), Environment.DIRECTORY_DOCUMENTS);

                File storageDira = filesa[filesa.length - 1];

                new FileSelector(getActivity(),
                        FileOperation.SAVE,
                        new OnHandleFileListener() {
                            @Override
                            public void handleFile(String filePath) {
                                //TextView textViewAs = (TextView) getCreatedView().findViewById(R.id.central_textView);
                                //File logFileAs = saveTextViewContentToFile(textViewAs, new File(filePath));

                                //fileSaveCompleteSnackBar(textViewAs, logFileAs);

                                mPresenter.exportDb(new File(filePath));
                            }
                        },
                        mFileFilter,
                        new File(storageDira, LoggingUtilities.getDefaultFileName(getActivity())))
                        .show();


//                String examinationReport = textView.getText().toString();
//                LoggingUtilities loggingUtilities = new LoggingUtilities(getActivity(), destinationFile);
//                loggingUtilities.updateTextFile(examinationReport);
//                return loggingUtilities.getLogFile();

            //mPresenter.exportDb();
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

    private void fileSaveCompleteSnackBar(View textViewAs, final File logFileAs) {
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
        return R.layout.main_layout;
    }

    @Override
    protected void setUi(View v) {
        Preferences preferences = new PreferencesServices(getActivity()).load();

        FloatingActionButton floatingActionButton = v.findViewById(R.id.action_button);
        if (preferences.isShowExamineButton())
            floatingActionButton.setVisibility(View.VISIBLE);
        else
            floatingActionButton.setVisibility(View.INVISIBLE);

        TextView centralTextView = getView().findViewById(R.id.central_textView);

        if (preferences.isClickAnywhere()){
            centralTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.examineIntent();
                }
            });
        }

    }

    @Override
    protected void init() {
    }

    @Override
    protected void populate() {
        mPresenter.considerAutoClick();
    }

    @Override
    protected void setListeners() {
        FloatingActionButton fab = (FloatingActionButton) getCreatedView().findViewById(R.id.action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.examineIntent();
            }
        });
    }

    @Override
    protected MainPresenter createPresenter() {
        IntentRepository intentRepository = new IntentRepository(getActivity(), getString(R.string.db_name), 1);
        PreferencesServices preferencesServices = new PreferencesServices(getActivity());
        IntentWrapperServices intentWrapperServices = new IntentWrapperServices(getActivity(), intentRepository, preferencesServices);
        return new MainPresenter(this, intentWrapperServices, preferencesServices);
    }

    @Override
    public void updatePreferences(Preferences preferences) {

        CheckBox autoExamineCheckbox
                = dialog.findViewById(R.id.settings_auto_examine_checkBox);
        autoExamineCheckbox.setChecked(preferences.isAutoExamine());

        CheckBox checkBox = dialog.findViewById(R.id.settings_auto_save_checkBox);
        checkBox.setChecked(preferences.isAutoSave());

        CheckBox showExamineButtonCheckBox
                = dialog.findViewById(R.id.settings_show_examine_button_checkBox);
        showExamineButtonCheckBox.setChecked(preferences.isShowExamineButton());

        CheckBox autoLogCheckBox
                = dialog.findViewById(R.id.settings_auto_log_checkBox);
        autoLogCheckBox.setChecked(preferences.isAutoLog());

        CheckBox clickAnywhereCheckBox
                = dialog.findViewById(R.id.settings_click_anywhere_examines_checkBox);
        clickAnywhereCheckBox.setChecked(preferences.isClickAnywhere());

        EditText defaultFileNameEditText
                = dialog.findViewById(R.id.settings_default_file_name_editText);
        defaultFileNameEditText.setText(preferences.getDefaultFileName());
    }

    @Override
    public void populateMainView(String stringifiedIntent) {
        TextView textView = (TextView) getView().getRootView().findViewById(R.id.central_textView);

        if (stringifiedIntent != null) {
            textView.setText(stringifiedIntent);
            textView.setGravity(Gravity.CENTER_VERTICAL);
        } else {
            textView.setText(R.string.intent_empty);
            textView.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void announceExportComplete(File logFileWritten) {
        fileSaveCompleteSnackBar(getView(), logFileWritten);
    }

    private Preferences buildPreferences(Dialog dialog) {
        Preferences preferences = new Preferences();

        CheckBox autoExamineCheckbox
                = dialog.findViewById(R.id.settings_auto_examine_checkBox);
        preferences.setAutoExamine(autoExamineCheckbox.isChecked());

        CheckBox checkBox = dialog.findViewById(R.id.settings_auto_save_checkBox);
        boolean autoSave = checkBox.isChecked();
        preferences.setAutoSave(autoSave);

        CheckBox showExamineButton
                = dialog.findViewById(R.id.settings_show_examine_button_checkBox);
        preferences.setShowExamineButton(showExamineButton.isChecked());

        CheckBox autoLogBox
                = dialog.findViewById(R.id.settings_auto_log_checkBox);
        preferences.setAutoLog(autoLogBox.isChecked());

        CheckBox clickAnywhereCheckBox
                = dialog.findViewById(R.id.settings_click_anywhere_examines_checkBox);
        preferences.setClickAnywhere(clickAnywhereCheckBox.isChecked());

        EditText defaultFileName
                = dialog.findViewById(R.id.settings_default_file_name_editText);
        preferences.setDefaultFileName(defaultFileName.getText().toString());

        return preferences;
    }
}