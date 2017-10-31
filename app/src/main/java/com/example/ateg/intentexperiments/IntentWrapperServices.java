package com.example.ateg.intentexperiments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * Created by ATeg on 10/31/2017.
 */

public class IntentWrapperServices {

    private Activity context;
    private IntentRepository intentRepository;

    public IntentWrapperServices(Activity context, IntentRepository intentRepository, PreferencesUtilites) {
        this.intentRepository = intentRepository;
        this.context = context;
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

    public IntentWrapper save(IntentRepository intentRepository) {
        return intentRepository.create(buildIntentWrapper());
    }

    public void export(IntentRepository intentRepository, ExportProgress exportProgress) {
        List<IntentWrapper> intentWrapperList = this.intentRepository.getDifferential();
        int intentWrapperListSize = intentWrapperList.size();
        for (IntentWrapper intentWrapper : intentWrapperList) {
            if (exportProgress != null)
                exportProgress.updateExportProgress(intentWrapperList.indexOf(intentWrapper), intentWrapperListSize);
            intentRepository.create(intentWrapper);
        }
        if (exportProgress != null)
            exportProgress.exportDone();
    }

    public void archived(){
        intentRepository.markAllArchived();
    }
}
