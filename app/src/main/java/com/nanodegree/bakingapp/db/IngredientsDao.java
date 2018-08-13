package com.nanodegree.bakingapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.nanodegree.bakingapp.holders.Ingredient;

import java.util.List;

@Dao
public interface IngredientsDao {

    String TABLE_NAME = "Ingredient";

    @Query("select * from " + TABLE_NAME)
	LiveData<List<Ingredient>> getAllIngredients();

    @Query("select * from " + TABLE_NAME + " where id = :id")
	LiveData<Ingredient> getIngredientById(int id);

    @Query("select * from " + TABLE_NAME + " where recipeId = :id")
    LiveData<List<Ingredient>> getIngredientsByRecipeId(int id);

    @Insert
    void insertIngredient(Ingredient ingredient);

    @Update
    void updateIngredient(Ingredient ingredient);

    @Delete
    void deleteIngredient(Ingredient ingredient);

    @Query("delete from " + TABLE_NAME)
    public void deleteAllIngredients();

    @Query("select * from " + TABLE_NAME + " where recipeId = :id")
	Cursor getIngredientsByRecipeIdForWidget(int id);
}