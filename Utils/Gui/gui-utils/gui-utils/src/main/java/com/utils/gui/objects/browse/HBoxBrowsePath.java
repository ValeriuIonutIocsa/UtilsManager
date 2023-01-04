package com.utils.gui.objects.browse;

import java.io.File;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.io.PathUtils;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public abstract class HBoxBrowsePath extends AbstractCustomControl<HBox> {

	private final String name;

	private final TextField pathTextField;

	private String initialDirectoryPathString;

	HBoxBrowsePath(
			final String name,
			final String initialValue,
			final String initialDirectoryPathString) {

		this.name = name;
		this.initialDirectoryPathString = initialDirectoryPathString;

		pathTextField = BasicControlsFactories.getInstance().createTextField("");
		if (StringUtils.isNotBlank(initialValue)) {
			pathTextField.setText(initialValue);
		}
	}

	@Override
	protected HBox createRoot() {

		final HBox hBoxRoot = LayoutControlsFactories.getInstance().createHBox();

		GuiUtils.addToHBox(hBoxRoot, pathTextField,
				Pos.CENTER, Priority.ALWAYS, 0, 0, 0, 0);

		final Button buttonBrowse = BasicControlsFactories.getInstance().createButton("...");
		buttonBrowse.setOnAction(event -> browse());
		GuiUtils.addToHBox(hBoxRoot, buttonBrowse,
				Pos.CENTER, Priority.NEVER, 0, 0, 0, 7);

		return hBoxRoot;
	}

	private void browse() {

		final String pathString = browsePath();
		if (StringUtils.isNotBlank(pathString)) {
			pathTextField.setText(pathString);
		}
	}

	abstract String browsePath();

	File getInitialDirectory() {

		final File initialDirectory;
		initialDirectory = new File(
				Objects.requireNonNullElseGet(initialDirectoryPathString, PathUtils::createRootPath));
		return initialDirectory;
	}

	public String getPathString() {
		return GuiUtils.computeTextInputControlPathString(pathTextField);
	}

	String getName() {
		return name;
	}

	public void setInitialDirectoryPathString(
			final String initialDirectoryPathString) {
		this.initialDirectoryPathString = initialDirectoryPathString;
	}

	public TextField getPathTextField() {
		return pathTextField;
	}
}
