package com.nanodegree.bakingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.bakingapp.R;

import java.util.List;

public class AdapterComponents extends RecyclerView.Adapter<AdapterComponents.StepViewHolder> {

    public interface IRecipeComponentClickListener {
        void onComponentClicked(int component);
    }

    private List<String> recipeComponents;
    private IRecipeComponentClickListener listener;

    public AdapterComponents(IRecipeComponentClickListener listener, List<String> recipeComponents) {
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
        final int viewPosition = position;
        final String component = recipeComponents.get(position);
        holder.title.setText(component);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onComponentClicked(viewPosition);
                }
            }
        });
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