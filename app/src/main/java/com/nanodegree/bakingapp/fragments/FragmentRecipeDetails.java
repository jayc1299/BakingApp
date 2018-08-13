package com.nanodegree.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.activities.RecipeStepActivity;
import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.holders.Step;
import com.nanodegree.bakingapp.utils.UiUtils;

import java.util.List;

import static com.nanodegree.bakingapp.activities.RecipeDetailActivity.RECIPE_ID;

public class FragmentRecipeDetails extends Fragment{

	private static final String TAG = FragmentRecipeDetails.class.getSimpleName();

	private RecipeViewModel viewModel;
	private LinearLayout ingredientList;
	private LinearLayout stepsList;
	private UiUtils uiUtils;
	private int recipeId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
		uiUtils = new UiUtils();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

		//I tried this with a RecyclerView but the look just wasn't what I was looking for when analysing the designs.
		ingredientList = view.findViewById(R.id.frag_details_ingredients_list);
		stepsList = view.findViewById(R.id.frag_details_steps_container);

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//Get recipe ID from intent
		Bundle args = getArguments();
		if (args != null && args.containsKey(RECIPE_ID)) {
			recipeId = args.getInt(RECIPE_ID, 0);
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
		if (getActivity() != null && getView() != null) {

			//get all ingredients for recipeid
			viewModel.getIngredientsByRecipeId(recipeId).observe(getActivity(), new Observer<List<Ingredient>>() {
				@Override
				public void onChanged(@Nullable List<Ingredient> ingredients) {
					addIngredients(ingredients);
				}
			});

			//Get all steps for receipeId
			viewModel.getStepsByRecipeId(recipeId).observe(getActivity(), new Observer<List<Step>>() {
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
	}

	/**
	 * Loop through ingredients and create a view for each ingredient and add it to the linearLayuotu
	 *
	 * @param ingredients List of ingredients
	 */
	private void addIngredients(List<Ingredient> ingredients) {
		for (Ingredient ingredient : ingredients) {
			View ingredientView = getLayoutInflater().inflate(R.layout.item_ingredient, stepsList, false);
			String ingredientDisplayName = uiUtils.buildSingleIngredientString(getActivity(), ingredient);
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

	private View.OnClickListener stepClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			Step step = (Step) view.getTag();
			Intent intent = new Intent(getActivity(), RecipeStepActivity.class);
			intent.putExtra(RecipeStepActivity.STEP_ID, step.getId());
			intent.putExtra(RecipeStepActivity.RECIPE_ID, recipeId);
			intent.putExtra(RecipeStepActivity.STEP_NAME, step.getShortDescription());
			startActivity(intent);
		}
	};
}