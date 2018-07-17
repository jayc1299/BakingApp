package com.nanodegree.bakingapp.network;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.nanodegree.bakingapp.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

	private static final String TAG = RetrofitClient.class.getSimpleName();
	private RecipeService service;

	public RetrofitClient() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://d17h27t6h515a5.cloudfront.net/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		service = retrofit.create(RecipeService.class);
	}

	public void getRecipes(final MutableLiveData<List<Recipe>> recipes){
		Call<List<Recipe>> repos = service.getRecipes();

		repos.enqueue(new Callback<List<Recipe>>() {
			@Override
			public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
				if (response.isSuccessful()) {
					recipes.postValue(response.body());
				}
			}

			@Override
			public void onFailure(Call<List<Recipe>> call, Throwable t) {
				Log.e(TAG, "onFailure: ", t);
			}
		});
	}
}