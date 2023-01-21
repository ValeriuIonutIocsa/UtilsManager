package com.utils.gui.screens;

import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public final class ScreenUtils {

	private ScreenUtils() {
	}

	public static Rectangle2D computeWidestScreenBounds() {

		final Screen screen = computeWidestScreen();
		return screen.getBounds();
	}

	private static Screen computeWidestScreen() {

		Screen widestScreen = Screen.getPrimary();
		double maxScreenWidth = widestScreen.getVisualBounds().getWidth();

		final List<Screen> screenList = Screen.getScreens();
		for (final Screen screen : screenList) {

			final double screenWidth = screen.getVisualBounds().getWidth();
			if (screenWidth > maxScreenWidth) {

				widestScreen = screen;
				maxScreenWidth = screenWidth;
			}
		}
		return widestScreen;
	}
}
