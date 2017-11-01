package com.example.ateg.intentexperiments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
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
        //init();
    }

    public void init() {
        Preferences preferences = preferencesServices.load();

        if (preferences.isAutoLog())
            log();

        if (preferences.isAutoSave() && !Objects.equals(defaultIntentRepository, intentRepository)) {
            save();
        }
    }

    public void setIntentRepository(IntentRepository intentRepository) {
        this.intentRepository = intentRepository;
    }

    public IntentWrapper buildIntentWrapper() {
        Intent intent = context.getIntent();
        Log.d("asdf", "builder");
        return new IntentWrapper(intent);
    }

    public IntentWrapper save() {
        return intentRepository.create(buildIntentWrapper());
    }

    public IntentWrapper log() {
        return defaultIntentRepository.create(buildIntentWrapper());
    }

    public IntentWrapper save(IntentRepository intentRepository) {
        return intentRepository.create(buildIntentWrapper());
    }

    public void export(IntentRepository intentRepository, ExportProgress exportProgress, boolean markArchived) {
        List<IntentWrapper> intentWrapperList = this.intentRepository.getDifferential();
        int intentWrapperListSize = intentWrapperList.size();
        for (IntentWrapper intentWrapper : intentWrapperList) {
            if (exportProgress != null)
                exportProgress.updateExportProgress(intentWrapperList.indexOf(intentWrapper), intentWrapperListSize);
            intentRepository.create(intentWrapper);
        }
        if (exportProgress != null)
            exportProgress.exportDone();

        if (markArchived)
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
