package com.example.ateg.intentexperiments;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;

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
        intentRepository = new IntentRepository(context, IntentRepository.INTENT_EXAMINER_TABLE_NAME, 2);
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

        List<Intent> intentList = intentRepository.getAll();
        assertTrue(intentList.contains(intent));

        assertTrue(intentList.size() > 0);
    }
}