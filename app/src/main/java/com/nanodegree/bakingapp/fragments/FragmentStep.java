package com.nanodegree.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.holders.Step;
import com.nanodegree.bakingapp.utils.UiUtils;

import java.util.List;

public class FragmentStep extends Fragment{

	private static final String TAG = FragmentStep.class.getSimpleName();

	private RecipeViewModel viewModel;
	private UiUtils uiUtils;

	private TextView description;
	private LinearLayout movieView;
	private LinearLayout ingredientList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
		uiUtils = new UiUtils();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_step, container, false);

		description = view.findViewById(R.id.fragment_step_details);
		movieView = view.findViewById(R.id.fragment_step_movie);
		ingredientList = view.findViewById(R.id.fragment_step_ingredients);

		return view;
	}

	public void showDetails(boolean showIngredients, int recipeId, int stepId){

		if (showIngredients) {
			//Show ingredient list
			viewModel.getIngredientsByRecipeId(recipeId).observe(this, new Observer<List<Ingredient>>() {
				@Override
				public void onChanged(@Nullable List<Ingredient> ingredients) {
					showIngredients(ingredients);
				}
			});
		} else {
			//show the step details
			viewModel.getStepByStepIdAndRecipeId(stepId, recipeId).observe(this, new Observer<Step>() {
				@Override
				public void onChanged(@Nullable Step step) {
					if (step != null) {
						description.setText(step.getDescription());
						description.setVisibility(View.VISIBLE);
						movieView.setVisibility(View.VISIBLE);
						ingredientList.setVisibility(View.GONE);
					}
				}
			});
		}
	}

	private void showIngredients(List<Ingredient> ingredients) {
		description.setVisibility(View.GONE);
		movieView.setVisibility(View.GONE);
		ingredientList.removeAllViews();
		ingredientList.setVisibility(View.VISIBLE);

		for (Ingredient ingredient : ingredients) {
			View ingredientView = getLayoutInflater().inflate(R.layout.item_ingredient, ingredientList, false);
			String ingredientDisplayName = getString(R.string.ingredients_display,
					uiUtils.formatFloatIfNeeded(ingredient.getQuantity()),
					ingredient.getMeasure().toLowerCase(), ingredient.getIngredient());
			((TextView) ingredientView.findViewById(R.id.item_ing_title)).setText(ingredientDisplayName);
			ingredientList.addView(ingredientView);
		}
	}
}