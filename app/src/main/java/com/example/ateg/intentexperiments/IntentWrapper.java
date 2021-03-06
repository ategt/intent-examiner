package com.example.ateg.intentexperiments;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by ATeg on 10/28/2017.
 */

public class IntentWrapper {

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

    public Bundle getExtras() {
        return intent.getExtras();
    }

    public String getAction() {
        return intent.getAction();
    }

    public Set<String> getCategories() {
        return intent.getCategories();
    }

    public int getFlags() {
        return intent.getFlags();
    }

    public String getType() {
        return intent.getType();
    }

    public String getPackage() {
        return intent.getPackage();
    }

    public ComponentName getComponent() {
        return intent.getComponent();
    }

    public ClipData getClipData() {
        return intent.getClipData();
    }

    public Uri getData() {
        return intent.getData();
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof IntentWrapper) {
            IntentWrapper otherIntentWrapper = (IntentWrapper) obj;

            boolean one = Objects.equals(this.hashCode(), otherIntentWrapper.hashCode()) &&
                    Objects.equals(this.getId(), otherIntentWrapper.getId()) &&
                    Objects.equals(this.getScheme(), otherIntentWrapper.getScheme()) &&
                    Objects.equals(this.getDataString(), otherIntentWrapper.getDataString());

            Bundle thisBundle = this.getExtras();
            Bundle otherBundle = otherIntentWrapper.getExtras();

            boolean six = thisBundle != null && otherBundle != null ? Objects.equals(thisBundle.size(), otherBundle.size()) : true;

            boolean five;

            if (thisBundle != null) {
                five = thisBundle.size() > 0 ? false : true;
                for (String key : thisBundle.keySet()) {
                    if (otherBundle != null &&
                            otherBundle.containsKey(key) &&
                            Objects.equals(thisBundle.getParcelable(key), otherBundle.getParcelable(key)) &&
                            Objects.equals(thisBundle.get(key), otherBundle.get(key))
                            ) {
                        five = true;
                    } else {
                        five = false;
                        break;
                    }
                }
            } else {
                five = thisBundle == null && otherBundle == null;
            }

            boolean seven = Objects.equals(this.getAction(), otherIntentWrapper.getAction());
            boolean eight = Objects.equals(this.getCategories(), otherIntentWrapper.getCategories());
            boolean nine = Objects.equals(this.getFlags(), otherIntentWrapper.getFlags());
            boolean ten = Objects.equals(this.getType(), otherIntentWrapper.getType());
            boolean eleven = Objects.equals(this.getPackage(), otherIntentWrapper.getPackage());
            boolean twelve = Objects.equals(this.getComponent(), otherIntentWrapper.getComponent());
            boolean thirteen = Objects.equals(this.getClipData(), otherIntentWrapper.getClipData());
            boolean fourteen = Objects.equals(this.getData(), otherIntentWrapper.getData());

            return one && five && six && seven && eight && nine && ten && eleven && twelve && thirteen && fourteen;

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