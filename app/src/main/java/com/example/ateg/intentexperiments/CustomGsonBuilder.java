package com.example.ateg.intentexperiments;

import android.net.Uri;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by ATeg on 11/2/2017.
 */

public class CustomGsonBuilder {
    public static GsonBuilder get() {
        return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                String name = clazz.getName();
                if (clazz.isInstance(ClassLoader.class) || name.equalsIgnoreCase("java.lang.ClassLoader"))
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
        }).registerTypeAdapter(Uri.class, new TypeAdapter<Uri>() {
            @Override
            public void write(JsonWriter out, Uri value) throws IOException {
                out.value(value == null ? null : value.toString());
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
        });
    }
}
