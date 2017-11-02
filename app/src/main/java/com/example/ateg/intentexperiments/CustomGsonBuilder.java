package com.example.ateg.intentexperiments;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

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
        });
    }
}
