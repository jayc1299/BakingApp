package com.nanodegree.bakingapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.adapters.AdapterComponents;
import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.holders.Step;

import java.util.ArrayList;
import java.util.List;

import static com.nanodegree.bakingapp.activities.RecipeDetailActivity.RECIPE_ID;

public class FragmentStepList extends Fragment{

	public interface IStepListClicked{
		void onStepClicked(boolean ingredients, int stepId);
	}

	private static final String TAG = FragmentStepList.class.getSimpleName();
	private RecipeViewModel viewModel;
	private int recipeId;
	private RecyclerView recyclerView;
	private ArrayList<String> recipeDetails;
	private AdapterComponents adapter;
	private IStepListClicked listener;

	private static final String SAVED_LAYOUT_MANAGER = "saved_layout_manager";
	private static final String SAVED_SELECTED_ITEM = "saved_selected_item";
	private Parcelable layoutManagerSavedState;
	private int selectedItemSavedSate = RecyclerView.NO_POSITION;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

		Bundle args = getArguments();
		if (args != null && args.containsKey(RECIPE_ID)) {
			recipeId = args.getInt(RECIPE_ID, 0);
			Log.d(TAG, "RecipeId: " + recipeId);
			//Create a list of details
			recipeDetails = new ArrayList<>();
			recipeDetails.add(getString(R.string.ingredients_header));
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_step_list, container, false);

		recyclerView = view.findViewById(R.id.frag_step_list_recycler);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		adapter = new AdapterComponents( componentClickListener, recipeDetails);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(adapter);
		getRecipeDetailsFromDb(recipeId);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		Parcelable state = recyclerView.getLayoutManager().onSaveInstanceState();
		int selectedItem = adapter.getSelectedItemPosition();
		outState.putParcelable(SAVED_LAYOUT_MANAGER, state);
		outState.putInt(SAVED_SELECTED_ITEM, selectedItem);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			layoutManagerSavedState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
			selectedItemSavedSate = savedInstanceState.getInt(SAVED_SELECTED_ITEM, RecyclerView.NO_POSITION);
		}
	}

	public void setStepClickedListener(IStepListClicked listener){
		this.listener = listener;
	}

	private void getRecipeDetailsFromDb(final int recipeId) {
		Log.d(TAG, "getRecipeDetailsFromDb: ");
		if (getActivity() != null) {

			//Get all steps for receipeId
			viewModel.getStepsByRecipeId(recipeId).observe(getActivity(), new Observer<List<Step>>() {
				@Override
				public void onChanged(@Nullable List<Step> steps) {
					Log.d(TAG, "onChanged: ");
					if (steps != null && steps.size() > 0) {

						for (Step step : steps) {
							recipeDetails.add(step.getShortDescription());
						}

						adapter.notifyDataSetChanged();

						if (layoutManagerSavedState != null) {
							Log.d(TAG, "onChanged: SavedState Not Null");
							recyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
						}
						if(selectedItemSavedSate != RecyclerView.NO_POSITION) {
							adapter.setSelected(selectedItemSavedSate);
						}
					}
				}
			});
		}
	}

	private AdapterComponents.IRecipeComponentClickListener componentClickListener = new AdapterComponents.IRecipeComponentClickListener() {
		@Override
		public void onComponentClicked(int component) {
			Log.d(TAG, "onComponentClicked: " + component);
			if(listener != null){
				listener.onStepClicked(component == 0, component);
			}
		}
	};
}