package com.utils.gui.objects.browse;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import com.utils.gui.factories.BasicControlsFactories;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;

import javafx.stage.FileChooser;

public class HBoxBrowsePathFile extends HBoxBrowsePath {

	public enum Mode {
		OPEN, SAVE
	}

	private final Mode mode;
	private final List<FileChooser.ExtensionFilter> extensionFilterList;

	public HBoxBrowsePathFile(
			final String name,
			final String initialValue,
			final Path initialDirectory,
			final Mode mode,
			final List<FileChooser.ExtensionFilter> extensionFilterList) {

		super(name, initialValue, initialDirectory);

		this.mode = mode;
		this.extensionFilterList = extensionFilterList;
	}

	@Override
	String browsePath() {

		String pathString = null;

		final String name = getName();
		final String title = "Browse " + name + " file path:";
		final File initialDirectory = getInitialDirectory();
		final FileChooser fileChooser =
				BasicControlsFactories.getInstance().createFileChooser(title, initialDirectory);

		if (extensionFilterList != null) {

			fileChooser.getExtensionFilters().addAll(extensionFilterList);

			final FileChooser.ExtensionFilter lastExtensionFilter =
					extensionFilterList.get(extensionFilterList.size() - 1);
			fileChooser.setSelectedExtensionFilter(lastExtensionFilter);
		}

		final File file;
		if (mode == Mode.OPEN) {
			file = fileChooser.showOpenDialog(getRoot().getScene().getWindow());
		} else if (mode == Mode.SAVE) {
			file = fileChooser.showSaveDialog(getRoot().getScene().getWindow());
		} else {
			file = null;
		}

		if (file != null) {
			pathString = file.getAbsolutePath();
		}

		return pathString;
	}

	@Override
	File getInitialDirectory() {

		File initialDirectory = null;
		final String pathString = getPathString();
		final String parentPathString = PathUtils.computeParentPathString(pathString);
		if (IoUtils.directoryExists(parentPathString)) {
			initialDirectory = new File(parentPathString);
		}
		if (initialDirectory == null) {
			initialDirectory = super.getInitialDirectory();
		}
		return initialDirectory;
	}
}
