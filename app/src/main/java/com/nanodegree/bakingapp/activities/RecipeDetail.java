package com.nanodegree.bakingapp.activities;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.adapters.AdapterRecipeDetail;
import com.nanodegree.bakingapp.db.RecipeViewModel;
import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.holders.Recipe;
import com.nanodegree.bakingapp.holders.RecipeComponent;
import com.nanodegree.bakingapp.holders.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetail extends AppCompatActivity {

    public static final String TAG = RecipeDetail.class.getSimpleName();
    public static final String RECIPE_ID = "recipeId";

    private RecipeViewModel viewModel;
    private AdapterRecipeDetail adapter;
    private MediatorLiveData<List<RecipeComponent>> recipeComponents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        //Steup Recycler and Adapter
        RecyclerView recyclerView = findViewById(R.id.detail_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterRecipeDetail(null, new ArrayList<RecipeComponent>());
        recyclerView.setAdapter(adapter);

        //Observe receipeComponents
        recipeComponents = new MediatorLiveData<>();
        recipeComponents.observe(this, new Observer<List<RecipeComponent>>() {
            @Override
            public void onChanged(@Nullable List<RecipeComponent> components) {
                adapter.updateComponents(components);
            }
        });

        //Get recipe ID from intent
        if (getIntent() != null && getIntent().hasExtra(RECIPE_ID)) {
            int recipeId = getIntent().getIntExtra(RECIPE_ID, 0);
            Log.d(TAG, "RecipeId: " + recipeId);
            getRecipeFromDb(recipeId);
        }
    }

    /**
     * Get the recipe from the DB.
     * Then get the ingredients and the steps from their respective tables.
     *
     * @param recipeId recipe ID
     */
    private void getRecipeFromDb(final int recipeId) {
        viewModel.getRecipeById(recipeId).observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                setTitle(recipe.getName());

                recipeComponents.addSource(viewModel.getIngredientsByRecipeId(recipeId), new Observer<List<Ingredient>>() {
                    @Override
                    public void onChanged(@Nullable List<Ingredient> ingredients) {
                        ArrayList<RecipeComponent> components = new ArrayList<>();
                        if (ingredients != null) {
                            components.addAll(ingredients);
                        }
                        recipeComponents.setValue(components);
                    }
                });

                recipeComponents.addSource(viewModel.getStepsByRecipeId(recipeId), new Observer<List<Step>>() {
                    @Override
                    public void onChanged(@Nullable List<Step> steps) {
                        ArrayList<RecipeComponent> components = new ArrayList<>();
                        if (steps != null) {
                            components.addAll(steps);
                        }
                        recipeComponents.setValue(components);
                    }
                });
            }
        });
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

    private void setTitle(String title) {
        ActionBar actionBar = RecipeDetail.this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }
}