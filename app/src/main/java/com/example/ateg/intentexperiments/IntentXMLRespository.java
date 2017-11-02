package com.example.ateg.intentexperiments;

import com.google.gson.Gson;

import java.io.File;
import java.util.List;

/**
 * Created by ATeg on 11/1/2017.
 */

class IntentXMLRespository implements IntentRepository {

    File file;
    LoggingUtilities loggingUtilities;

    public IntentXMLRespository(File file, LoggingUtilities loggingUtilities) {
        this.file = file;
        this.loggingUtilities = loggingUtilities;
    }

    @Override
    public List<IntentWrapper> create(List<IntentWrapper> intentWrapperList) {

        JAXBContext jaxbContext = JAXBContext.newInstance(IntentWrapper.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        List<IntentWrapper> tile = jaxbUnmarshaller.unmarshal(file);

    }

    @Override
    public IntentWrapper create(IntentWrapper intentWrapper) {
        return null;
    }

    @Override
    public List<IntentWrapper> getAll() {
        return null;
    }

    @Override
    public List<IntentWrapper> getDifferential() {
        return null;
    }

    @Override
    public void markAllArchived() {

    }

    @Override
    public void deleteAll() {

    }
}
