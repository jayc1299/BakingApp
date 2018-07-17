package com.nanodegree.bakingapp;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CollapsingToolbarLayout collapsingToolbarLayout =  findViewById(R.id.collapsing_toolbar);
		collapsingToolbarLayout.setTitle(getString(R.string.app_name));
		collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

		RecyclerView recyclerView = findViewById(R.id.main_recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		ArrayList<Recipe> recipes = new ArrayList<>();
		recipes.add(new Recipe(1, "Cookies"));
		recipes.add(new Recipe(2, "Cheesecake"));
		recipes.add(new Recipe(3, "Chicken Pie"));
		recipes.add(new Recipe(4, "Chocolate Cake"));

		AdapterRecipe adapter = new AdapterRecipe(listener, recipes);
		recyclerView.setAdapter(adapter);
	}

	AdapterRecipe.IRecipeClickListener listener = new AdapterRecipe.IRecipeClickListener() {
		@Override
		public void onRecipeClicked(Recipe recipe) {
			Intent intent = new Intent(MainActivity.this, RecipeDetail.class);
			startActivity(intent);
		}
	};
}