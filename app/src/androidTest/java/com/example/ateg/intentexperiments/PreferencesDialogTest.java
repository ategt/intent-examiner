package com.example.ateg.intentexperiments;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by ATeg on 10/31/2017.
 */

@RunWith(AndroidJUnit4.class)
public class PreferencesDialogTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);


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

    @Test
    public void settingsSaveAndResetCorrectlyTest() {
        openSettingsDialog();

        Espresso.onView(withId(R.id.dialog_scrollView))
                .perform(ViewActions.swipeUp())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.settings_reset_button))
                .check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.settings_reset_button))
                .perform(click());

        Espresso.onView(withId(R.id.dialog_scrollView))
                .perform(ViewActions.swipeDown())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.settings_auto_examine_checkBox))
                .check(ViewAssertions.matches(isNotChecked()))
                .perform(click())
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.settings_auto_save_checkBox))
                .check(ViewAssertions.matches(isNotChecked()))
                .perform(click())
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.settings_auto_log_checkBox))
                .check(ViewAssertions.matches(isNotChecked()))
                .perform(click())
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.settings_click_anywhere_examines_checkBox))
                .check(ViewAssertions.matches(isNotChecked()))
                .perform(click())
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.settings_show_examine_button_checkBox))
                .check(ViewAssertions.matches(isChecked()))
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

        openSettingsDialog();

        Espresso.onView(withId(R.id.settings_auto_examine_checkBox))
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.settings_auto_save_checkBox))
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.settings_auto_log_checkBox))
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.settings_click_anywhere_examines_checkBox))
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.settings_show_examine_button_checkBox))
                .check(matches(isNotChecked()));

        Espresso.onView(withId(R.id.dialog_scrollView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.dialog_scrollView))
                .perform(ViewActions.swipeUp())
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.settings_reset_button))
                .check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.settings_reset_button))
                .perform(click());

        Espresso.onView(withId(R.id.settings_auto_examine_checkBox))
                .check(matches(not(isChecked())));

        Espresso.onView(withId(R.id.settings_auto_save_checkBox))
                .check(matches(not(isChecked())));

        Espresso.onView(withId(R.id.settings_auto_log_checkBox))
                .check(matches(not(isChecked())));

        Espresso.onView(withId(R.id.settings_click_anywhere_examines_checkBox))
                .check(matches(not(isChecked())));

        Espresso.onView(withId(R.id.settings_show_examine_button_checkBox))
                .check(matches(isChecked()));

        Espresso.onView(withId(R.id.settings_accept_button))
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