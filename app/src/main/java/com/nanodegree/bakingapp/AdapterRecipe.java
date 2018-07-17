package com.nanodegree.bakingapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterRecipe extends RecyclerView.Adapter<AdapterRecipe.RecipeViewHolder>{

	public interface IRecipeClickListener{
		void onRecipeClicked(Recipe recipe);
	}

	private ArrayList<Recipe> recipes;
	private IRecipeClickListener listener;

	public AdapterRecipe(IRecipeClickListener listener, ArrayList<Recipe> recipes) {
		this.listener = listener;
		this.recipes = recipes;
	}

	@NonNull
	@Override
	public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new RecipeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
		final Recipe recipe = recipes.get(position);
		holder.title.setText(recipe.getName());
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.onRecipeClicked(recipe);
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return recipes.size();
	}

	class RecipeViewHolder extends RecyclerView.ViewHolder {
		TextView title;

		public RecipeViewHolder(View itemView) {
			super(itemView);
			title = itemView.findViewById(R.id.item_recipe_title);
		}
	}
}