package com.nanodegree.bakingapp.utils;

import java.text.DecimalFormat;

public class UiUtils {

	public String formatFloatIfNeeded(float quantity) {
		return new DecimalFormat("#.##").format(quantity);
	}

}