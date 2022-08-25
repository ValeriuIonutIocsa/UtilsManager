package com.utils.gui.objects.web_view;

import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class HBoxWebViewSearchLogger extends HBoxWebViewSearch {

	private final String title;

	public HBoxWebViewSearchLogger(
			final CustomWebView customWebView,
			final String title) {

		super(customWebView);

		this.title = title;
	}

	@Override
	protected void createControls(
			final HBox hBoxRoot) {

		final Label labelTitle = BasicControlsFactories.getInstance().createLabel(title, "boldFontSize10");
		labelTitle.setMinWidth(84);
		GuiUtils.addToHBox(hBoxRoot, labelTitle,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 5, 0, 5);

		final Separator separator = BasicControlsFactories.getInstance().createSeparator(Orientation.VERTICAL);
		GuiUtils.addToHBox(hBoxRoot, separator,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		super.createControls(hBoxRoot);

		final Button buttonClear = BasicControlsFactories.getInstance().createButton("clear");
		buttonClear.setTooltip(BasicControlsFactories.getInstance().createTooltip("clear the console below"));
		buttonClear.setOnAction(event -> customWebView.clear());
		GuiUtils.addToHBox(hBoxRoot, buttonClear,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 5, 0, 12);
	}
}
