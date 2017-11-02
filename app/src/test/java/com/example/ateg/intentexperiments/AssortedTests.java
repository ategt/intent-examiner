package com.example.ateg.intentexperiments;

import android.content.ClipData;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by ATeg on 11/2/2017.
 */

public class AssortedTests {

    @Test
    public void gsonDeserializationTest(){
        String testString = "{\"mClipDescription\":{\"mMimeTypes\":[\"text/plain\"]},\"mItems\":[{\"mText\":\"https://stackoverflow.com/questions/22955691/sending-file-via-bluetooth-using-intent-action-send-does-not-working-in-android\"}]}";

        Gson gson = new GsonBuilder().create();

        ClipData clipData = gson.fromJson(testString, ClipData.class);

        assertNotNull(clipData);
    }

    @Test
    public void gsonDeserializationByRepoTest(){
        String testString = "{\"mClipDescription\":{\"mMimeTypes\":[\"text/plain\"]},\"mItems\":[{\"mText\":\"https://stackoverflow.com/questions/22955691/sending-file-via-bluetooth-using-intent-action-send-does-not-working-in-android\"}]}";

        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                if (clazz.isInstance(ClassLoader.class))
                    return true;
                else
                    return false;
            }
        }).create();

        ClipData clipData = gson.fromJson(testString, ClipData.class);

        assertNotNull(clipData);
    }
}
