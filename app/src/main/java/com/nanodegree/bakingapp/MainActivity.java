package com.nanodegree.bakingapp;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

		ArrayList<String> recipes = new ArrayList<>();
		recipes.add("Cookies");
		recipes.add("Cheesecake");
		recipes.add("Chicken Pie");
		recipes.add("Chocolate Cake");

		AdapterRecipe adapter = new AdapterRecipe(recipes);
		recyclerView.setAdapter(adapter);
	}
}
