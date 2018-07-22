package com.nanodegree.bakingapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Recipe {

	@SerializedName("id")
	@PrimaryKey
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("servings")
	private int servings;
	@SerializedName("image")
	private String image;

	public Recipe() {}

	public Recipe(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}