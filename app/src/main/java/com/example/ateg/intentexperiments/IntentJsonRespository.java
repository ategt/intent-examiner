package com.example.ateg.intentexperiments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATeg on 11/1/2017.
 */

public class IntentJsonRespository implements IntentRepository {

    File file;
    Gson gson;
    LoggingUtilities loggingUtilities;

    public IntentJsonRespository(File file, LoggingUtilities loggingUtilities) {
        this.file = file;
        this.loggingUtilities = loggingUtilities;
        this.gson = new GsonBuilder().create();
    }

    @Override
    public List<IntentWrapper> create(List<IntentWrapper> intentWrapperList) {
        if (intentWrapperList == null) return null;

        String json = gson.toJson(intentWrapperList);

        boolean success = loggingUtilities.updateTextFile(json);

        if (success) {
            return intentWrapperList;
        } else {
            throw new ExportException("JSON Serialization to File Failed.");
        }
    }

    @Override
    public IntentWrapper create(IntentWrapper intentWrapper) {
        if (intentWrapper == null) return null;

        String json = gson.toJson(intentWrapper);

        boolean success = loggingUtilities.updateTextFile(json);

        if (success) {
            return intentWrapper;
        } else {
            throw new ExportException("JSON Serialization to File Failed.");
        }
    }

    @Override
    public List<IntentWrapper> getAll() {
        String json = loggingUtilities.readFile(loggingUtilities.getLogFile());
        List<IntentWrapper> intentWrapperList = gson.fromJson(json, List.class);

        return intentWrapperList;
    }

    @Override
    public List<IntentWrapper> getDifferential() {
        return getAll();
    }

    @Override
    public void markAllArchived() {

    }

    @Override
    public void deleteAll() {

    }
}
