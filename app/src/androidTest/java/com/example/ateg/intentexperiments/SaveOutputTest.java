package com.example.ateg.intentexperiments;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.EspressoKey;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.OngoingStubbing;
import android.support.test.espresso.intent.VerificationMode;
import android.support.test.espresso.intent.VerificationModes;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by ATeg on 6/14/2017.
 */
@RunWith(AndroidJUnit4.class)
public class SaveOutputTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void before(){
        Intents.init();
    }

    @After
    public void after(){
        Intents.release();
    }

    @Test
    public void examineSaveEmptyIntentTest() {
        Intent intent = new Intent();
        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);

        intending(IntentMatchers.hasAction(Intent.ACTION_VIEW)).respondWith(activityResult);

        LoggingUtilities loggingUtilities = new LoggingUtilities(
                InstrumentationRegistry.getTargetContext(),
                "IntentExamination.txt",
                Environment.DIRECTORY_DOCUMENTS);

        File logFile = loggingUtilities.getLogFile();

        long startingLogFileSize = logFile.exists() ? logFile.length() : -1;

        Espresso.onView(withId(R.id.action_button)).perform(click());

        Espresso.onView(withId(R.id.central_textView)).check(matches(withText(R.string.intent_empty)));

        Espresso.onView(withId(R.id.save_to_file)).perform(click());

        Assert.assertTrue(logFile.exists());
        Assert.assertTrue(startingLogFileSize < logFile.length());

        Espresso.onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("File Saved")))
                .check(matches(isDisplayed()));

        String logFileContents = LoggingUtilities.readFile(InstrumentationRegistry.getTargetContext(),
                logFile);

        String emptyIntentDisplay = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.intent_empty);
        Assert.assertTrue(logFileContents.contains(emptyIntentDisplay));

        Espresso.onView(allOf(withId(android.support.design.R.id.snackbar_action)))
                .perform(click());

        Matcher<Intent> intentMatcher = IntentMatchers.anyIntent();
        Intents.intended(intentMatcher);
    }

    @Test
    public void examineSaveTextTest() {
        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent());
        intending(IntentMatchers.hasAction(Intent.ACTION_VIEW)).respondWith(activityResult);

        LoggingUtilities loggingUtilities = new LoggingUtilities(
                InstrumentationRegistry.getTargetContext(),
                "IntentExamination.txt",
                Environment.DIRECTORY_DOCUMENTS);

        File logFile = loggingUtilities.getLogFile();

        long startingLogFileSize = logFile.exists() ? logFile.length() : -1;

        String randomString = UUID.randomUUID().toString();

        Intent intent = new Intent();
        intent.putExtra("com.example.ateg.intentexperiments.RANDOM_STRING", randomString);

        MainActivity mainActivity = mainActivityActivityTestRule.launchActivity(intent);

        Espresso.onView(withId(R.id.action_button)).perform(click());

        Espresso.onView(withId(R.id.central_textView)).check(matches(not(withText(R.string.intent_empty))));

        Espresso.onView(withId(R.id.save_to_file)).perform(click());

        Assert.assertTrue(logFile.exists());
        Assert.assertTrue(startingLogFileSize < logFile.length());

        Espresso.onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("File Saved")))
                .check(matches(isDisplayed()));

        TextView textView = (TextView) mainActivity.findViewById(R.id.central_textView);
        String textViewText = textView.getText().toString();

        Assert.assertTrue(textViewText.contains(randomString));

        String logFileContents = LoggingUtilities.readFile(InstrumentationRegistry.getTargetContext(),
                logFile);

        Assert.assertTrue(logFileContents.contains(randomString));

        Espresso.onView(allOf(withId(android.support.design.R.id.snackbar_action)))
                .perform(click());

        VerificationMode expectTwoMatchingIntents = Intents.times(2);
        Matcher<Intent> intentMatcher = IntentMatchers.anyIntent();

        Intents.intended(IntentMatchers.hasAction("android.intent.action.VIEW"));

        Intents.intended(intentMatcher, expectTwoMatchingIntents);
    }

    @Test
    public void examineTextIntegerBooleanTest() {
        String randomString = UUID.randomUUID().toString();
        Random random = new Random();
        int randomInt = random.nextInt();
        Integer randomInteger = new Integer(random.nextInt());
        boolean randomBool = random.nextBoolean();
        Boolean randomBoolean = new Boolean(random.nextBoolean());

        Intent intent = new Intent();
        intent.putExtra("com.example.ateg.intentexperiments.RANDOM_STRING", randomString);
        intent.putExtra("com.example.ateg.intentexperiments.RANDOM_INTEGER", randomInt);
        intent.putExtra("com.example.ateg.intentexperiments.RANDOM_INTEGER_OBJECT", randomInteger);

        intent.putExtra("com.example.ateg.intentexperiments.RANDOM_BOOLEAN", randomBool);
        intent.putExtra("com.example.ateg.intentexperiments.RANDOM_BOOLEAN_OBJECT", randomBoolean);

        MainActivity mainActivity = mainActivityActivityTestRule.launchActivity(intent);

        Espresso.onView(withId(R.id.action_button)).perform(click());

        Espresso.onView(withId(R.id.central_textView)).check(matches(not(withText(R.string.intent_empty))));


        TextView textView = (TextView) mainActivity.findViewById(R.id.central_textView);
        String textViewText = textView.getText().toString();

        Assert.assertTrue(textViewText.contains(randomString));
        Assert.assertTrue(textViewText.contains(randomBool ? "true" : "false"));
        Assert.assertTrue(textViewText.contains(randomBoolean ? "true" : "false"));
        Assert.assertTrue(textViewText.contains(String.valueOf(randomInt)));
        Assert.assertTrue(textViewText.contains(String.valueOf(randomInteger)));

        Assert.assertTrue(textViewText.contains(randomString));
    }
}