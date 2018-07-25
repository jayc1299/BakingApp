package com.nanodegree.bakingapp.holders;

import android.content.Context;

public class RecipeHeader extends RecipeComponent{

    private String header;

    public RecipeHeader() {}

    public RecipeHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public String getDisplayName(Context context) {
        return header;
    }
}