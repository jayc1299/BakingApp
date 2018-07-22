package com.nanodegree.bakingapp.network;

import com.nanodegree.bakingapp.holders.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {

	@GET("topher/2017/May/59121517_baking/baking.json")
	Call<List<Recipe>> getRecipes();
}