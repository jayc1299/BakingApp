package com.nanodegree.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.holders.Step;
import com.nanodegree.bakingapp.utils.UiUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FragmentStep extends Fragment{

	private static final String TAG = FragmentStep.class.getSimpleName();
	private static final String SHOW_INGREDIENTS_STATE = "show_ingredients_state";
	private static final String RECIPIE_ID_STATE = "recipe_id_state";
	private static final String STEP_ID_STATE = "step_id_state";
	private static final String VIDEO_PLAYER_STATE = "video_player_state";
	private static final String VIDEO_PLAYER_PLAYING_STATE = "video_player_playing_state";

	private RecipeViewModel viewModel;
	private UiUtils uiUtils;

	private TextView description;
	private LinearLayout ingredientList;
	private ImageView thumbnail;

	private boolean showIngredients;
	private int recipeId = 0;
	private int stepId = 0;
	private long exoPlayerState = 0L;
	private boolean isPlaying = false;

	private PlayerView exoPlayerView;
	private SimpleExoPlayer exoPlayer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
		uiUtils = new UiUtils();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_step, container, false);

		description = view.findViewById(R.id.fragment_step_details);
		ingredientList = view.findViewById(R.id.fragment_step_ingredients);
		exoPlayerView = view.findViewById(R.id.fragment_step_movie);
		thumbnail = view.findViewById(R.id.fragment_step_thumbnail);

		return view;
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		outState.putBoolean(SHOW_INGREDIENTS_STATE, showIngredients);
		outState.putInt(RECIPIE_ID_STATE, recipeId);
		outState.putInt(STEP_ID_STATE, stepId);
		if(exoPlayer != null) {
			outState.putLong(VIDEO_PLAYER_STATE, exoPlayer.getContentPosition());
			outState.putBoolean(VIDEO_PLAYER_PLAYING_STATE, exoPlayer.getPlayWhenReady());
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			showDetails(
					savedInstanceState.getBoolean(SHOW_INGREDIENTS_STATE),
					savedInstanceState.getInt(RECIPIE_ID_STATE),
					savedInstanceState.getInt(STEP_ID_STATE)
			);
			exoPlayerState = savedInstanceState.getLong(VIDEO_PLAYER_STATE);
			isPlaying = savedInstanceState.getBoolean(VIDEO_PLAYER_PLAYING_STATE);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (exoPlayer != null) {
			exoPlayer.release();
		}
	}

	public void showDetails(boolean showIngredients, int recipeId, int stepId){
		//save these for save instance state
		this.showIngredients = showIngredients;
		this.recipeId = recipeId;
		this.stepId = stepId;

		Log.d(TAG, "showDetails showIngredients: " + showIngredients);
		Log.d(TAG, "showDetails recipeId: " + recipeId);
		Log.d(TAG, "showDetails stepId: " + stepId);

		if (showIngredients) {
			//Show ingredient list
			viewModel.getIngredientsByRecipeId(recipeId).observe(this, new Observer<List<Ingredient>>() {
				@Override
				public void onChanged(@Nullable List<Ingredient> ingredients) {
					showIngredients(ingredients);
				}
			});
		} else {
			//show the step details
			viewModel.getStepByStepIdAndRecipeId(stepId, recipeId).observe(this, new Observer<Step>() {
				@Override
				public void onChanged(@Nullable Step step) {
					if (step != null) {
						description.setText(step.getDescription());
						description.setVisibility(View.VISIBLE);
						ingredientList.setVisibility(View.GONE);

						if (!TextUtils.isEmpty(step.getThumbnailURL())) {
							setupExoPlayer();
							setupMedia(Uri.parse(step.getVideoUrl()));
							thumbnail.setVisibility(View.GONE);
							exoPlayerView.setVisibility(View.VISIBLE);
						}else{
							Picasso.get()
									.load(step.getThumbnailURL())
									.into(thumbnail);
							thumbnail.setVisibility(View.VISIBLE);
							exoPlayerView.setVisibility(View.GONE);
						}
					}else{
						Log.w(TAG, "Step null");
					}
				}
			});
		}
	}

	private void showIngredients(List<Ingredient> ingredients) {
		description.setVisibility(View.GONE);
		exoPlayerView.setVisibility(View.GONE);
		ingredientList.removeAllViews();
		ingredientList.setVisibility(View.VISIBLE);

		for (Ingredient ingredient : ingredients) {
			View ingredientView = getLayoutInflater().inflate(R.layout.item_ingredient, ingredientList, false);
			String ingredientDisplayName = uiUtils.buildSingleIngredientString(getActivity(), ingredient);
			((TextView) ingredientView.findViewById(R.id.item_ing_title)).setText(ingredientDisplayName);
			ingredientList.addView(ingredientView);
		}
	}

	private void setupExoPlayer(){
		//1) Do stuff
		BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
		TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
		DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

		// 2. Create the player
		exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

		// 3. Bind the player to the view.
		exoPlayerView.setPlayer(exoPlayer);
	}

	private void setupMedia(Uri mediaUri){
		if (getActivity() != null && !getActivity().isFinishing()) {
			Log.d(TAG, "setupMedia: " + mediaUri.toString());

			if (TextUtils.isEmpty(mediaUri.toString())) {
				exoPlayerView.hideController();
				exoPlayerView.setEnabled(false);
				exoPlayerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
					@Override
					public void onVisibilityChange(int visibility) {
						exoPlayerView.hideController();
					}
				});
				return;
			} else {
				exoPlayerView.showController();
				exoPlayerView.setEnabled(true);
				exoPlayerView.setControllerVisibilityListener(null);
			}

			// Measures bandwidth during playback. Can be null if not required.
			DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
			// Produces DataSource instances through which media data is loaded.
			DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
					Util.getUserAgent(getActivity(), getString(R.string.app_name)), defaultBandwidthMeter);
			// This is the MediaSource representing the media to be played.
			MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
			// Prepare the player with the source.
			exoPlayer.prepare(videoSource);
			if (exoPlayerState > 0) {
				exoPlayer.seekTo(exoPlayerState);
			}
			if (isPlaying) {
				exoPlayer.setPlayWhenReady(isPlaying);
			}
		}
	}
}