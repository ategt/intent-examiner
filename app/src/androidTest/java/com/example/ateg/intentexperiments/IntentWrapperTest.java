package com.example.ateg.intentexperiments;

import android.content.Intent;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void wrapperListTest(){
        String randomStringKey = "com.example.ateg.intentexperiments.RANDOM_STRING";
        String randomString = UUID.randomUUID().toString();

        Intent intent = new Intent();
        intent.putExtra(randomStringKey, randomString);

        IntentWrapper intentWrapper = new IntentWrapper(intent);
        intentWrapper.setId(0);

        IntentWrapper intentWrapper1
                = new IntentWrapper(new Intent().putExtra(randomStringKey, UUID.randomUUID().toString()));
        intentWrapper1.setId(1);

        IntentWrapper intentWrapper2
                = new IntentWrapper(new Intent().putExtra(randomStringKey, randomString));
        intentWrapper2.setId(0);

        assertEquals(intentWrapper, intentWrapper2);
        assertNotEquals(intentWrapper, intentWrapper1);

        List<IntentWrapper> intentWrapperList = new ArrayList<>();
        intentWrapperList.add(intentWrapper1);
        intentWrapperList.add(intentWrapper2);

        assertTrue(intentWrapperList.contains(intentWrapper));
        assertFalse(intentWrapperList.contains(new IntentWrapper(new Intent())));
    }

}
