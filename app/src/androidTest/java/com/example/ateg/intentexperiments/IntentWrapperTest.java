package com.example.ateg.intentexperiments;

import android.content.Intent;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by ATeg on 10/29/2017.
 */

public class IntentWrapperTest {

    @Test
    public void wrapperTest(){
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";
        String randomString = UUID.randomUUID().toString();

        Intent intent = new Intent();
        intent.putExtra(randomStringKey, randomString);

        IntentWrapper intentWrapper = new IntentWrapper(intent);

        IntentWrapper intentWrapper1
                = new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString()));

        IntentWrapper intentWrapper2
                = new IntentWrapper(new Intent().putExtra(randomStringKey, randomString));

        assertEquals(intentWrapper, intentWrapper2);
        assertNotEquals(intentWrapper, intentWrapper1);
    }
}
