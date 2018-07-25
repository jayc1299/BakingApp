package com.nanodegree.bakingapp.holders;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.nanodegree.bakingapp.R;

import java.text.DecimalFormat;

@Entity
public class Ingredient extends RecipeComponent{

	@SerializedName("id")
	@PrimaryKey(autoGenerate = true)
	private int id;
	@SerializedName("recipeId")
	private int recipeId;
	@SerializedName("quantity")
	private float quantity;
	@SerializedName("measure")
	private String measure;
	@SerializedName("ingredient")
	private String ingredient;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

	@Override
	public String getDisplayName(Context context) {
		return context.getString(R.string.ingredients_display, formatFloatIfNeeded(quantity), measure.toLowerCase(), ingredient);
	}

	public String formatFloatIfNeeded(float quantity) {
		return new DecimalFormat("#.##").format(quantity);
	}
}