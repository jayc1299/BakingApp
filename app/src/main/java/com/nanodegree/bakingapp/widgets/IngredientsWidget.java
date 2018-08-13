package com.nanodegree.bakingapp.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.db.AppDatabase;
import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.utils.UiUtils;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidgetConfigureActivity IngredientsWidgetConfigureActivity}
 */
public class IngredientsWidget extends AppWidgetProvider {

	private static final String TAG = IngredientsWidget.class.getSimpleName();
	private AppDatabase database;
	private UiUtils uiUtils;

	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String ingredients) {
		int recipeId = IngredientsWidgetConfigureActivity.getRecipeIdFromPref(context, appWidgetId);
		Log.d(TAG, "updateAppWidget appWidgetId: " + appWidgetId + "==" + recipeId);

		// Construct the RemoteViews object
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
		views.setTextViewText(R.id.widget_ingredient_list, ingredients);

		// Instruct the widget manager to update the widget
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@Override
	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// There may be multiple widgets active, so update all of them
		Log.d(TAG, "onUpdate: " + appWidgetIds.length);

		database = AppDatabase.getInstance(context.getApplicationContext());
		uiUtils = new UiUtils();

		for (final int appWidgetId : appWidgetIds) {
			//Get recipe id associated with appWidgetId
			final int recipeId = IngredientsWidgetConfigureActivity.getRecipeIdFromPref(context, appWidgetId);

			if (recipeId > 0) {
				//Need to access DB on a thread. Can't use viewModel in a widget, sigh.
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						Log.d(TAG, "run recipe: " + recipeId);
						//Get the ingredients for a given recipeId from the DB.
						Cursor cursor = database.ingredientsDao().getIngredientsByRecipeIdForWidget(recipeId);
						if(cursor != null && cursor.moveToFirst()) {

							ArrayList<Ingredient> ingredients = new ArrayList<>();
							while (cursor.moveToNext()) {
								//Create a shell ingredient so we can display with existing function.
								Ingredient ingredient = new Ingredient();
								ingredient.setIngredient(cursor.getString(cursor.getColumnIndex(Ingredient.INGREDIENT_COLUMN_NAME)));
								ingredient.setMeasure(cursor.getString(cursor.getColumnIndex(Ingredient.MEASURE_COLUMN_NAME)));
								ingredient.setQuantity(cursor.getFloat(cursor.getColumnIndex(Ingredient.QUANTITY_COLUMN_NAME)));
								ingredients.add(ingredient);
							}

							//Convert ingredient list into a string
							String ingredientList = uiUtils.buildFullIngredientList(context, ingredients);

							//Call static method to set widget UI
							IngredientsWidget.updateAppWidget(context,
									appWidgetManager,
									appWidgetId,
									ingredientList);
							cursor.close();
						}
					}
				});
				thread.start();
			}

		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// When the user deletes the widget, delete the preference associated with it.
		for (int appWidgetId : appWidgetIds) {
			IngredientsWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
		}
	}

	@Override
	public void onEnabled(Context context) {
		// Enter relevant functionality for when the first widget is created
	}

	@Override
	public void onDisabled(Context context) {
		// Enter relevant functionality for when the last widget is disabled
	}


	public static void sendRefreshBroadcast(Context context) {
		Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		intent.setComponent(new ComponentName(context, IngredientsWidget.class));
		context.sendBroadcast(intent);
	}

	@Override
	public void onReceive(final Context context, Intent intent) {
		final String action = intent.getAction();
		if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
			// refresh all your widgets
			AppWidgetManager mgr = AppWidgetManager.getInstance(context);
			ComponentName cn = new ComponentName(context, IngredientsWidget.class);
			mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widget_ingredient_list);
		}
		super.onReceive(context, intent);
	}
}