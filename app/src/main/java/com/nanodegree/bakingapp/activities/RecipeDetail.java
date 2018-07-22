package com.nanodegree.bakingapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.Recipe;
import com.nanodegree.bakingapp.db.RecipeViewModel;

public class RecipeDetail extends AppCompatActivity {

	public static final String TAG = RecipeDetail.class.getSimpleName();
	public static final String RECIPE_ID = "recipeId";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		if(getIntent() != null && getIntent().hasExtra(RECIPE_ID)) {
			int recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
			Log.d(TAG, "onCreate: " + recipeId);
			getRecipeFromDb(recipeId);
		}
	}

	private void getRecipeFromDb(final int recipeId){
		RecipeViewModel viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
		viewModel.getRecipeById(recipeId).observe(this, new Observer<Recipe>() {
			@Override
			public void onChanged(@Nullable Recipe recipe) {
				setTitle(recipe.getName());
				setupRecipe(recipe);
			}
		});
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

	private void setTitle(String title){
		ActionBar actionBar = RecipeDetail.this.getSupportActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title);
		}
	}

	private void setupRecipe(Recipe recipe){

	}
}