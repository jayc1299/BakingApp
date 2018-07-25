package com.nanodegree.bakingapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.holders.Step;

public class RecipeStepActivity extends AppCompatActivity {

	public static final String RECIPE_ID = "recipeId";
	public static final String STEP_ID = "stepId";
	public static final String RECIPE_NAME = "recipeName";
	private static final String TAG = RecipeStepActivity.class.getSimpleName();

	private RecipeViewModel viewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step);

		viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

		if (getIntent() != null) {
			//Get recipe name
			String recipeName = getIntent().getStringExtra(RECIPE_NAME);
			if(recipeName != null && recipeName.length() > 0) {
				setTitle(recipeName);
			}
			//Get step ID
			int stepId = getIntent().getIntExtra(STEP_ID, 0);
			int recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
			Log.d(TAG, "stepId: " + stepId);
			Log.d(TAG, "recipeId: " + recipeId);
			getStepFromDb(stepId, recipeId);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home: {
				this.finish();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void getStepFromDb(int stepId, int recipeId) {
		viewModel.getStepByStepIdAndRecipeId(stepId, recipeId).observe(RecipeStepActivity.this, new Observer<Step>() {
			@Override
			public void onChanged(@Nullable Step step) {
				if(step != null) {
					Log.d(TAG, "onChanged: " + step.getDescription());
					showStep(step);
				}
			}
		});
	}

	private void showStep(Step step){
		TextView longDesc = findViewById(R.id.activity_step_details);
		longDesc.setText(step.getDescription());

		//Next clicked
		findViewById(R.id.activity_step_next).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(RecipeStepActivity.this, "Next", Toast.LENGTH_SHORT).show();
			}
		});

		findViewById(R.id.activity_step_previous).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(RecipeStepActivity.this, "Previous", Toast.LENGTH_SHORT).show();
			}
		});
	}
}