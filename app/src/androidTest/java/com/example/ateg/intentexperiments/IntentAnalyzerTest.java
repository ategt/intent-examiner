package com.example.ateg.intentexperiments;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.PendingIntent;
import android.content.ComponentName;
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
import java.util.Iterator;
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


        Set<String> categories = intent.getCategories();
        if (categories != null) {
            sb.append("Categories: ");
            Iterator<String> categoriesIterator = categories.iterator();
            while (categoriesIterator.hasNext()) {
                sb.append(categoriesIterator.next());
                if (categoriesIterator.hasNext())
                    sb.append(", ");
            }
            sb.append(System.lineSeparator());
        }

        String type = intent.getType();
        if (type != null) {
            sb.append("Type: ");
            sb.append(type);
            sb.append(System.lineSeparator());
        }

        String dataString = intent.getDataString();
        if (dataString != null) {
            sb.append("Data: ");
            sb.append(dataString);
            sb.append(System.lineSeparator());
        }

        ComponentName componentName = intent.getComponent();

        if (componentName != null) {
            String componentString = componentName.getPackageName();
            if (componentString != null) {
                sb.append("Component: ");
                sb.append(componentString);
                sb.append(System.lineSeparator());
            }
        }

        String intentPackage = intent.getPackage();
        if (intentPackage != null) {
            sb.append("Package: ");
            sb.append(intentPackage);
            sb.append(System.lineSeparator());
        }

        String scheme = intent.getScheme();
        if (scheme != null) {
            sb.append("Scheme: ");
            sb.append(scheme);
            sb.append(System.lineSeparator());
        }


        sb.append("Intent Bundle Size: ");
        sb.append(bundle.size());
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());

        for (String key : keysSet) {
            sb.append(key);
            sb.append(System.lineSeparator());
            Object object = bundle.get(key);

            sb.append("\t");
            if (object == null)
                sb.append("-Null-");
            else {
                String canClassName = object.getClass().getCanonicalName();
                String classString = object.getClass().toString();
                sb.append(canClassName);
                sb.append("\t");
                sb.append(classString);

                sb.append(System.lineSeparator());

                sb.append("\t\t");
                sb.append(object.toString());
            }
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
        }

        TextView textView = (TextView) mainActivity.findViewById(R.id.central_textView);
        String textViewText = textView.getText().toString();

        Assert.assertTrue(textViewText.contains(randomString));

        Assert.assertEquals(textViewText, sb.toString());
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

    @Test
    public void testNullItemInBundle() {
        Intent intent = new Intent();
        String nullString = null;
        intent.putExtra("com.example.ateg.intentexperiments.NULL_STRING", nullString);

        MainActivity mainActivity = mainActivityActivityTestRule.launchActivity(intent);

        Espresso.onView(withId(R.id.action_button)).perform(click());

        Espresso.onView(withId(R.id.central_textView)).check(matches(not(withText(R.string.intent_empty))));

        TextView textView = (TextView) mainActivity.findViewById(R.id.central_textView);
        String textViewText = textView.getText().toString();

        Assert.assertTrue(textViewText.contains("com.example.ateg.intentexperiments.NULL_STRING"));
    }
}
