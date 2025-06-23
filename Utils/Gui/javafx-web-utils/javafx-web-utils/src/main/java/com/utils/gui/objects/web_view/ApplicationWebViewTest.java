package com.utils.gui.objects.web_view;

import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.split_pane.CustomSplitPane;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ApplicationWebViewTest extends Application {

	private final TextArea textArea;
	private final CustomWebView customWebView;

	public ApplicationWebViewTest() {

		textArea = BasicControlsFactories.getInstance().createTextArea("");

		final String webViewStyleCss = CustomWebViewUtils.createWebViewStyleCss();
		customWebView = new CustomWebView(webViewStyleCss);
	}

	@Override
	public void start(
			final Stage primaryStage) {

		primaryStage.setTitle("WebView Test");
		primaryStage.setWidth(1000);
		primaryStage.setHeight(700);
		primaryStage.setMinWidth(500);
		primaryStage.setMinHeight(300);
		primaryStage.setMaximized(true);

		final SplitPane splitPaneRoot = createSplitPane();

		final Scene scene = new Scene(splitPaneRoot);
		GuiUtils.setStyleCss(scene, "com/utils/gui/objects/web_view/web_view_test.css");

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private SplitPane createSplitPane() {

		final SplitPane splitPane = new CustomSplitPane(Orientation.HORIZONTAL);

		final VBox vBoxLeft = createVBoxLeft();
		splitPane.getItems().add(vBoxLeft);

		splitPane.getItems().add(customWebView.getRoot());

		splitPane.setDividerPositions(0.4);
		return splitPane;
	}

	private VBox createVBoxLeft() {

		final VBox vBoxLeft = LayoutControlsFactories.getInstance().createVBox();

		GuiUtils.addToVBox(vBoxLeft, textArea,
				Pos.CENTER_LEFT, Priority.ALWAYS, 7, 7, 0, 7);

		final Button buttonLoad = BasicControlsFactories.getInstance().createButton("Load", "boldFontSize10");
		buttonLoad.setOnAction(event -> load());
		GuiUtils.addToVBox(vBoxLeft, buttonLoad,
				Pos.CENTER_RIGHT, Priority.NEVER, 7, 7, 7, 7);

		return vBoxLeft;
	}

	private void load() {

		final String text = textArea.getText();
		customWebView.load(text);
	}
}
