package com.nanodegree.bakingapp.db;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.nanodegree.bakingapp.Recipe;
import com.nanodegree.bakingapp.network.RetrofitClient;

public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {
	private Application application;
	private RetrofitClient retrofitClient;


	public RecipeViewModelFactory(Application application, RetrofitClient retrofitClient) {
		this.application = application;
		this.retrofitClient = retrofitClient;
	}


	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		//noinspection unchecked
		return (T) new RecipeViewModel(application, retrofitClient);
	}
}