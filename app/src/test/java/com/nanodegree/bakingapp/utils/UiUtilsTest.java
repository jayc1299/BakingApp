package com.nanodegree.bakingapp.utils;

import android.content.Context;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.holders.Ingredient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UiUtilsTest {

	private UiUtils uiUtils;

	private Context context;

	@Before
	public void setUp() {
		uiUtils = new UiUtils();

		context = Mockito.mock(Context.class);
		when(context.getString(R.string.ingredients_display, "7", "g", "Chocolate")).thenReturn("7 g Chocolate");
		when(context.getString(R.string.ingredients_display, "5.5", "cups", "milk")).thenReturn("5.5 cups milk");
	}

	@Test
	public void testBasicFormatting() {
		assertEquals("7", uiUtils.formatFloatIfNeeded(7.0f));
	}

	@Test
	public void testAlreadyFormatted() {
		assertEquals("100", uiUtils.formatFloatIfNeeded(100f));
	}

	@Test
	public void testRounding() {
		assertEquals("4.2", uiUtils.formatFloatIfNeeded(4.2f));
	}

	@Test
	public void testSingleIngredient(){
		String expected = "7 g Chocolate";
		Ingredient ingredient = new Ingredient();
		ingredient.setQuantity(7.0f);
		ingredient.setMeasure("g");
		ingredient.setIngredient("Chocolate");

		String fullIngredientTitle = uiUtils.buildSingleIngredientString(context, ingredient);

		assertEquals(expected, fullIngredientTitle);
	}

	@Test
	public void testListOfIngredients(){
		String expected = "7 g Chocolate\n5.5 cups milk\n";

		ArrayList<Ingredient> ingredients = new ArrayList<>();
		Ingredient ingredient = new Ingredient();
		ingredient.setQuantity(7.0f);
		ingredient.setMeasure("g");
		ingredient.setIngredient("Chocolate");
		ingredients.add(ingredient);

		ingredient = new Ingredient();
		ingredient.setQuantity(5.5f);
		ingredient.setMeasure("cups");
		ingredient.setIngredient("milk");
		ingredients.add(ingredient);

		String fullIngredientList = uiUtils.buildFullIngredientList(context, ingredients);

		assertEquals(expected, fullIngredientList);
	}
}