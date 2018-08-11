package com.nanodegree.bakingapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.fragments.FragmentRecipeDetails;
import com.nanodegree.bakingapp.fragments.FragmentStep;
import com.nanodegree.bakingapp.fragments.FragmentStepList;
import com.nanodegree.bakingapp.holders.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {

	public static final String RECIPE_ID = "recipeId";
	private static final String TAG = RecipeDetailActivity.class.getSimpleName();
	private static final String STATE_RECIPIE_ID = "state_recipe_id";
	private static final String STATE_TITLE = "state_title";

	private RecipeViewModel viewModel;
	private int recipeId;
	private String recipeTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

		//Get recipe ID from intent
		if (savedInstanceState == null && getIntent() != null && getIntent().hasExtra(RECIPE_ID)) {
			recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
			Log.d(TAG, "RecipeId: " + recipeId);
			getRecipeFromDb(recipeId);
		}

		//Need to re-attatch the listener on rotate
		if(savedInstanceState != null){
			FragmentStepList fragStepList = (FragmentStepList) getSupportFragmentManager().findFragmentByTag(FragmentStepList.class.getSimpleName());
			fragStepList.setStepClickedListener(stepListClickedListener);
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_RECIPIE_ID, recipeId);
		outState.putString(STATE_TITLE, recipeTitle);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState != null) {
			recipeId = savedInstanceState.getInt(STATE_RECIPIE_ID);
			setTitle(savedInstanceState.getString(STATE_TITLE));
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void setTitle(String title) {
		recipeTitle = title;
		ActionBar actionBar = RecipeDetailActivity.this.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title);
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
				loadFragments(recipeId);
			}
		});
	}

	private void loadFragments(int recipeId){
		Log.d(TAG, "loadFragments: ");
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putInt(RECIPE_ID, recipeId);

		//Are we a two pain mode for tablets?
		if (findViewById(R.id.activity_detail_list) != null) {
			//List
			FragmentStepList fragStepList = new FragmentStepList();
			fragStepList.setStepClickedListener(stepListClickedListener);
			fragStepList.setArguments(bundle);
			//Details
			FragmentStep fragDetails = new FragmentStep();
			fragDetails.setArguments(bundle);
			fragmentTransaction.replace(R.id.activity_detail_list, fragStepList, fragStepList.getClass().getSimpleName());
			fragmentTransaction.replace(R.id.activity_detail_contents, fragDetails, fragDetails.getClass().getSimpleName());
		}else{
			//Single pain mode.
			FragmentRecipeDetails frag = new FragmentRecipeDetails();
			frag.setArguments(bundle);
			fragmentTransaction.replace(R.id.activity_detail_full_container, frag, frag.getClass().getSimpleName());
		}
		fragmentTransaction.commit();
	}

	private FragmentStepList.IStepListClicked stepListClickedListener = new FragmentStepList.IStepListClicked() {
		@Override
		public void onStepClicked(boolean ingredients, int stepId) {
			Log.d(TAG, "onStepClicked: " + stepId);
			FragmentStep fragmentStep = (FragmentStep) getSupportFragmentManager().findFragmentByTag(FragmentStep.class.getSimpleName());
			if (fragmentStep != null) {
				fragmentStep.showDetails(ingredients, recipeId, stepId);
			}else{
				Log.d(TAG, "onStepClicked: Not found");
			}
		}
	};
}