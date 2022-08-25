package com.utils.gui.preloader;

import com.utils.gui.GuiUtils;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.stages.StageUtils;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class AbstractCustomPreloader extends Preloader {

	private static Stage primaryStage;

	private final String title;

	protected AbstractCustomPreloader(
			final String title) {

		this.title = title;
	}

	@Override
	public void start(
			final Stage primaryStage) {

		AbstractCustomPreloader.primaryStage = primaryStage;

		primaryStage.setTitle(title);

		final Image image = getImage();
		GuiUtils.setAppIcon(primaryStage, image);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setWidth(image.getWidth());
		primaryStage.setHeight(image.getHeight() + 20);

		final VBox vBoxPrimary = LayoutControlsFactories.getInstance().createVBox();

		final ImageView imageView = new ImageView(image);
		vBoxPrimary.getChildren().add(imageView);

		final ProgressIndicatorBarPreloader progressIndicatorBarPreloader =
				new ProgressIndicatorBarPreloader();
		vBoxPrimary.getChildren().add(progressIndicatorBarPreloader.getRoot());

		final Scene scene = new Scene(vBoxPrimary);
		scene.getStylesheets().add("com/utils/gui/preloader/preloader.css");
		primaryStage.setScene(scene);

		primaryStage.setOnShown(event -> StageUtils.centerOnScreen(primaryStage));
		primaryStage.show();
	}

	protected abstract Image getImage();

	public static void hide() {
		primaryStage.hide();
	}
}
