package com.example.ateg.intentexperiments;

import android.content.ClipData;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CustomGsonBuilderTest {

    @Test
    public void gsonDeserializationByRepoTest() {
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
        }).registerTypeAdapter(java.lang.CharSequence.class, new TypeAdapter<CharSequence>() {
            @Override
            public void write(JsonWriter out, CharSequence value) throws IOException {
                out.jsonValue(value == null ? null : value.toString());
            }

            @Override
            public CharSequence read(JsonReader in) throws IOException {
                String string = in.nextString();
                return string;
            }
        }).create();

        ClipData clipData = gson.fromJson(testString, ClipData.class);

        assertNotNull(clipData);

        String outputJson = gson.toJson(clipData);

        assertEquals(testString, outputJson);

        clipData = gson.fromJson(outputJson, ClipData.class);

        assertNotNull(clipData);
    }

    @Test
    public void customGsonBuilderTest() {
        String testString = "{\"mClipDescription\":{\"mMimeTypes\":[\"text/plain\"]},\"mItems\":[{\"mText\":\"https://stackoverflow.com/questions/22955691/sending-file-via-bluetooth-using-intent-action-send-does-not-working-in-android\"}]}";

        Gson gson = CustomGsonBuilder.get().create();

        ClipData clipData = gson.fromJson(testString, ClipData.class);

        assertNotNull(clipData);

        String outputJson = gson.toJson(clipData);

        assertEquals(testString, outputJson);

        clipData = gson.fromJson(outputJson, ClipData.class);

        assertNotNull(clipData);
    }
}