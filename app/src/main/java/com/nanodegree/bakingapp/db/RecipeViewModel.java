package com.nanodegree.bakingapp.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.nanodegree.bakingapp.Recipe;
import com.nanodegree.bakingapp.network.RetrofitClient;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

	private static final String TAG = RecipeViewModel.class.getSimpleName();
	private MutableLiveData<List<Recipe>> recipes;

	public RecipeViewModel(@NonNull Application application, RetrofitClient retrofitClient) {
		super(application);

		if (recipes == null) {
			recipes = new MutableLiveData<>();
			retrofitClient.getRecipes(recipes);
		}
	}

	public LiveData<List<Recipe>> getMovies() {
		return recipes;
	}
}