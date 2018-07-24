package com.nanodegree.bakingapp.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.holders.Recipe;
import com.nanodegree.bakingapp.holders.Step;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

	private static final String TAG = RecipeViewModel.class.getSimpleName();
	private LiveData<List<Recipe>> recipes;
	private LiveData<List<Ingredient>> ingredients;
	private AppDatabase database;

	public RecipeViewModel(@NonNull Application application) {
		super(application);
		database = AppDatabase.getInstance(this.getApplication());

		if (recipes == null) {
			recipes = database.recipesDao().getAllRecipes();
		}
	}

	public LiveData<List<Recipe>> getRecipes() {
		return recipes;
	}

	public LiveData<Recipe> getRecipeById(int id) {
		return database.recipesDao().getRecipeById(id);
	}

	public LiveData<List<Ingredient>> getIngredientsByRecipeId(int id) {
		return database.ingredientsDao().getIngredientsByRecipeId(id);
	}

	public LiveData<List<Step>> getStepsByRecipeId(int id) {
		return database.stepsDao().getStepsByRecipeId(id);
	}
}