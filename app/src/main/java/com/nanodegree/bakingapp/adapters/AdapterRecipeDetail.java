package com.nanodegree.bakingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.holders.Ingredient;
import com.nanodegree.bakingapp.holders.RecipeComponent;
import com.nanodegree.bakingapp.holders.Step;

import java.util.List;

public class AdapterRecipeDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface IRecipeComponentClickListener {
        void onComponentClicked(RecipeComponent component);
    }

    private static final int TYPE_INGREDIENT = 1;
    private static final int TYPE_STEP = 2;

    private List<RecipeComponent> recipeComponents;
    private IRecipeComponentClickListener listener;

    public AdapterRecipeDetail(IRecipeComponentClickListener listener, List<RecipeComponent> recipeComponents) {
        this.listener = listener;
        this.recipeComponents = recipeComponents;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_INGREDIENT) {
            return new IngredientViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false));
        } else {
            return new StepViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof IngredientViewHolder) {
            //Ingredient
            final Ingredient ingredient = (Ingredient) recipeComponents.get(position);
            IngredientViewHolder newHolder = (IngredientViewHolder) holder;
            newHolder.title.setText(ingredient.getDisplayName());
            newHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onComponentClicked(ingredient);
                    }
                }
            });
        } else {
            //step
            final Step step = (Step) recipeComponents.get(position);
            StepViewHolder newHolder = (StepViewHolder) holder;
            newHolder.title.setText(step.getDisplayName());
            newHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onComponentClicked(step);
                    }
                }
            });
        }
    }

    public void updateComponents(List<RecipeComponent> components) {
        recipeComponents.addAll(components);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipeComponents.size();
    }

    @Override
    public int getItemViewType(int position) {
        RecipeComponent recipeComponent = recipeComponents.get(position);
        if (recipeComponent instanceof Ingredient) {
            return TYPE_INGREDIENT;
        } else {
            return TYPE_STEP;
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_ing_title);
        }
    }

    class StepViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public StepViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_step_title);
        }
    }
}