package com.example.ateg.intentexperiments;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ATeg on 11/1/2017.
 */

public class IntentJsonRepository implements IntentRepository {

    private static final String TAG = "JSON Repo";
    Gson gson;
    LoggingUtilities loggingUtilities;

    public IntentJsonRepository(LoggingUtilities loggingUtilities) {
        this.loggingUtilities = loggingUtilities;
        this.gson = CustomGsonBuilder.get().create();
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
        IntentWrapper[] intentWrapperArray = gson.fromJson(json, IntentWrapper[].class);

        List<IntentWrapper> intentWrapperList = Arrays.asList(intentWrapperArray);

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
