package com.example.ateg.intentexperiments;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

/**
 * Created by ATeg on 10/28/2017.
 */

public class IntentWrapper {
    private Gson gson = new GsonBuilder().create();

    //public IntentWrapper() {
    //}

    public IntentWrapper(Intent sourceIntent) {
        buildIntent(sourceIntent);
    }

    private void buildIntent(Intent sourceIntent) {
        Intent intent = new Intent();

        intent.setAction(sourceIntent.getAction());
        intent.setComponent(sourceIntent.getComponent());
        intent.setFlags(sourceIntent.getFlags());
        intent.setType(sourceIntent.getType());
        intent.setData(sourceIntent.getData());
        intent.setClipData(sourceIntent.getClipData());
        intent.setPackage(sourceIntent.getPackage());

        if (sourceIntent.getExtras() != null)
            intent.putExtras(sourceIntent.getExtras());

        if (sourceIntent.getCategories() != null)
            for (String category : sourceIntent.getCategories()) {
                intent.addCategory(category);
            }

        setDataString(sourceIntent.getDataString());
        setScheme(sourceIntent.getScheme());

        setIntentJson(gson.toJson(sourceIntent));
        setIntent(intent);
    }

    private Integer id;

    private Intent intent;

    private String intentJson;
    private String dataString;
    private String scheme;

    public Intent getIntent() {
        return intent;
    }

    private void setIntent(Intent intent) {
        this.intent = intent;
    }

    public String getIntentJson() {
        return intentJson;
    }

    public void setIntentJson(String intentJson) {
        this.intentJson = intentJson;
    }

    public String getDataString() {
        return dataString;
    }

    private void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public String getScheme() {
        return scheme;
    }

    private void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof IntentWrapper) {
            IntentWrapper otherIntentWrapper = (IntentWrapper) obj;

            boolean one = Objects.equals(this.hashCode(), otherIntentWrapper.hashCode());
            boolean two = Objects.equals(this.getId(), otherIntentWrapper.getId());
            boolean three = Objects.equals(this.getScheme(), otherIntentWrapper.getScheme());
            boolean four = Objects.equals(this.getDataString(), otherIntentWrapper.getDataString());

            

            boolean five = Objects.equals(gson.toJson(this.getIntent()), gson.toJson(otherIntentWrapper.getIntent()));

            return one && two && three && four && five;

        } else return false;
    }

    @Override
    public String toString() {
        return getIntentJson();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}