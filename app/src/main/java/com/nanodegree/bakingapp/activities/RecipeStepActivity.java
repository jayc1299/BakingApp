package com.nanodegree.bakingapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.fragments.FragmentStep;

public class RecipeStepActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "recipeId";
    public static final String STEP_ID = "stepId";
    public static final String STEP_NAME = "stepName";
    private static final String TAG = RecipeStepActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        if (getIntent() != null && savedInstanceState == null) {
            //Get recipe name
            String recipeName = getIntent().getStringExtra(STEP_NAME);
            if (recipeName != null && recipeName.length() > 0) {
                setTitle(recipeName);
            }
            //Get step ID
            int currentStepId = getIntent().getIntExtra(STEP_ID, 0);
            int recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
            Log.d(TAG, "stepId: " + currentStepId);
            Log.d(TAG, "recipeId: " + recipeId);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentStep frag = new FragmentStep();
            Bundle bundle = new Bundle();
            bundle.putInt(FragmentStep.RECIPE_ID, recipeId);
            bundle.putInt(FragmentStep.STEP_ID, currentStepId);
            frag.setArguments(bundle);
            ft.replace(R.id.activity_step_fragment, frag, frag.getClass().getSimpleName());
            ft.commit();

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
    /*

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(exoPlayer != null) {
			outState.putLong(VIDEO_PLAYER_STATE, exoPlayer.getContentPosition());
			outState.putBoolean(VIDEO_PLAYER_PLAYING_STATE, exoPlayer.getPlayWhenReady());
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if(savedInstanceState != null) {
			exoPlayerState = savedInstanceState.getLong(VIDEO_PLAYER_STATE);
			isPlaying = savedInstanceState.getBoolean(VIDEO_PLAYER_PLAYING_STATE);
		}
		super.onRestoreInstanceState(savedInstanceState);
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
		if(exoPlayerState > 0){
			exoPlayer.seekTo(exoPlayerState);
		}
		if(isPlaying){
			exoPlayer.setPlayWhenReady(isPlaying);
		}
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
	*/
}