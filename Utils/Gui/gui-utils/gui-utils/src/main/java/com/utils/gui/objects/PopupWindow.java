package com.utils.gui.objects;

import com.utils.gui.data.Dimensions;
import com.utils.gui.stages.StageUtils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupWindow extends Stage {

	public PopupWindow(
			final Scene parentScene,
			final Modality modality,
			final String title,
			final Dimensions dimensions,
			final Parent parent) {

		getIcons().addAll(((Stage) parentScene.getWindow()).getIcons());
		setTitle(title);

		if (dimensions != null) {
			dimensions.set(this);
		} else {
			sizeToScene();
		}

		initModality(modality);

		final Scene scene = new Scene(parent);
		scene.getStylesheets().addAll(parentScene.getStylesheets());

		setOnShown(event -> {

			StageUtils.centerOnScreen(this);
			if (dimensions == null) {

				final double height = getHeight();
				setMinHeight(height);
				final double width = getWidth();
				setMinWidth(width);
			}
			scene.getRoot().requestFocus();
		});
		setScene(scene);
	}
}
