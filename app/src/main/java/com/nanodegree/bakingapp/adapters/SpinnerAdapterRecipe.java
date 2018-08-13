package com.nanodegree.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.holders.Recipe;

import java.util.List;

public class SpinnerAdapterRecipe extends ArrayAdapter<Recipe> {

	private List<Recipe> recipes;

	public SpinnerAdapterRecipe(@NonNull Context context, int resource, List<Recipe> recipes) {
		super(context, resource);
		this.recipes = recipes;
	}

	public void updateRecipes(List<Recipe> recipes){
		this.recipes = recipes;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return recipes.size();
	}

	@Override
	public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		return getView(position, convertView, parent);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		View listItem = convertView;
		if(listItem == null) {
			listItem = LayoutInflater.from(getContext()).inflate(R.layout.item_recipe_small, parent, false);
		}

		Recipe recipe = recipes.get(position);

		TextView title = (TextView) listItem.findViewById(R.id.item_recipe_title);
		title.setText(recipe.getName());

		return listItem;
	}

	@Nullable
	@Override
	public Recipe getItem(int position) {
		return recipes.get(position);
	}
}