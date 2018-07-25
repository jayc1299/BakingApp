package com.nanodegree.bakingapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nanodegree.bakingapp.holders.Step;

import java.util.List;

@Dao
public interface StepsDao {

    String TABLE_NAME = "Step";

    @Query("select * from " + TABLE_NAME)
    LiveData<List<Step>> getAllSteps();

    @Query("select * from " + TABLE_NAME + " where id = :stepId AND recipeId = :recipeId")
    LiveData<Step> getStepByIdAndRecipeId(int stepId, int recipeId);

    @Query("select * from " + TABLE_NAME + " where recipeId = :id")
    LiveData<List<Step>> getStepsByRecipeId(int id);

    @Insert
    void insertStep(Step step);

    @Update
    void updateStep(Step step);

    @Delete
    void deleteStep(Step step);

    @Query("delete from " + TABLE_NAME)
    public void deleteAllSteps();
}