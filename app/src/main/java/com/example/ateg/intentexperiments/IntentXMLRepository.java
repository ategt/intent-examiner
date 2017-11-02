package com.example.ateg.intentexperiments;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by ATeg on 11/1/2017.
 */

class IntentXMLRepository implements IntentRepository {

    File file;

    public IntentXMLRepository(File file) {
        this.file = file;
    }

    @Override
    public List<IntentWrapper> create(List<IntentWrapper> intentWrapperList) {
        Serializer serializer = new Persister();

        try {
            serializer.write(intentWrapperList, file);
            return intentWrapperList;
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong.");
        }
    }

    @Override
    public IntentWrapper create(IntentWrapper intentWrapper) {
        Serializer serializer = new Persister();

        try {
            serializer.write(intentWrapper, file);
            return intentWrapper;
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong.");
        }
    }

    @Override
    public List<IntentWrapper> getAll() {
        Serializer serializer = new Persister();

        try {
            IntentWrapper[] result = serializer.read(IntentWrapper[].class, file);
            return Arrays.asList(result);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong.");
        }
    }

    @Override
    public List<IntentWrapper> getDifferential() {
        return getAll();
    }

    @Override
    public void markAllArchived() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }
}
