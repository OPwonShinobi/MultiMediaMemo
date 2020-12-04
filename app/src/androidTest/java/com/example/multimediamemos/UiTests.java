package com.example.multimediamemos;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UiTests {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public IntentsTestRule<MainActivity> intentsRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void testCreate() {
        onView(withId(R.id.empty_list_msg)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.videoBox)).check(ViewAssertions.matches((isDisplayed())));
        onView(withId(R.id.empty_list_msg)).check(ViewAssertions.matches(not(isDisplayed())));
    }
    @Test
    public void testSearch() {
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.searchField)).perform(typeText("caption"), closeSoftKeyboard());
        onView(withId(R.id.searchBtn)).perform(click());
        onView(withId(R.id.action_search)).check(ViewAssertions.matches(isDisplayed()));
    }
    @Test
    public void testDelete() throws InterruptedException {
        onView(withId(R.id.action_add)).perform(click());
        onView(withId(R.id.videoBox)).check(ViewAssertions.matches((isDisplayed())));
        onView(withId(R.id.empty_list_msg)).check(ViewAssertions.matches(not(isDisplayed())));
        onView(withId(R.id.deleteBtn)).perform(click());
        onView(withId(R.id.empty_list_msg)).check(ViewAssertions.matches(isDisplayed()));
    }

}