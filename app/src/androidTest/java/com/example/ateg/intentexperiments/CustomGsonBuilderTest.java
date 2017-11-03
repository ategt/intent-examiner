package com.example.ateg.intentexperiments;

import android.content.ClipData;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Objects;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
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

                try {
                    in.nextNull();
                    return null;
                } catch (java.lang.IllegalStateException | com.google.gson.JsonSyntaxException ex) {
                }

                Uri result = null;

                try {
                    String value = in.nextString();
                    result = Uri.parse(value);
                } catch (JsonSyntaxException | IllegalStateException | MalformedJsonException ex) {
                }

                try {
                    if (result == null) {
                        String decodedValue = null;

                        in.beginObject();

                        while (in.hasNext()) {
                            String name = in.nextName();

                            if (Objects.equals(name, "path")) {
                                in.beginObject();

                                while (in.hasNext()) {
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
                        }

                        in.endObject();

                        result = decodedValue == null ? null : Uri.parse(decodedValue);
                    }
                } catch (MalformedJsonException ex) {
                }
                return result;
            }
        }).create();

        ClipData clipData = gson.fromJson(testString, ClipData.class);

        assertNotNull(clipData);

        String outputJson = gson.toJson(clipData);

        clipData = gson.fromJson(outputJson, ClipData.class);

        assertNotNull(clipData);

        String dataJson = "null";
        Uri data = gson.fromJson(dataJson, Uri.class);

        assertNull(data);

        dataJson = null;
        data = gson.fromJson(dataJson, Uri.class);

        assertNull(data);
    }

    @Test
    public void gsonExceptionHandlingTest() {
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
        }).create();

        try {
            ClipData clipData = gson.fromJson(testString, ClipData.class);
            fail("This was supposed to fail.");
        } catch (RuntimeException ex) {
            Throwable throwable = ex.getCause();
            String message = throwable.getMessage();

            if (throwable instanceof java.lang.InstantiationException) {
                assertTrue("Handle this somehow.", true);
            } else {
                fail("Also, should not happen.");
            }
        }
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

        clipData = gson.fromJson(outputJson, ClipData.class);

        assertNotNull(clipData);

        testString = gson.toJson(clipData);

        assertEquals(testString, outputJson);
    }

    @Test
    public void customGsonBuilderNullTest() {
        String testString = "null";

        Gson gson = CustomGsonBuilder.get().create();

        ClipData clipData = gson.fromJson(testString, ClipData.class);

        assertNull(clipData);
    }

    @Test
    public void customGsonBuilderNullLiteralTest() {
        String testString = null;

        Gson gson = CustomGsonBuilder.get().create();

        ClipData clipData = gson.fromJson(testString, ClipData.class);

        assertNull(clipData);
    }

    @Test
    public void customGsonBuilderNullUriTest() {
        String testString = "null";

        Gson gson = CustomGsonBuilder.get().create();

        Uri uri = gson.fromJson(testString, Uri.class);

        assertNull(uri);
    }

    @Test
    public void customGsonBuilderNullUriLiteralTest() {
        String testString = null;

        Gson gson = CustomGsonBuilder.get().create();

        Uri uri = gson.fromJson(testString, Uri.class);

        assertNull(uri);
    }
}