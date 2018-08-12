package com.nanodegree.bakingapp.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UiUtilsTest {

	private UiUtils uiUtils;

	@Before
	public void setUp() {
		uiUtils = new UiUtils();
	}

	@Test
	public void testBasicFormatting() {
		assertEquals("7", uiUtils.formatFloatIfNeeded(7.0f));
	}

	@Test
	public void testAlreadyFormatted() {
		assertEquals("100", uiUtils.formatFloatIfNeeded(100f));
	}

	@Test
	public void testRounding() {
		assertEquals("4.2", uiUtils.formatFloatIfNeeded(4.2f));
	}
}