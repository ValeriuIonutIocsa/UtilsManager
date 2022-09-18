package com.utils.gui.objects.web_view;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.icons.ImagesGuiUtils;
import com.utils.string.StrUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class HBoxWebViewSearch extends AbstractCustomControl<HBox> {

	private static final String SEARCH_TOOLTIP_TEXT = "enter some text and click enter" +
			System.lineSeparator() + "to search in the console below";

	protected final CustomWebView customWebView;

	public HBoxWebViewSearch(
			final CustomWebView customWebView) {

		this.customWebView = customWebView;
	}

	@Override
	protected HBox createRoot() {

		final HBox hBoxRoot = LayoutControlsFactories.getInstance().createHBox();
		hBoxRoot.setMinWidth(0);

		createControls(hBoxRoot);

		return hBoxRoot;
	}

	protected void createControls(
			final HBox hBoxRoot) {

		final Label searchLabel = BasicControlsFactories.getInstance().createLabel("search:");
		searchLabel.setTooltip(BasicControlsFactories.getInstance().createTooltip(SEARCH_TOOLTIP_TEXT));
		GuiUtils.addToHBox(hBoxRoot, searchLabel,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 5);

		final ToggleButton caseSensitiveToggleButton = createCaseSensitiveToggleButton();

		final TextField searchTextField = BasicControlsFactories.getInstance().createTextField("");
		searchTextField.setTooltip(BasicControlsFactories.getInstance().createTooltip(SEARCH_TOOLTIP_TEXT));
		searchTextField.setOnAction(event -> customWebView.search(
				searchTextField.getText(), caseSensitiveToggleButton.isSelected()));
		customWebView.getRoot().setOnKeyPressed(event -> {
			if (event.isControlDown() && event.getCode() == KeyCode.F) {
				searchTextField.requestFocus();
			}
		});
		GuiUtils.addToHBox(hBoxRoot, searchTextField,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 5);

		final ImageView imageViewSearch = new ImageView(ImagesGuiUtils.IMAGE_SEARCH);
		final StackPane stackPaneImageViewSearch = GuiUtils.addToHBox(hBoxRoot, imageViewSearch,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 5);
		stackPaneImageViewSearch.setPadding(new Insets(0, 2, 0, 2));
		stackPaneImageViewSearch.setOnMouseClicked(mouseEvent -> {
			if (GuiUtils.isLeftClick(mouseEvent)) {
				customWebView.search(searchTextField.getText(), caseSensitiveToggleButton.isSelected());
			}
		});
		GuiUtils.setDefaultBorder(stackPaneImageViewSearch);

		GuiUtils.addToHBox(hBoxRoot, caseSensitiveToggleButton,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 5);
	}

	private static ToggleButton createCaseSensitiveToggleButton() {

		final ToggleButton caseSensitiveToggleButton =
				BasicControlsFactories.getInstance().createToggleNode("Aa", "bold");
		caseSensitiveToggleButton.setTooltip(
				BasicControlsFactories.getInstance().createTooltip("case sensitive search"));
		GuiUtils.setDefaultBorder(caseSensitiveToggleButton);
		return caseSensitiveToggleButton;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
