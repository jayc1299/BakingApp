package com.nanodegree.bakingapp.widgets;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class MyWidgetRemoteViewsService extends RemoteViewsService {

	private static final String TAG = MyWidgetRemoteViewsService.class.getSimpleName();

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		Log.d(TAG, "onGetViewFactory: " + intent.getIntExtra(IngredientsWidgetRemoteViewsFactory.PARAM_RECIPE_ID, 0));
		return new IngredientsWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
	}
}