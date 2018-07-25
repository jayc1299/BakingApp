package com.nanodegree.bakingapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.holders.Recipe;
import com.nanodegree.bakingapp.holders.Step;
import com.nanodegree.bakingapp.utils.UiUtils;

import java.util.List;

public class RecipeDetail extends AppCompatActivity {

	public static final String TAG = RecipeDetail.class.getSimpleName();
	public static final String RECIPE_ID = "recipeId";

	private RecipeViewModel viewModel;
	private LinearLayout ingredientList;
	private LinearLayout stepsList;
	private UiUtils uiUtils;
	private int recipeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
		uiUtils = new UiUtils();

		//I tried this with a RecyclerView but the look just wasn't what I was looking for when analysing the designs.
		ingredientList = findViewById(R.id.ingredients_list);
		stepsList = findViewById(R.id.details_steps_container);

		//Get recipe ID from intent
		if (getIntent() != null && getIntent().hasExtra(RECIPE_ID)) {
			recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
			Log.d(TAG, "RecipeId: " + recipeId);
			getRecipeFromDb(recipeId);
		}
	}

	/**
	 * Get the recipe from the DB.
	 * Then get the ingredients and the steps from their respective tables.
	 *
	 * @param recipeId recipe ID
	 */
	private void getRecipeFromDb(final int recipeId) {
		viewModel.getRecipeById(recipeId).observe(this, new Observer<Recipe>() {
			@Override
			public void onChanged(@Nullable Recipe recipe) {
				setTitle(recipe.getName());

				//get all ingredients for recipeid
				viewModel.getIngredientsByRecipeId(recipeId).observe(RecipeDetail.this, new Observer<List<Ingredient>>() {
					@Override
					public void onChanged(@Nullable List<Ingredient> ingredients) {
						addIngredients(ingredients);
					}
				});

				//Get all steps for receipeId
				viewModel.getStepsByRecipeId(recipeId).observe(RecipeDetail.this, new Observer<List<Step>>() {
					@Override
					public void onChanged(@Nullable List<Step> steps) {
						if (steps != null && steps.size() > 0) {
							//Deliberately removing step 1 so we skip the introduction title.
							steps.remove(0);
							addSteps(steps);
						}
					}
				});
			}
		});
	}

	/**
	 * Loop through ingredients and create a view for each ingredient and add it to the linearLayuotu
	 *
	 * @param ingredients List of ingredients
	 */
	private void addIngredients(List<Ingredient> ingredients) {
		for (Ingredient ingredient : ingredients) {
			View ingredientView = getLayoutInflater().inflate(R.layout.item_ingredient, stepsList, false);
			String ingredientDisplayName = getString(R.string.ingredients_display,
					uiUtils.formatFloatIfNeeded(ingredient.getQuantity()),
					ingredient.getMeasure().toLowerCase(), ingredient.getIngredient());
			((TextView) ingredientView.findViewById(R.id.item_ing_title)).setText(ingredientDisplayName);
			ingredientList.addView(ingredientView);
		}
	}

	/**
	 * Loop through all steps and add to linear layout
	 *
	 * @param steps list of steps
	 */
	private void addSteps(List<Step> steps) {
		for (Step step : steps) {
			View stepView = getLayoutInflater().inflate(R.layout.item_step, stepsList, false);
			stepView.setTag(step);
			stepView.setOnClickListener(stepClickListener);
			((TextView) stepView.findViewById(R.id.item_step_title)).setText(step.getShortDescription());
			stepsList.addView(stepView);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home: {
				this.finish();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void setTitle(String title) {
		ActionBar actionBar = RecipeDetail.this.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title);
		}
	}

	private View.OnClickListener stepClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Step step = (Step) view.getTag();
			Intent intent = new Intent(RecipeDetail.this, RecipeStepActivity.class);
			intent.putExtra(RecipeStepActivity.STEP_ID, step.getId());
			intent.putExtra(RecipeStepActivity.RECIPE_ID, recipeId);
			startActivity(intent);
		}
	};
}