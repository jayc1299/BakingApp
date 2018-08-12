package com.nanodegree.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nanodegree.bakingapp.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

	@Rule
	public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Test
	public void testTitleVisible() {
		onView(withId(R.id.collapsing_toolbar)).check(matches(isDisplayed()));
	}

	@Test
	public void testRecyclerVisible() {
		onView(withId(R.id.main_recycler)).check(matches(isDisplayed()));
	}

	@Test
	public void testRecyclerContainsBrownies(){
		onView(withId(R.id.main_recycler)).check(matches(hasDescendant(withText("Brownies"))));
	}
}