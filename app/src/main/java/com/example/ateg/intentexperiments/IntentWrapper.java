package com.example.ateg.intentexperiments;

import android.content.Intent;

/**
 * Created by ATeg on 10/28/2017.
 */

public class IntentWrapper {
    public IntentWrapper(){}
    public IntentWrapper(Intent sourceIntent){
        buildIntent(sourceIntent);
    }

    private void buildIntent(Intent sourceIntent) {
        Intent intent = new Intent();

        intent.setAction(sourceIntent.getAction());
        intent.setComponent(sourceIntent.getComponent());
        intent.setFlags(sourceIntent.getFlags());
        intent.putExtras(sourceIntent.getExtras());
        intent.setType(sourceIntent.getType());
        intent.setData(sourceIntent.getData());
        intent.setClipData(sourceIntent.getClipData());
        intent.setPackage(sourceIntent.getPackage());

        for (String category : sourceIntent.getCategories()) {
            intent.addCategory(category);
        }

        setDataString(sourceIntent.getDataString());
        setScheme(sourceIntent.getScheme());
    }

    private Intent intent;

    private String intentJson;
    private String dataString;
    private String scheme;


    public Intent getIntent() {
        return intent;
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
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntentWrapper){
            
        }
    }
}