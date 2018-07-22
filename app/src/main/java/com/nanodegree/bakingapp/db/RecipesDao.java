package com.nanodegree.bakingapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nanodegree.bakingapp.Recipe;

import java.util.List;

@Dao
public interface RecipesDao {

    String TABLE_NAME = "Recipe";

    @Query("select * from " + TABLE_NAME)
	LiveData<List<Recipe>> getAllRecipes();

    @Query("select * from " + TABLE_NAME + " where id = :id")
	LiveData<Recipe> getRecipeById(int id);

    @Insert
    void insertRecipe(Recipe Recipe);

    @Update
    void updateRecipe(Recipe Recipe);

    @Delete
    void deleteRecipe(Recipe Recipe);

    @Query("delete from " + TABLE_NAME)
    public void deleteAllRecipes();
}