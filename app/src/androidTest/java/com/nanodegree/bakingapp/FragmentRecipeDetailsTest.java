package com.nanodegree.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nanodegree.bakingapp.activities.RecipeDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class FragmentRecipeDetailsTest {

	@Rule
	public ActivityTestRule<RecipeDetailActivity> mActivityRule = new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class){
		@Override
		protected Intent getActivityIntent() {
			Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
			Intent result = new Intent(targetContext, RecipeDetailActivity.class);
			result.putExtra(RecipeDetailActivity.RECIPE_ID, 1);
			return result;
		}
	};

	@Before
	public void init(){
		mActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
	}

	@Test
	public void testIngredientListVisible() {
		onView(withId(R.id.frag_details_ingredients_list)).check(matches(isDisplayed()));
	}

	@Test
	public void testStepsListVisible() {
		onView(withId(R.id.frag_details_steps_container)).check(matches(isDisplayed()));
	}

	@Test
	public void testIngredientHeaderText() {
		onView(withId(R.id.frag_details_ingredient_header)).check(matches(isDisplayed()));
		onView(withId(R.id.frag_details_ingredient_header)).check(matches(withText("Ingredients")));
	}

	@Test
	public void testStepsHeaderText() {
		onView(withId(R.id.frag_details_steps_header)).check(matches(isDisplayed()));
		onView(withId(R.id.frag_details_steps_header)).check(matches(withText("Steps")));
	}
}