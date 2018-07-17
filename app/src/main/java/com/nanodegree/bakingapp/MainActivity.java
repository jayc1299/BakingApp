package com.nanodegree.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.db.RecipeViewModelFactory;
import com.nanodegree.bakingapp.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CollapsingToolbarLayout collapsingToolbarLayout =  findViewById(R.id.collapsing_toolbar);
		collapsingToolbarLayout.setTitle(getString(R.string.app_name));
		collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

		recyclerView = findViewById(R.id.main_recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		RetrofitClient retrofitClient = new RetrofitClient();
		RecipeViewModelFactory factory = new RecipeViewModelFactory(getApplication(), retrofitClient);
		RecipeViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
		viewModel.getMovies().observe(this, new Observer<List<Recipe>>() {
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
			startActivity(intent);
		}
	};
}