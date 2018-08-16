package com.nanodegree.bakingapp.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.holders.Step;

public class RecipeStepActivity extends AppCompatActivity {

	public static final String RECIPE_ID = "recipeId";
	public static final String STEP_ID = "stepId";
	public static final String STEP_NAME = "stepName";
	private static final String TAG = RecipeStepActivity.class.getSimpleName();

	private RecipeViewModel viewModel;
	private TextView nextButton;
	private TextView previousButton;
	private int currentStepId;

	private PlayerView exoPlayerView;
	private SimpleExoPlayer exoPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step);

		nextButton = findViewById(R.id.activity_step_next);
		previousButton = findViewById(R.id.activity_step_previous);
		exoPlayerView = findViewById(R.id.exo_player);

		viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);


		if (getIntent() != null) {
			//Get recipe name
			String recipeName = getIntent().getStringExtra(STEP_NAME);
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
	protected void onStop() {
		super.onStop();
		exoPlayer.release();
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

	private void setupExoPlayer(){
		//1) Do stuff
		BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
		TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
		DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

		// 2. Create the player
		exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

		// 3. Bind the player to the view.
		exoPlayerView.setPlayer(exoPlayer);
	}

	private void setupMedia(Uri mediaUri){
		Log.d(TAG, "setupMedia: " + mediaUri.toString());

		if(TextUtils.isEmpty(mediaUri.toString())){
			exoPlayerView.hideController();
			exoPlayerView.setEnabled(false);
			exoPlayerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
				@Override
				public void onVisibilityChange(int visibility) {
					exoPlayerView.hideController();
				}
			});
			return;
		}else{
			exoPlayerView.showController();
			exoPlayerView.setEnabled(true);
			exoPlayerView.setControllerVisibilityListener(null);
		}

		// Measures bandwidth during playback. Can be null if not required.
		DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
		// Produces DataSource instances through which media data is loaded.
		DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
				Util.getUserAgent(this, getString(R.string.app_name)), defaultBandwidthMeter);
		// This is the MediaSource representing the media to be played.
		MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
		// Prepare the player with the source.
		exoPlayer.prepare(videoSource);
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
					setupExoPlayer();
					setupMedia(Uri.parse(step.getVideoUrl()));
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
					nextButton.setVisibility(View.VISIBLE);
					nextButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							exoPlayer.stop();
							exoPlayer.release();
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
				if(step != null && step.getId() != 0) {
					previousButton.setVisibility(View.VISIBLE);
					previousButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							exoPlayer.stop();
							exoPlayer.release();
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
		setTitle(step.getShortDescription());
	}
}