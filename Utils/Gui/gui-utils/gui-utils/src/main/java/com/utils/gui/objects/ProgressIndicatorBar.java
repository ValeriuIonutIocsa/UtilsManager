package com.utils.gui.objects;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.string.StrUtils;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

public class ProgressIndicatorBar extends AbstractCustomControl<StackPane> {

	private final SimpleDoubleProperty value;

	public ProgressIndicatorBar() {

		value = new SimpleDoubleProperty();
	}

	@Override
	protected StackPane createRoot() {

		final StackPane stackPane =
				LayoutControlsFactories.getInstance().createStackPane(Pos.CENTER);

		stackPane.getStylesheets().add("com/utils/gui/objects/progress_indicator_bar.css");

		stackPane.setPrefWidth(150);
		stackPane.setPrefHeight(24);
		stackPane.setMinHeight(24);

		final Label label = BasicControlsFactories.getInstance()
				.createLabel("loading", "label-progress-indicator-bar");
		final SimpleStringProperty labelValue = new SimpleStringProperty();
		Bindings.bindBidirectional(labelValue, value, new StringConverter<>() {

			@Override
			public String toString(
					final Number number) {

				final String str;
				if (number == null) {
					str = "";

				} else {
					final double n = (double) number;
					if (n <= 0 || Double.isNaN(n)) {
						str = "";
					} else {
						str = StrUtils.doubleToPercentageString(n, 2);
					}
				}
				return str;
			}

			@Override
			public Number fromString(
					final String string) {
				return null;
			}
		});
		label.textProperty().bind(labelValue);

		final ProgressBar progressBar = new ProgressBar();
		progressBar.setPrefHeight(24);
		progressBar.setMinHeight(24);
		progressBar.setPrefWidth(Double.MAX_VALUE);
		progressBar.progressProperty().bind(value);

		stackPane.getChildren().setAll(progressBar, label);

		return stackPane;
	}

	public void updateValue(
			final double value) {
		GuiUtils.run(() -> this.value.setValue(value));
	}
}
