package com.utils.gui.stages;

import com.utils.annotations.ApiMethod;
import com.utils.gui.screens.ScreenUtils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;

public final class StageUtils {

	private StageUtils() {
	}

	@ApiMethod
	public static void centerOnScreen(
			final Stage primaryStage) {

		final Rectangle2D widestScreenBounds = ScreenUtils.computeWidestScreenBounds();
		centerOnScreen(primaryStage, widestScreenBounds);
	}

	@ApiMethod
	public static void centerOnScreen(
            final Stage primaryStage,
            final Rectangle2D widestScreenBounds) {

		primaryStage.setX(widestScreenBounds.getMinX() +
				(widestScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
		primaryStage.setY(widestScreenBounds.getMinY() +
				(widestScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
	}
}
