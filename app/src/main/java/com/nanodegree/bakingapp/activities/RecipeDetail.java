package com.nanodegree.bakingapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.adapters.AdapterRecipeDetail;
import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.holders.Recipe;
import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.holders.RecipeComponent;
import com.nanodegree.bakingapp.holders.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetail extends AppCompatActivity {

	public static final String TAG = RecipeDetail.class.getSimpleName();
	public static final String RECIPE_ID = "recipeId";

	private RecipeViewModel viewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

		if(getIntent() != null && getIntent().hasExtra(RECIPE_ID)) {
			int recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
			Log.d(TAG, "onCreate: " + recipeId);
			getRecipeFromDb(recipeId);
		}
	}

	private void getRecipeFromDb(final int recipeId){
		viewModel.getRecipeById(recipeId).observe(this, new Observer<Recipe>() {
			@Override
			public void onChanged(@Nullable Recipe recipe) {
				setTitle(recipe.getName());
				//TODO: Also get steps
				getIngredientsFromDb(recipe.getId());
			}
		});
	}

	private void getIngredientsFromDb(final int recipeId){
		viewModel.getIngredientsByRecipeId(recipeId).observe(this, new Observer<List<Ingredient>>() {
			@Override
			public void onChanged(@Nullable List<Ingredient> ingredients) {
				setupRecipe(ingredients);
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

	private void setupRecipe(List<Ingredient> ingredients){
		//Create a combined list of steps and ingredients
		ArrayList<RecipeComponent> components = new ArrayList<>();

		if(ingredients != null) {
			components.addAll(ingredients);
		}

		Log.d(TAG, "setupRecipe: " + components.size());

		//TODO: Setup adapter & Recycler first, then add to adapter as data is retrieved (add in steps)
		RecyclerView recyclerView = findViewById(R.id.detail_recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		AdapterRecipeDetail adapter = new AdapterRecipeDetail(null, components);
		recyclerView.setAdapter(adapter);
	}
}