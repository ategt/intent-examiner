package com.example.ateg.intentexperiments;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ATeg on 11/1/2017.
 */
public class IntentTextRepositoryTest {

    private IntentRepository intentRepository;
    private File file;

    @Before
    public void setup() throws IOException {
        Context context = InstrumentationRegistry.getTargetContext();
        file = File.createTempFile("test", ".tmp");
        // file = new File(UUID.randomUUID().toString());
        LoggingUtilities loggingUtilities = new LoggingUtilities(context, file);
        intentRepository = new IntentTextRepository(loggingUtilities);
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

        String content = LoggingUtilities.readFile(new Instrumentation().getTargetContext(), file);

        assertTrue(content.contains(randomString));
        assertTrue(content.contains(randomStringKey));
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

        String content = LoggingUtilities.readFile(new Instrumentation().getTargetContext(), file);

        assertTrue(content.contains(randomStringKey));
        assertEquals(content.split(randomStringKey).length, 4);

        for (IntentWrapper intentWrapper : list) {
            assertTrue(content.contains((String) intentWrapper.getExtras().get(randomStringKey)));
        }
    }
}