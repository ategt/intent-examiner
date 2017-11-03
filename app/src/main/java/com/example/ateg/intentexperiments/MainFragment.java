package com.example.ateg.intentexperiments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.ateg.intentexperiments.FileSelector.FileOperation;
import com.example.ateg.intentexperiments.FileSelector.FileSelector;
import com.example.ateg.intentexperiments.FileSelector.OnHandleFileListener;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MainFragment extends BaseFragment<MainPresenter> implements MainView {

    private static final String TEXT_WINDOW_VALUE = "com.example.ateg.intentexperiments.TEXT_WINDOW_VALUE";
    private static final String TAG = "Main Fragment";

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

                final Dialog exportDialog = new Dialog(getActivity());

                exportDialog.setCancelable(true);
                exportDialog.setContentView(R.layout.export_dialog);
                exportDialog.setTitle(R.string.export_dialog_title);

                Button exportAcceptButton = (Button) exportDialog.findViewById(R.id.export_accept_button);
                exportAcceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ExportSettings tempExportSettings = buildExportSettings(exportDialog);

                        if (tempExportSettings.validate()) {

                            final ExportSettings exportSettings = tempExportSettings;

                            exportDialog.dismiss();

                            File[] files = ContextCompat.getExternalFilesDirs(getActivity(), Environment.DIRECTORY_DOCUMENTS);

                            File storageDir = files[files.length - 1];

                            if (ExportSettings.Destination.LOCAL == exportSettings.getDestination()) {

                                new FileSelector(getActivity(),
                                        FileOperation.SAVE,
                                        new OnHandleFileListener() {
                                            @Override
                                            public void handleFile(String filePath) {
                                                File file = new File(filePath);
                                                exportSettings.setFile(file);
                                                mPresenter.exportDb(getActivity(), exportSettings);
                                            }
                                        },
                                        mFileFilter,
                                        new File(storageDir, LoggingUtilities.getDefaultFileName(getActivity())))
                                        .show();
                            } else {
                                File file = new File(storageDir, "temp_stream_" + UUID.randomUUID() + ".tmp");
                                exportSettings.setFile(file);
                                file.deleteOnExit();
                                mPresenter.exportDb(getActivity(), exportSettings);
                            }
                        } else {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("One Or More Required Fields Are Empty.")
                                    .setTitle("Error")
                                    //.setIcon(ic_alert)
                                    .setCancelable(true)
                                    .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
                });

                Button exportCancelButton = exportDialog.findViewById(R.id.export_cancel_button);
                exportCancelButton.setOnClickListener(new View.OnClickListener()

                {
                    @Override
                    public void onClick(View view) {
                        exportDialog.dismiss();
                    }
                });

                exportDialog.show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private ExportSettings buildExportSettings(Dialog exportDialog) {
        final ExportSettings exportSettings = new ExportSettings();

        RadioButton textRadioButton = exportDialog.findViewById(R.id.export_txt_radioButton);
        RadioButton jsonRadioButton = exportDialog.findViewById(R.id.export_json_radioButton);

        CheckBox markArchiveCheckBox = exportDialog.findViewById(R.id.export_mark_archive_checkBox);

        RadioButton fullRadioButton = exportDialog.findViewById(R.id.export_full_radioButton);
        RadioButton diffRadioButton = exportDialog.findViewById(R.id.export_diff_radioButton);
        RadioButton singleRadioButton = exportDialog.findViewById(R.id.export_single_radioButton);

        RadioButton localRadioButton = exportDialog.findViewById(R.id.export_local_radioButton);
        RadioButton sendRadioButton = exportDialog.findViewById(R.id.export_send_radioButton);

        if (textRadioButton.isChecked()) {
            exportSettings.setFormat(ExportSettings.Format.TEXT);
        } else if (jsonRadioButton.isChecked()) {
            exportSettings.setFormat(ExportSettings.Format.JSON);
        }

        exportSettings.setMarkArchived(markArchiveCheckBox.isChecked());

        if (fullRadioButton.isChecked()) {
            exportSettings.setScope(ExportSettings.Scope.FULL);
        } else if (diffRadioButton.isChecked()) {
            exportSettings.setScope(ExportSettings.Scope.DIFF);
        } else if (singleRadioButton.isChecked()) {
            exportSettings.setScope(ExportSettings.Scope.SINGLE);
        }

        if (localRadioButton.isChecked()) {
            exportSettings.setDestination(ExportSettings.Destination.LOCAL);
        } else if (sendRadioButton.isChecked()) {
            exportSettings.setDestination(ExportSettings.Destination.SEND);
        }
        return exportSettings;
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
                        File tempFile = null;
                        try {
                            tempFile = copyToTempFile(logFileAs);
                            Intent intent = intentToOpenFile(tempFile);
                            startActivity(intent);
                        } catch (IOException e) {
                            Log.e(TAG, getString(R.string.file_temp_error), e);
                            Toast.makeText(getActivity(), R.string.file_temp_error, Toast.LENGTH_LONG)
                                    .show();
                        }
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
    private File copyToTempFile(File logFile) throws IOException {
        File tempFile = File.createTempFile("temp", ".txt");

        new LoggingUtilities(getActivity(), tempFile)
                .updateTextFile(LoggingUtilities.readFile(getActivity(), logFile));

        tempFile.deleteOnExit();
        return tempFile;
    }

    @Override
    public void showError(Throwable ex) {
        Log.e(TAG, "Something bad happened.", ex);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.unknown_field_error)
                .setCancelable(true)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        if (ex instanceof UnfamiliarIntentJsonException) {
            String message = ex.getMessage();
            alertBuilder = alertBuilder.setMessage(String.format(getString(R.string.unknown_field_error_explanation), message));
        } else {
            alertBuilder = alertBuilder.setMessage(R.string.generic_error);
        }
        alertBuilder.show();
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

        TextView centralTextView = getCreatedView().findViewById(R.id.central_textView);

        if (preferences.isClickAnywhere()) {
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
        IntentRepository intentRepository = new IntentSQLiteRepository(getActivity(), getString(R.string.db_name), 1);
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

        CheckBox filterAutoLogCheckBox
                = dialog.findViewById(R.id.settings_filter_empty_auto_log_checkBox);
        filterAutoLogCheckBox.setChecked(preferences.isFilterEmpties());

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

    @Override
    public void examineDone() {
        Snackbar.make(getCreatedView().findViewById(R.id.examination_button_container)
                , R.string.examine_completed, Snackbar.LENGTH_LONG)
                .show();
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

        CheckBox filterAutoLogBox
                = dialog.findViewById(R.id.settings_filter_empty_auto_log_checkBox);
        preferences.setFilterEmpties(filterAutoLogBox.isChecked());

        CheckBox clickAnywhereCheckBox
                = dialog.findViewById(R.id.settings_click_anywhere_examines_checkBox);
        preferences.setClickAnywhere(clickAnywhereCheckBox.isChecked());

        EditText defaultFileName
                = dialog.findViewById(R.id.settings_default_file_name_editText);
        preferences.setDefaultFileName(defaultFileName.getText().toString());

        return preferences;
    }
}