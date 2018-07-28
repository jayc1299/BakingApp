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
	private TextView nextButton;
	private TextView previousButton;
	private int currentStepId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step);

		nextButton = findViewById(R.id.activity_step_next);
		previousButton = findViewById(R.id.activity_step_previous);

		viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

		if (getIntent() != null) {
			//Get recipe name
			String recipeName = getIntent().getStringExtra(RECIPE_NAME);
			if(recipeName != null && recipeName.length() > 0) {
				setTitle(recipeName);
			}
			//Get step ID
			currentStepId = getIntent().getIntExtra(STEP_ID, 0);
			int recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
			Log.d(TAG, "stepId: " + currentStepId);
			Log.d(TAG, "recipeId: " + recipeId);
			setupStep(recipeId);
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

	private void setupStep(int recipeId){
		getStepFromDb(recipeId);
		findNextStep(recipeId);
		findPreviousStep(recipeId);
	}

	private void getStepFromDb(int recipeId) {
		viewModel.getStepByStepIdAndRecipeId(currentStepId, recipeId).observe(RecipeStepActivity.this, new Observer<Step>() {
			@Override
			public void onChanged(@Nullable Step step) {
				if(step != null) {
					Log.d(TAG, "getStepFromDb: " + step.getDescription());
					showStep(step);
				}
			}
		});
	}

	private void findNextStep(final int recipeId) {
		final int nextStepId = currentStepId + 1;
		viewModel.getStepByStepIdAndRecipeId(nextStepId, recipeId).observe(RecipeStepActivity.this, new Observer<Step>() {
			@Override
			public void onChanged(@Nullable Step step) {
				if(step != null) {
					Log.d(TAG, "findNextStep: " + step.getDescription());
					nextButton.setVisibility(View.VISIBLE);
					nextButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							currentStepId = nextStepId;
							setupStep(recipeId);
						}
					});
				}else{
					nextButton.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	private void findPreviousStep(final int recipeId) {
		final int previousStepId = currentStepId - 1;
		viewModel.getStepByStepIdAndRecipeId(previousStepId, recipeId).observe(RecipeStepActivity.this, new Observer<Step>() {
			@Override
			public void onChanged(@Nullable Step step) {
				if(step != null) {
					Log.d(TAG, "findPreviousStep: " + step.getDescription());
					previousButton.setVisibility(View.VISIBLE);
					previousButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							currentStepId = previousStepId;
							setupStep(recipeId);
						}
					});
				}else{
					previousButton.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	private void showStep(Step step){
		TextView longDesc = findViewById(R.id.activity_step_details);
		longDesc.setText(step.getDescription());

		findViewById(R.id.activity_step_previous).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(RecipeStepActivity.this, "Previous", Toast.LENGTH_SHORT).show();
			}
		});
	}
}