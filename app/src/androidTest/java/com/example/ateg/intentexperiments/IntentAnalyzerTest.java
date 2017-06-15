package com.example.ateg.intentexperiments;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.Espresso;
import android.widget.Button;
import android.widget.TextView;

import junit.framework.Assert;

import java.io.File;
import java.math.BigInteger;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;

import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.CharMatcher.is;
import static android.support.test.espresso.core.deps.guava.base.Predicates.instanceOf;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by ATeg on 6/14/2017.
 */
@RunWith(AndroidJUnit4.class)
public class IntentAnalyzerTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void examineEmptyIntentTest() {
        Espresso.onView(withId(R.id.action_button)).perform(click());

        Espresso.onView(withId(R.id.central_textView)).check(matches(withText(R.string.intent_empty)));

    }

    @Test
    public void examineTextTest() {
        String randomString = UUID.randomUUID().toString();

        Intent intent = new Intent();
        intent.putExtra("com.example.ateg.intentexperiments.RANDOM_STRING", randomString);

        MainActivity mainActivity = mainActivityActivityTestRule.launchActivity(intent);

        Espresso.onView(withId(R.id.action_button)).perform(click());

        Espresso.onView(withId(R.id.central_textView)).check(matches(not(withText(R.string.intent_empty))));

        StringBuilder sb = new StringBuilder();
        Bundle bundle = intent.getExtras();
        Set<String> keysSet = bundle.keySet();

        String action = intent.getAction();
        int flags = intent.getFlags();

        sb.append("Action: ");
        sb.append(action);
        sb.append(System.lineSeparator());

        sb.append("Flags: ");
        sb.append(flags);
        sb.append(System.lineSeparator());

        sb.append("Intent Bundle Size: ");
        sb.append(bundle.size());
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());

        for (String key : keysSet) {
            sb.append(key);
            sb.append(System.lineSeparator());
            Object object = bundle.get(key);

            sb.append("\t");
            String canClassName = object.getClass().getCanonicalName();
            String classString = object.getClass().toString();
            sb.append(canClassName);
            sb.append("\t");
            sb.append(classString);
            sb.append(System.lineSeparator());

            sb.append("\t\t");
            sb.append(object.toString());
        }

        TextView textView = (TextView) mainActivity.findViewById(R.id.central_textView);
        String textViewText = textView.getText().toString();

        Assert.assertTrue(textViewText.contains(randomString));

        Assert.assertEquals(textViewText, sb.toString());
    }

//    public void comments() {


//    Context context = InstrumentationRegistry.getTargetContext();
//    ActivityTestRule<MainActivity> mainActivityActivityTestRule
//            = new ActivityTestRule<MainActivity>(MainActivity.class);
//
//    //PendingIntent.getActivity();
//        ContextCompat.startActivity(context, new Intent(context, MainActivity.class), null);
//
//
////
//        String typedFileName = (new Random().nextInt()) + ".txt";
//
//        Espresso.onView(
//
//                withId(R.id.menu_item_save_favorites)).
//
//                perform(click());
//
//        Espresso.onView(
//
//                withId(R.id.fileName)).
//
//                perform(clearText());
//        Espresso.onView(
//
//                withId(R.id.fileName)).
//
//                perform(typeText(typedFileName));
//
//        Espresso.closeSoftKeyboard();
//
//        Espresso.onView(
//
//                withId(R.id.fileSaveLoad)).
//
//                check(matches(withText("Save")));
//        Espresso.onView(
//
//                withId(R.id.fileSaveLoad)).
//
//                check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())));
//
//        Espresso.onView(
//
//                withId(R.id.fileSaveLoad)).
//
//                perform(ViewActions.scrollTo());
//
//        Espresso.onView(
//
//                withId(R.id.fileSaveLoad)).
//
//                check(matches(ViewMatchers.isDisplayed()));
//        Espresso.onView(
//
//                withId(R.id.fileSaveLoad)).
//
//                check(ViewAssertions.matches(ViewMatchers.isClickable()));
//
//
//        long beforeClick = System.currentTimeMillis();
//
//        long temp = Math.round(Math.pow(10.0, 4));
//        beforeClick /= temp;
//
//        beforeClick *= temp;
//
//        Espresso.onView(
//
//                withId(R.id.fileSaveLoad)).
//
//                perform(click());
//
//        File[] files = ContextCompat.getExternalFilesDirs(
//                photoFavoritesGalleryActivityActivityTestRule.getActivity(),
//                Environment.DIRECTORY_DOCUMENTS);
//
//        File storageDir = files[files.length - 1];
//
//        File generatedFile = new File(storageDir, typedFileName);
//
//        Assert.assertTrue(generatedFile.exists());
//
//        Assert.assertTrue(generatedFile.length() > 0);
//
//        Assert.assertTrue(beforeClick <= generatedFile.lastModified());
//
//        Assert.assertTrue(System.currentTimeMillis() >= generatedFile.lastModified());
//
//        photoFavoritesGalleryActivityActivityTestRule.getActivity().
//
//                finish();
//
//        generatedFile.delete();
//    }
}
