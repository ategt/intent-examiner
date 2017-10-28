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

    private Context context;
    private IntentRepository intentRepository;

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        intentRepository = new IntentRepository(context, "TestDataBase.db", 3);
    }

    @Test
    public void createTest() {
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";
        String randomString = UUID.randomUUID().toString();

        Intent intent = new Intent();
        intent.putExtra(randomStringKey, randomString);

        Intent returnedIntent = intentRepository.create(intent);

        assertEquals(intent, returnedIntent);
        assertNotEquals(intent, new Intent());
        assertNotEquals(intent, new Intent().putExtra(randomStringKey, UUID.randomUUID().toString()));
    }

    @Test
    public void getAllTest() {
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";
        String randomString = UUID.randomUUID().toString();

        Intent intent = new Intent();
        intent.putExtra(randomStringKey, randomString);

        intent = intentRepository.create(intent);

        Bundle extras = intent.getExtras();
        intent = removeUnserializablePortions(intent);

        assertEquals(extras, intent.getExtras());

        List<Intent> intentList = intentRepository.getAll();
        assertTrue(intentList.contains(intent));

        assertTrue(intentList.size() > 0);
    }

    private Intent removeUnserializablePortions(Intent intent) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                Log.d("fdsa", f.getName() + " = " + f.toString());
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                Log.d("asd", clazz.toString());
                return clazz.isInstance(java.lang.ClassLoader.class);
            }
        })
                .create();

        String json = gson.toJson(intent);


            intent = gson.fromJson(json, Intent.class);

        return intent;
    }

    @Test
    public void archiveFeatureTest() {
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";

        Intent archivedIntent = intentRepository.create(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString()));

        intentRepository.markAllArchived();

        Intent newIntent = intentRepository.create(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString()));

        List<Intent> intentList = intentRepository.getAll();
        assertTrue(intentList.contains(archivedIntent));
        assertTrue(intentList.contains(newIntent));

        int allListSize = intentList.size();
        assertTrue(allListSize > 1);

        intentList = intentRepository.getDifferential();
        assertFalse(intentList.contains(archivedIntent));
        assertTrue(intentList.contains(newIntent));

        assertTrue(allListSize > intentList.size());
    }

    @Test
    public void resetTest() {
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";

        intentRepository.create(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString()));

        intentRepository.deleteAll();

        List<Intent> intentList = intentRepository.getAll();

        assertNotNull(intentList);
        assertTrue(intentList.isEmpty());

        intentList = intentRepository.getDifferential();

        assertNotNull(intentList);
        assertTrue(intentList.isEmpty());
    }
}