package com.nanodegree.bakingapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nanodegree.bakingapp.adapters.AdapterRecipe;
import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.holders.Recipe;
import com.nanodegree.bakingapp.db.AppDatabase;
import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.network.RetrofitClient;

import java.util.List;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		recyclerView = findViewById(R.id.main_recycler);
		if(!getResources().getBoolean(R.bool.is_tablet)) {
			CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
			collapsingToolbarLayout.setTitle(getString(R.string.app_name));
			collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
			recyclerView.setLayoutManager(new LinearLayoutManager(this));
		}else{
			Toolbar toolbar = findViewById(R.id.toolbar);
			toolbar.setTitle(getString(R.string.app_name));
			recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
		}


		RetrofitClient retrofitClient = new RetrofitClient();
		retrofitClient.getRecipes(AppDatabase.getInstance(this.getApplication()));

		RecipeViewModel viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
		viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
			@Override
			public void onChanged(@Nullable List<Recipe> recipes) {
				Log.d(TAG, "onChanged: " + recipes.size());
				AdapterRecipe adapter = new AdapterRecipe(listener, recipes);
				recyclerView.setAdapter(adapter);
			}
		});
	}

	AdapterRecipe.IRecipeClickListener listener = new AdapterRecipe.IRecipeClickListener() {
		@Override
		public void onRecipeClicked(Recipe recipe) {
			Intent intent = new Intent(MainActivity.this, RecipeDetail.class);
			intent.putExtra(RecipeDetail.RECIPE_ID, recipe.getId());
			startActivity(intent);
		}
	};
}