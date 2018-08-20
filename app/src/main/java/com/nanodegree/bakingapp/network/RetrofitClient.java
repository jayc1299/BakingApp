package com.nanodegree.bakingapp.network;

import android.util.Log;

import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.holders.Recipe;
import com.nanodegree.bakingapp.db.AppDatabase;
import com.nanodegree.bakingapp.holders.Step;
import com.nanodegree.bakingapp.widgets.IngredientsWidget;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

	private static final String TAG = RetrofitClient.class.getSimpleName();
	private RecipeService service;

	public RetrofitClient() {
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient client = new OkHttpClient.Builder()
				.addInterceptor(interceptor)
				.build();

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://d17h27t6h515a5.cloudfront.net/")
				.addConverterFactory(GsonConverterFactory.create())
				.client(client)
				.build();

		service = retrofit.create(RecipeService.class);
	}

	public void getRecipes(final AppDatabase database){
		Call<List<Recipe>> repos = service.getRecipes();

		repos.enqueue(new Callback<List<Recipe>>() {
			@Override
			public void onResponse(Call<List<Recipe>> call, final Response<List<Recipe>> response) {
				if (response.isSuccessful() && response.body() != null) {
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							database.recipesDao().deleteAllRecipes();
							database.stepsDao().deleteAllSteps();
							database.ingredientsDao().deleteAllIngredients();

							for (Recipe recipe : response.body()) {
								database.recipesDao().insertRecipe(recipe);

								//Now insert ingredients
								for (Ingredient ingredient : recipe.getIngreditents()) {
									ingredient.setRecipeId(recipe.getId());
									database.ingredientsDao().insertIngredient(ingredient);
								}

								//Now insert steps
								for (Step step : recipe.getSteps()) {
									step.setRecipeId(recipe.getId());
									database.stepsDao().insertStep(step);
								}
							}
						}});
					thread.start();
				}
			}

			@Override
			public void onFailure(Call<List<Recipe>> call, Throwable t) {
				Log.e(TAG, "onFailure: ", t);
			}
		});
	}
}