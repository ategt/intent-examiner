package com.example.ateg.intentexperiments;

import android.content.ClipData;
import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Objects;

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
    public void gsonDeserializationWithURITest() {
        String testString = "{\"mClipDescription\":{\"mMimeTypes\":[\"*/*\"]},\"mItems\":[{\"mUri\":{\"authority\":{\"decoded\":\"\",\"encoded\":\"\"},\"fragment\":{},\"path\":{\"decoded\":\"/storage/extSdCard/Android/data/com.example.ateg.intentexperiments/files/Documents/temp_stream_21275b72-6031-44b5-9688-5a9ea4d9dad5.tmp\",\"encoded\":\"NOT CACHED\"},\"query\":{},\"scheme\":\"file\",\"uriString\":\"NOT CACHED\",\"host\":\"NOT CACHED\",\"port\":-2}}]}";

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
        }).registerTypeAdapter(CharSequence.class, new TypeAdapter<CharSequence>() {
            @Override
            public void write(JsonWriter out, CharSequence value) throws IOException {
                out.jsonValue(value == null ? null : value.toString());
            }

            @Override
            public CharSequence read(JsonReader in) throws IOException {
                String string = in.nextString();
                return string;
            }
        }).registerTypeAdapter(Uri.class, new TypeAdapter<Uri>() {
            @Override
            public void write(JsonWriter out, Uri value) throws IOException {
                out.value(value.toString());
            }

            @Override
            public Uri read(JsonReader in) throws IOException {

                Uri result = null;

                try {
                    String path = in.getPath();
                    Log.d("adf", "Path: " + path);
                    //String name = in.nextName();
                    //Log.d("adf", "Name: " + name);
                    String value = in.nextString();
                    result = Uri.parse(value);
                } catch (JsonSyntaxException | IllegalStateException | MalformedJsonException ex) {
                    Log.d("adf", "Value lifting failed.");
                }

                try {
                    if (result == null) {
                        boolean has = in.hasNext();

                        //JsonToken jsonToken = in.peek();
                        //jsonToken.
                        String path = in.getPath();

                        //android.net.Uri uri = Uri.parse("/storage/extSdCard/Android/data/com.example.ateg.intentexperiments/files/Documents/temp_stream_21275b72-6031-44b5-9688-5a9ea4d9dad5.tmp");

                        //in.

                        String decodedValue = null;

                        in.beginObject();


                        while (in.hasNext()) { // && decodedValue == null) {
                            String name = in.nextName();

                            if (Objects.equals(name, "path")) {
                                in.beginObject();

                                while (in.hasNext()) { // && decodedValue == null) {
                                    String lookForDecode = in.nextName();
                                    if (Objects.equals(lookForDecode, "decoded")) {
                                        decodedValue = in.nextString();
                                    } else {
                                        in.skipValue();
                                    }
                                }

                                in.endObject();
                            } else {
                                in.skipValue();
                            }
                            //String string = in.nextString();
                        }

                        //String name = in.nextName();
                        in.endObject();

                        //android.net.Uri uri = null;
                        //uri.

                        result = decodedValue == null ? null : Uri.parse(decodedValue);
                    }
                } catch (MalformedJsonException ex) {
                    Log.d("adf", "Parsing failed.");
                    String path = in.getPath();
                    Log.d("adf", "Path: " + path);
                }
                //return null;
                return result;
            }
        }).create();

        ClipData clipData = gson.fromJson(testString, ClipData.class);

        assertNotNull(clipData);

        String outputJson = gson.toJson(clipData);

        //assertEquals(testString, outputJson);

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

    @Test
    public void customGsonBuilderURITest() {
        String testString = "{\"mClipDescription\":{\"mMimeTypes\":[\"*/*\"]},\"mItems\":[{\"mUri\":{\"authority\":{\"decoded\":\"\",\"encoded\":\"\"},\"fragment\":{},\"path\":{\"decoded\":\"/storage/extSdCard/Android/data/com.example.ateg.intentexperiments/files/Documents/temp_stream_21275b72-6031-44b5-9688-5a9ea4d9dad5.tmp\",\"encoded\":\"NOT CACHED\"},\"query\":{},\"scheme\":\"file\",\"uriString\":\"NOT CACHED\",\"host\":\"NOT CACHED\",\"port\":-2}}]}";

        Gson gson = CustomGsonBuilder.get().create();

        ClipData clipData = gson.fromJson(testString, ClipData.class);

        assertNotNull(clipData);

        String outputJson = gson.toJson(clipData);

        assertEquals(testString, outputJson);

        clipData = gson.fromJson(outputJson, ClipData.class);

        assertNotNull(clipData);
    }
}