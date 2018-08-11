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

	public static final String TAG = RecipeDetailActivity.class.getSimpleName();
	public static final String RECIPE_ID = "recipeId";

	private RecipeViewModel viewModel;
	private int recipeId;
	private FragmentStep fragDetails;

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
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putInt(RECIPE_ID, recipeId);

		//Are we a two pain mode for tablets?
		if (findViewById(R.id.activity_detail_list) != null) {
			//List
			FragmentStepList fragList = new FragmentStepList();
			fragList.setStepClickedListener(stepListClickedListener);
			fragList.setArguments(bundle);
			//Details
			fragDetails = new FragmentStep();
			fragDetails.setArguments(bundle);
			fragmentTransaction.replace(R.id.activity_detail_list, fragList, fragList.getClass().getSimpleName());
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
			if (fragDetails != null) {
				fragDetails.showDetails(ingredients, recipeId, stepId);
			}
		}
	};
}