package com.nanodegree.bakingapp.widgets;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.adapters.SpinnerAdapterRecipe;
import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.holders.Recipe;
import com.nanodegree.bakingapp.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The configuration screen for the {@link IngredientsWidget IngredientsWidget} AppWidget.
 */
public class IngredientsWidgetConfigureActivity extends AppCompatActivity {

	private static final String TAG = IngredientsWidgetConfigureActivity.class.getSimpleName();

	private static final String PREFS_NAME = "com.nanodegree.bakingapp.widgets.IngredientsWidget";
	private static final String PREF_PREFIX_KEY = "appwidget_";

	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private AppCompatSpinner mAppWidgetSpinner;
	private SpinnerAdapterRecipe adapterRecipe;
	private RecipeViewModel viewModel;
	private UiUtils uiUtils;

	public IngredientsWidgetConfigureActivity() {
		super();
	}

	// Write the prefix to the SharedPreferences object for this widget
	static void saveRecipeIdPref(Context context, int appWidgetId, int recipeId) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
		prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipeId);
		prefs.apply();
	}

	// Get the recipeId from shared prefs
	static int getRecipeIdFromPref(Context context, int appWidgetId) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
	}

	static void deleteTitlePref(Context context, int appWidgetId) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
		prefs.remove(PREF_PREFIX_KEY + appWidgetId);
		prefs.apply();
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Set the result to CANCELED.  This will cause the widget host to cancel
		// out of the widget placement if the user presses the back button.
		setResult(RESULT_CANCELED);

		uiUtils = new UiUtils();

		setContentView(R.layout.ingredients_widget_configure);
		mAppWidgetSpinner = findViewById(R.id.widget_config_recipie_spinner);
		findViewById(R.id.widget_config_add_button).setOnClickListener(mOnClickListener);

		//Setup spinner
		adapterRecipe = new SpinnerAdapterRecipe(IngredientsWidgetConfigureActivity.this,
				0,
				new ArrayList<Recipe>());
		adapterRecipe.setDropDownViewResource(R.layout.item_recipe_small);
		mAppWidgetSpinner.setAdapter(adapterRecipe);

		// Find the widget id from the intent.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// If this activity was started with an intent without an app widget ID, finish with an error.
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
			return;
		}

		//Load recipes into spinner
		viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
		viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
			@Override
			public void onChanged(@Nullable List<Recipe> recipes) {
				adapterRecipe.updateRecipes(recipes);
			}
		});
	}


	View.OnClickListener mOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			final Context context = IngredientsWidgetConfigureActivity.this;

			int recipeId = ((Recipe) mAppWidgetSpinner.getSelectedItem()).getId();
			saveRecipeIdPref(context, mAppWidgetId, recipeId);
			Log.d(TAG, "onClick: " + mAppWidgetId + "==" + recipeId);

			// It is the responsibility of the configuration activity to update the app widget
			viewModel.getIngredientsByRecipeId(recipeId).observe(IngredientsWidgetConfigureActivity.this, new Observer<List<Ingredient>>() {
				@Override
				public void onChanged(@Nullable List<Ingredient> ingredients) {
					if(ingredients != null && ingredients.size() > 0) {
						AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
						String ingredientList = uiUtils.buildFullIngredientList(context, ingredients);
						IngredientsWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId, ingredientList);
					}
				}
			});


			// Make sure we pass back the original appWidgetId
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}
	};
}

