package com.example.ateg.intentexperiments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class IntentSqliteRepositoryTest {

    private Gson gson = new GsonBuilder().create();

    private Context context;
    private IntentRepository intentRepository;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        intentRepository = new IntentRepository(context, "TestDataBase.db", 6);
    }

    @Test
    public void createTest() {
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";
        String randomString = UUID.randomUUID().toString();

        Intent intent = new Intent();
        intent.putExtra(randomStringKey, randomString);

        IntentWrapper wrappedIntent = new IntentWrapper(intent);

        IntentWrapper returnedWrappedIntent = intentRepository.create(wrappedIntent);

        assertEquals(wrappedIntent, returnedWrappedIntent);
        assertNotEquals(wrappedIntent, new IntentWrapper(new Intent()));
        assertNotEquals(wrappedIntent, new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));
    }

    @Test
    public void getAllTest() {
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";
        String randomString = UUID.randomUUID().toString();

        Intent intent = new Intent();
        intent.putExtra(randomStringKey, randomString);

        IntentWrapper wrappedIntent = new IntentWrapper(intent);
        wrappedIntent = intentRepository.create(wrappedIntent);

        Bundle extras = wrappedIntent.getIntent().getExtras();

        assertEquals(extras.hashCode() + " has " + extras.size() + ", " +
                        intent.getExtras().hashCode() + " has " + intent.getExtras().size() + "\n" +
                        "1-" + gson.toJson(extras) +
                        "2-" + gson.toJson(intent.getExtras())
                , gson.toJson(extras),
                gson.toJson(intent.getExtras()));

        List<IntentWrapper> intentList = intentRepository.getAll();

        assertTrue(intentList.contains(wrappedIntent));

        assertTrue(intentList.size() > 0);
    }

    @Test
    public void archiveFeatureTest() {
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";

        IntentWrapper archivedIntentWrapper = intentRepository.create(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));

        intentRepository.markAllArchived();

        IntentWrapper newIntentWrapper = intentRepository.create(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));

        List<IntentWrapper> intentList = intentRepository.getAll();
        assertTrue(intentList.contains(archivedIntentWrapper));
        assertTrue(intentList.contains(newIntentWrapper));

        int allListSize = intentList.size();
        assertTrue(allListSize > 1);

        intentList = intentRepository.getDifferential();
        assertFalse(intentList.contains(archivedIntentWrapper));
        assertTrue(intentList.contains(newIntentWrapper));

        assertTrue(allListSize > intentList.size());
    }

    @Test
    public void resetTest() {
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";

        intentRepository.create(new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString())));

        intentRepository.deleteAll();

        List<IntentWrapper> intentList = intentRepository.getAll();

        assertNotNull(intentList);
        assertTrue(intentList.isEmpty());

        intentList = intentRepository.getDifferential();

        assertNotNull(intentList);
        assertTrue(intentList.isEmpty());
    }
}