package com.nanodegree.bakingapp.holders;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Step extends RecipeComponent{

	@SerializedName("id")
	@PrimaryKey
	private int id;
	@SerializedName("shortDescription")
	private String shortDescription;
	@SerializedName("description")
	private String description;
	@SerializedName("videoURL")
	private String videoUrl;
	@SerializedName("thumbnailURL")
	private String thumbnailURL;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	@Override
	public String getDisplayName() {
		return description;
	}
}