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
}