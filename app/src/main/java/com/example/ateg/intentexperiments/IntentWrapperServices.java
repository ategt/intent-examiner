package com.example.ateg.intentexperiments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by ATeg on 10/31/2017.
 */

public class IntentWrapperServices {

    private Activity context;
    private IntentRepository intentRepository;
    private PreferencesServices preferencesServices;
    private IntentRepository defaultIntentRepository;

    public IntentWrapperServices(Activity context, IntentRepository intentRepository, PreferencesServices preferencesServices) {
        this.intentRepository = intentRepository;
        this.defaultIntentRepository = intentRepository;
        this.preferencesServices = preferencesServices;
        this.context = context;
    }

    public void init(ProgressUpdateListener progressUpdater) {
        Preferences preferences = preferencesServices.load();
        if (preferences.isAutoLog()) {
            IntentWrapper intentWrapper = conditionalLog(preferences);
            if (intentWrapper != null)
                updateProgress(progressUpdater, R.string.auto_log_successful);
        }

        if (preferences.isAutoSave() && !Objects.equals(defaultIntentRepository, intentRepository)) {
            IntentWrapper intentWrapper = save();
            if (intentWrapper != null)
                updateProgress(progressUpdater, R.string.auto_save_successful);
        }
    }

    private void updateProgress(ProgressUpdateListener progressUpdater, int auto_log_successful) {
        if (progressUpdater != null) {
            progressUpdater.onProgress(auto_log_successful);
        }
    }

    private IntentWrapper conditionalLog(Preferences preferences) {
        if (preferences.isFilterEmpties()) {
            return smartLog();
        } else {
            return log();
        }
    }

    public void setIntentRepository(IntentRepository intentRepository) {
        this.intentRepository = intentRepository;
    }

    public IntentWrapper buildIntentWrapper() {
        Intent intent = context.getIntent();
        return new IntentWrapper(intent);
    }

    public IntentWrapper save() {
        return intentRepository.create(buildIntentWrapper());
    }

    public IntentWrapper log() {
        return log(buildIntentWrapper());
    }

    public IntentWrapper smartLog() {
        IntentWrapper intentWrapper = buildIntentWrapper();

        Bundle bundle = intentWrapper.getExtras();
        if (bundle != null) {
            if (bundle.size() > 0) {
                return log();
            }
        }

        return null;
    }

    private IntentWrapper log(IntentWrapper intentWrapper) {
        return defaultIntentRepository.create(intentWrapper);
    }

    public IntentWrapper save(IntentRepository intentRepository) {
        return intentRepository.create(buildIntentWrapper());
    }

    public void export(IntentRepository destinationIntentRepository, ExportProgress exportProgress, ExportSettings exportSettings) {
        IntentRepository sourceIntentRepository = this.intentRepository;
        export(sourceIntentRepository, destinationIntentRepository, exportProgress, exportSettings);
    }

    public void export(IntentRepository sourceIntentRepository,
                       IntentRepository destinationIntentRepository,
                       ExportProgress exportProgress,
                       ExportSettings exportSettings) {

        List<IntentWrapper> intentWrapperList = null;
        if (exportSettings.getScope() == ExportSettings.Scope.FULL) {
            intentWrapperList = sourceIntentRepository.getAll();
        } else if (exportSettings.getScope() == ExportSettings.Scope.DIFF) {
            intentWrapperList = sourceIntentRepository.getDifferential();
        } else if (exportSettings.getScope() == ExportSettings.Scope.SINGLE) {
            List<IntentWrapper> list = new ArrayList<>();
            list.add(buildIntentWrapper());
            intentWrapperList = list;
        }

        destinationIntentRepository.create(intentWrapperList);

        //int intentWrapperListSize = intentWrapperList.size();
//        for (IntentWrapper intentWrapper : intentWrapperList) {
//            if (exportProgress != null)
//                exportProgress.updateExportProgress(intentWrapperList.indexOf(intentWrapper), intentWrapperListSize);
//            destinationIntentRepository.create(intentWrapper);
//        }

        if (exportProgress != null)
            exportProgress.exportDone();

        if (exportSettings != null && exportSettings.isMarkArchived())
            archive();
    }

    public File export(File destinationFile, ExportProgress exportProgress, boolean markArchived) {
        ExamineServices examineServices = new ExamineServices();
        StringBuilder stringBuilder = new StringBuilder();
        List<IntentWrapper> intentWrapperList = this.intentRepository.getDifferential();
        int intentWrapperListSize = intentWrapperList.size();
        for (IntentWrapper intentWrapper : intentWrapperList) {
            if (exportProgress != null)
                exportProgress.updateExportProgress(intentWrapperList.indexOf(intentWrapper), intentWrapperListSize);
            stringBuilder.append(examineServices.examineIntent(intentWrapper));
        }

        LoggingUtilities loggingUtilities = new LoggingUtilities(context, destinationFile);
        loggingUtilities.updateTextFile(stringBuilder.toString());
        //return loggingUtilities.getLogFile();
        //}

        if (exportProgress != null)
            exportProgress.exportDone();

        if (markArchived)
            archive();

        return loggingUtilities.getLogFile();
    }

    public void archive() {
        intentRepository.markAllArchived();
    }
}
