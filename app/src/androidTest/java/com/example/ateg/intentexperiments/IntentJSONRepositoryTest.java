package com.example.ateg.intentexperiments;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ATeg on 11/1/2017.
 */
public class IntentJSONRepositoryTest {

    private IntentRepository intentRepository;
    private File file;

    @Before
    public void setup() {
        file = new File(UUID.randomUUID().toString());
        Context context = new Instrumentation().getTargetContext();
        LoggingUtilities loggingUtilities = new LoggingUtilities(context, file);
        intentRepository = new IntentJsonRepository(loggingUtilities);
    }

    @After
    public void tearDown() {
        if (file.exists())
            file.delete();
    }

    @Test
    public void create() throws Exception {
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";
        String randomString = UUID.randomUUID().toString();

        Intent intent = new Intent();
        intent.putExtra(randomStringKey, randomString);

        IntentWrapper intentWrapper = new IntentWrapper(intent);

        intentWrapper = intentRepository.create(intentWrapper);

        assertNotNull(intentWrapper);
    }

    @Test
    public void create1() throws Exception {
        List<IntentWrapper> list = new ArrayList<>();
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";

        list.add(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));
        list.add(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));
        list.add(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));

        list = intentRepository.create(list);

        assertNotNull(list);
    }

    @Test
    public void getAll() throws Exception {
        List<IntentWrapper> list = new ArrayList<>();
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";

        list.add(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));
        list.add(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));
        list.add(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));

        List<IntentWrapper> resultList = intentRepository.create(list);

        List<IntentWrapper> retrievedList = intentRepository.getAll();

        for (IntentWrapper intentWrapper : list) {
            assertTrue(retrievedList.contains(intentWrapper));
        }

        for (IntentWrapper intentWrapper : list) {
            assertTrue(resultList.contains(intentWrapper));
        }
    }

    @Test
    public void getDifferential() throws Exception {
        List<IntentWrapper> list = new ArrayList<>();
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";

        list.add(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));
        list.add(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));
        list.add(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));

        List<IntentWrapper> resultList = intentRepository.create(list);

        List<IntentWrapper> retrievedList = intentRepository.getDifferential();

        for (IntentWrapper intentWrapper : list) {
            assertTrue(retrievedList.contains(intentWrapper));
        }

        for (IntentWrapper intentWrapper : list) {
            assertTrue(resultList.contains(intentWrapper));
        }
    }

    @Test
    public void markAllArchived() throws Exception {

    }

    @Test
    public void deleteAll() throws Exception {

    }

}