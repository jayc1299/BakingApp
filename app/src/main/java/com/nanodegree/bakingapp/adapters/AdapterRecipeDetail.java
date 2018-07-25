package com.nanodegree.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.holders.RecipeComponent;
import com.nanodegree.bakingapp.holders.Step;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecipeDetail extends RecyclerView.Adapter<AdapterRecipeDetail.StepViewHolder> {

    public interface IRecipeComponentClickListener {
        void onComponentClicked(RecipeComponent component);
    }

    private List<Step> recipeComponents;
    private IRecipeComponentClickListener listener;
    private Context context;

    public AdapterRecipeDetail(Context context, IRecipeComponentClickListener listener, List<Step> recipeComponents) {
        this.context = context;
        this.listener = listener;
        this.recipeComponents = recipeComponents;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StepViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        //step
        final Step step = (Step) recipeComponents.get(position);
        StepViewHolder newHolder = (StepViewHolder) holder;
        newHolder.title.setText(step.getDisplayName(context));
        newHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onComponentClicked(step);
                }
            }
        });
    }

    public void updateSteps(List<Step> steps) {
        recipeComponents = new ArrayList<>();
        recipeComponents.addAll(steps);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipeComponents.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        StepViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_step_title);
        }
    }
}