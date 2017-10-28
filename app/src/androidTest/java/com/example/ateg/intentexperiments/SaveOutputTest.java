package com.example.ateg.intentexperiments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.core.deps.guava.base.Objects;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.VerificationMode;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Created by ATeg on 6/14/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SaveOutputTest {

    private List<File> filesToBeDeleted;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void before() {
        Intents.init();
        filesToBeDeleted = new ArrayList<>();

        mainActivityActivityTestRule.getActivity()
                .getFragmentManager()
                .beginTransaction();
    }

    @After
    public void after() {
        Intents.release();
        if (filesToBeDeleted != null) {
            for (File file : filesToBeDeleted) {
                if (file != null)
                    file.delete();
            }
        }
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
        Assert.assertTrue("StartingLogFileSize: " + startingLogFileSize
                        + ", LogFile Length: " + logFile.length(),
                startingLogFileSize < logFile.length());

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
    public void examineSaveAsEmptyIntentTest() {
        Intent intent = new Intent();
        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);

        intending(IntentMatchers.hasAction(Intent.ACTION_VIEW)).respondWith(activityResult);

        String newFileName = "TestIntentExamination.txt";

        LoggingUtilities loggingUtilities = new LoggingUtilities(
                InstrumentationRegistry.getTargetContext(),
                "StartingIntentExamination.txt",
                Environment.DIRECTORY_DOCUMENTS);

        File templogFile = loggingUtilities.getLogFile();
        File testLogFile = new File(templogFile.getParentFile(), newFileName);
        File originalLogFile = new File(templogFile.getParentFile(), "IntentExamination.txt");

        filesToBeDeleted.add(testLogFile);
        //filesToBeDeleted.add(templogFile);

        long startingOriginalLogFileSize = originalLogFile.length();
        long startingOriginalLogFileModifiedDate = originalLogFile.lastModified();

        Assert.assertFalse(testLogFile.exists());
        Assert.assertFalse(templogFile.exists());

        long startingLogFileSize = -1L;

        Espresso.onView(withId(R.id.action_button)).perform(click());

        Espresso.onView(withId(R.id.central_textView)).check(matches(withText(R.string.intent_empty)));

        Espresso.onView(withId(R.id.save_to_file_as)).perform(click());

        Espresso.onView(withId(R.id.fileName_editText)).perform(clearText());
        Espresso.onView(withId(R.id.fileName_editText)).perform(ViewActions.typeText(newFileName));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.fileSaveLoad)).check(matches(withText("Save")));

        Espresso.onView(withId(R.id.fileSaveLoad)).perform(ViewActions.scrollTo());

        Espresso.onView(withId(R.id.fileSaveLoad)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.fileSaveLoad)).check(ViewAssertions.matches(ViewMatchers.isClickable()));

        long beforeClick = System.currentTimeMillis();

        long temp = Math.round(Math.pow(10.0, 4));
        beforeClick /= temp;

        beforeClick *= temp;

        Espresso.onView(withId(R.id.fileSaveLoad)).perform(click());

        Assert.assertTrue(testLogFile.exists());

        Assert.assertEquals(startingOriginalLogFileModifiedDate, originalLogFile.lastModified());
        Assert.assertEquals(startingOriginalLogFileSize, originalLogFile.length());

        Assert.assertFalse(templogFile.exists());

        Assert.assertTrue(testLogFile.length() > 0);

        Assert.assertTrue(beforeClick <= testLogFile.lastModified());

        Assert.assertTrue(System.currentTimeMillis() >= testLogFile.lastModified());

        Assert.assertTrue(startingLogFileSize < testLogFile.length());

        Espresso.onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("File Saved")))
                .check(matches(isDisplayed()));

        String logFileContents = LoggingUtilities.readFile(InstrumentationRegistry.getTargetContext(),
                testLogFile);

        String emptyIntentDisplay = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.intent_empty);
        Assert.assertTrue(logFileContents.contains(emptyIntentDisplay));

        Espresso.onView(allOf(withId(android.support.design.R.id.snackbar_action)))
                .perform(click());

        Matcher<Intent> intentMatcher = IntentMatchers.anyIntent();
        Intents.intended(intentMatcher);

        testLogFile.delete();
    }

    @Test
    public void examineSaveAsIntentWithContentTest() {
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


        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent());

        intending(IntentMatchers.hasAction(Intent.ACTION_VIEW)).respondWith(activityResult);

        String newFileName = "TestIntentExamination.txt";

        LoggingUtilities loggingUtilities = new LoggingUtilities(
                InstrumentationRegistry.getTargetContext(),
                "StartingIntentExamination.txt",
                Environment.DIRECTORY_DOCUMENTS);

        File templogFile = loggingUtilities.getLogFile();
        File testLogFile = new File(templogFile.getParentFile(), newFileName);
        File originalLogFile = new File(templogFile.getParentFile(), "IntentExamination.txt");

        filesToBeDeleted.add(testLogFile);

        long startingOriginalLogFileSize = originalLogFile.length();
        long startingOriginalLogFileModifiedDate = originalLogFile.lastModified();

        Assert.assertFalse(testLogFile.exists());
        Assert.assertFalse(templogFile.exists());

        long startingLogFileSize = -1L;

        MainActivity mainActivity = mainActivityActivityTestRule.launchActivity(intent);
        Espresso.onView(withId(R.id.action_button)).perform(click());

        String emptyIntentString = InstrumentationRegistry.getTargetContext().getResources().getString(R.string.intent_empty);

        TextView textView = (TextView) mainActivityActivityTestRule.getActivity().findViewById(R.id.central_textView);
        String textViewText = textView.getText().toString();

        Assert.assertFalse(textViewText.contains(emptyIntentString));

        try {
            Espresso.onView(withId(R.id.save_to_file_as)).perform(click());
        } catch (android.support.test.espresso.NoMatchingViewException ex) {
            Espresso.openActionBarOverflowOrOptionsMenu(getTargetContext());
            Espresso.onView(withText(getTargetContext().getString(R.string.save_to_file_as)))
                    .perform(click());
        }

        Espresso.onView(withId(R.id.fileName_editText)).perform(clearText());
        Espresso.onView(withId(R.id.fileName_editText)).perform(ViewActions.typeText(newFileName));

        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.fileSaveLoad)).check(matches(withText("Save")));

        Espresso.onView(withId(R.id.fileSaveLoad)).perform(ViewActions.scrollTo());

        Espresso.onView(withId(R.id.fileSaveLoad)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.fileSaveLoad)).check(ViewAssertions.matches(ViewMatchers.isClickable()));

        long beforeClick = System.currentTimeMillis();

        long temp = Math.round(Math.pow(10.0, 4));
        beforeClick /= temp;

        beforeClick *= temp;

        Espresso.onView(withId(R.id.fileSaveLoad)).perform(click());

        Assert.assertTrue(testLogFile.exists());

        Assert.assertEquals(startingOriginalLogFileModifiedDate, originalLogFile.lastModified());
        Assert.assertEquals(startingOriginalLogFileSize, originalLogFile.length());

        Assert.assertFalse(templogFile.exists());

        Assert.assertTrue(testLogFile.length() > 0);

        Assert.assertTrue(beforeClick <= testLogFile.lastModified());

        Assert.assertTrue(System.currentTimeMillis() >= testLogFile.lastModified());

        Assert.assertTrue(startingLogFileSize < testLogFile.length());

        Espresso.onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("File Saved")))
                .check(matches(isDisplayed()));

        String logFileContents = LoggingUtilities.readFile(InstrumentationRegistry.getTargetContext(),
                testLogFile);

        Assert.assertTrue(logFileContents.contains(randomString));
        Assert.assertTrue(logFileContents.contains(String.valueOf(randomInt)));
        Assert.assertTrue(logFileContents.contains(randomInteger.toString()));
        Assert.assertTrue(logFileContents.contains(String.valueOf(randomBool)));
        Assert.assertTrue(logFileContents.contains(randomBoolean.toString()));

        Espresso.onView(allOf(withId(android.support.design.R.id.snackbar_action)))
                .perform(click());

        Matcher<Intent> intentMatcher = IntentMatchers.anyIntent();
        Intents.intended(intentMatcher, Intents.times(2));

        testLogFile.delete();
    }

    @Test
    public void clearLineButtonTest() {
        Espresso.onView(withId(R.id.action_button)).perform(click());

        try {
            Espresso.onView(withId(R.id.save_to_file_as)).perform(click());
        } catch (android.support.test.espresso.NoMatchingViewException ex) {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            Espresso.onView(withText(getInstrumentation().getTargetContext().getString(R.string.save_to_file_as)))
                    .perform(click());
            //Espresso.onView(withId(R.id.save_to_file_as)).perform(click());
        }

        Espresso.onView(withId(R.id.fileName_editText)).check(matches(withText("IntentExamination.txt")));

        Espresso.onView(withId(R.id.clearLine)).perform(click());

        Espresso.onView(withId(R.id.fileName_editText)).check(matches(not(withText("IntentExamination.txt"))));
        Espresso.onView(withId(R.id.fileName_editText)).check(matches(withText("")));

        Espresso.onView(withId(R.id.fileCancel)).perform(click());
    }

    @Test
    public void clearLineButtonLandscapeTest() {
        mainActivityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Espresso.onView(withId(R.id.action_button)).perform(click());
        Espresso.onView(withId(R.id.save_to_file_as)).perform(click());

        Espresso.onView(withId(R.id.fileName_editText)).check(matches(withText("IntentExamination.txt")));

        Espresso.onView(withId(R.id.clearLine)).perform(click());

        Espresso.onView(withId(R.id.fileName_editText)).check(matches(not(withText("IntentExamination.txt"))));
        Espresso.onView(withId(R.id.fileName_editText)).check(matches(withText("")));

        Espresso.onView(withId(R.id.fileCancel)).perform(click());
    }

    @Test
    public void retainTextOnRotateTest() throws InterruptedException {
        TextView textView = (TextView) getView(R.id.central_textView);

        Activity activity = (Activity) textView.getContext();

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Espresso.onView(withId(R.id.central_textView)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.central_textView)).check(matches(withText(R.string.opening_greeting)));

        String displayedText = textView.getText().toString();
        String noExaminationYet = activity.getResources().getString(R.string.opening_greeting);

        Assert.assertEquals(noExaminationYet, displayedText);

        Espresso.onView(withId(R.id.action_button)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.action_button)).perform(click());

        Espresso.onView(withId(R.id.central_textView)).check(matches(withText(R.string.intent_empty)));

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Espresso.onView(withId(R.id.central_textView)).check(matches(withText(R.string.intent_empty)));

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Espresso.onView(withId(R.id.central_textView)).check(matches(withText(R.string.intent_empty)));
    }

    private View getView(int id) {
        final View[] aview = {null};

        Espresso.onView(withId(id))
                .check(new ViewAssertion() {
                    @Override
                    public void check(View view, NoMatchingViewException noViewFoundException) {
                        aview[0] = view;
                    }
                })
                .check(new ViewAssertion() {
                    @Override
                    public void check(View view, NoMatchingViewException noViewFoundException) {
                        return;
                    }
                });

        return aview[0];
    }

    @Test
    public void settingsCancelButtonTest() {
        try {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        } catch (android.support.test.espresso.NoMatchingViewException ex) {
        }

        Espresso.onView(withText(getInstrumentation().getTargetContext().getString(R.string.action_settings)))
                .perform(click());

        Espresso.onView(withId(R.id.dialog_scrollView))
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.settings_cancel_button))
                .perform(click());

        Espresso.onView(withId(R.id.dialog_scrollView))
                .check(ViewAssertions.doesNotExist());

        try {
            Espresso.onView(withId(R.id.dialog_scrollView))
                    .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())));
            Assert.fail("This should not exist");
        } catch (android.support.test.espresso.NoMatchingViewException ex) {

        }
    }

    @Test
    public void settingsAcceptButtonTest() {
        openSettingsDialog();

        Espresso.onView(withId(R.id.settings_auto_examine_checkBox))
                .check(ViewAssertions.matches(isNotChecked()))
                .perform(click())
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.dialog_scrollView))
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.settings_accept_button))
                .perform(click());

        Espresso.onView(withId(R.id.dialog_scrollView))
                .check(ViewAssertions.doesNotExist());

        try {
            Espresso.onView(withId(R.id.dialog_scrollView))
                    .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())));
            Assert.fail("This should not exist");
        } catch (android.support.test.espresso.NoMatchingViewException ex) {
        }

        openSettingsDialog();

        Espresso.onView(withId(R.id.settings_auto_examine_checkBox))
                .check(ViewAssertions.matches(ViewMatchers.isChecked()))
                .perform(click())
                .check(matches(isNotChecked()));

        Espresso.onView(withId(R.id.dialog_scrollView))
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.settings_accept_button))
                .perform(click());

        Espresso.onView(withId(R.id.dialog_scrollView))
                .check(ViewAssertions.doesNotExist());

        try {
            Espresso.onView(withId(R.id.dialog_scrollView))
                    .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())));
            Assert.fail("This should not exist");
        } catch (android.support.test.espresso.NoMatchingViewException ex) {
        }

    }

    @Test
    public void settingsCancelButtonDoesNotSaveTest() {
        openSettingsDialog();

        Espresso.onView(withId(R.id.settings_auto_examine_checkBox))
                .check(ViewAssertions.matches(isNotChecked()))
                .perform(click())
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.dialog_scrollView))
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.settings_cancel_button))
                .perform(click());

        Espresso.onView(withId(R.id.dialog_scrollView))
                .check(ViewAssertions.doesNotExist());

        openSettingsDialog();

        Espresso.onView(withId(R.id.settings_auto_examine_checkBox))
                .check(matches(isNotChecked()));

        Espresso.onView(withId(R.id.dialog_scrollView))
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.settings_cancel_button))
                .perform(click());

        Espresso.onView(withId(R.id.dialog_scrollView))
                .check(ViewAssertions.doesNotExist());
    }

    @Test
    public void settingsResetButtonTest() {
        openSettingsDialog();

        Espresso.onView(withId(R.id.settings_auto_examine_checkBox))
                .check(ViewAssertions.matches(isNotChecked()))
                .perform(click())
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.dialog_scrollView))
                .perform(ViewActions.swipeUp())
                .perform(ViewActions.swipeUp())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.settings_reset_button))
                .perform(click());

        Espresso.onView(withId(R.id.settings_auto_examine_checkBox))
                .check(ViewAssertions.matches(isNotChecked()));

        Espresso.onView(withId(R.id.settings_cancel_button))
                .perform(click());

        Espresso.onView(withId(R.id.dialog_scrollView))
                .check(ViewAssertions.doesNotExist());
    }

    private void openSettingsDialog() {
        try {
            openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        } catch (android.support.test.espresso.NoMatchingViewException ex) {
        }

        Espresso.onView(withText(getInstrumentation().getTargetContext().getString(R.string.action_settings)))
                .perform(click());
    }
}