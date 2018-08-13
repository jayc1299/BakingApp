package com.nanodegree.bakingapp.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.db.AppDatabase;
import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.utils.UiUtils;

public class IngredientsWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

	public static final String PARAM_RECIPE_ID = "param_recipe_id";

	private static final String TAG = IngredientsWidgetRemoteViewsFactory.class.getSimpleName();
	private static final String INGREDIENT_COLUMN_NAME = "ingredient";
	private static final String QUANTITY_COLUMN_NAME = "quantity";
	private static final String MEASURE_COLUMN_NAME = "measure";

	private Context mContext;
	private Cursor mCursor;
	private UiUtils uiUtils;
	private int mAppWidgetId;

	public IngredientsWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
		this.mContext = applicationContext;
		uiUtils = new UiUtils();

		if (intent != null) {
			mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate: ");
	}

	@Override
	public void onDataSetChanged() {
		int recipeId = IngredientsWidgetConfigureActivity.getRecipeIdFromPref(mContext, mAppWidgetId);
		Log.d(TAG, "onDataSetChanged: " + recipeId);
		Log.d(TAG, "mAppWidgetId: " + mAppWidgetId);
		AppDatabase database = AppDatabase.getInstance(mContext.getApplicationContext());
		mCursor = database.ingredientsDao().getIngredientsByRecipeIdForWidget(recipeId);
	}

	@Override
	public void onDestroy() {
		if (mCursor != null) {
			mCursor.close();
		}
	}

	@Override
	public int getCount() {
		return mCursor == null ? 0 : mCursor.getCount();
	}

	@Override
	public RemoteViews getViewAt(int position) {
		if (position == AdapterView.INVALID_POSITION ||
				mCursor == null || !mCursor.moveToPosition(position)) {
			return null;
		}

		RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_widget_item);

		//Create a shell ingredient so we can display with existing function.
		Ingredient ingredient = new Ingredient();
		ingredient.setIngredient(mCursor.getString(mCursor.getColumnIndex(INGREDIENT_COLUMN_NAME)));
		ingredient.setMeasure(mCursor.getString(mCursor.getColumnIndex(MEASURE_COLUMN_NAME)));
		ingredient.setQuantity(mCursor.getFloat(mCursor.getColumnIndex(QUANTITY_COLUMN_NAME)));
		String fullIngredientText = uiUtils.buildSingleIngredientString(mContext, ingredient);
		rv.setTextViewText(R.id.item_ing_title, fullIngredientText);

		return rv;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public long getItemId(int position) {
		return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
}