package com.nanodegree.bakingapp.utils;

import android.content.Context;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.holders.Ingredient;

import java.text.DecimalFormat;
import java.util.List;

public class UiUtils {

	public String formatFloatIfNeeded(float quantity) {
		return new DecimalFormat("#.##").format(quantity);
	}

	public String buildFullIngredientList(Context context, List<Ingredient> ingredients){
		StringBuilder sb = new StringBuilder();

		for (Ingredient ingredient : ingredients) {
			sb.append(buildSingleIngredientString(context, ingredient));
			sb.append("\n");
		}
		return sb.toString();
	}

	public String buildSingleIngredientString(Context context, Ingredient ingredient){
		return context.getString(R.string.ingredients_display,
				formatFloatIfNeeded(ingredient.getQuantity()),
				ingredient.getMeasure().toLowerCase(), ingredient.getIngredient());
	}
}