package com.utils.gui.objects.browse;

import java.io.File;

import com.utils.gui.factories.BasicControlsFactories;
import com.utils.io.IoUtils;

import javafx.stage.DirectoryChooser;

public class HBoxBrowsePathFolder extends HBoxBrowsePath {

	public HBoxBrowsePathFolder(
			final String name,
			final String initialValue,
			final String initialDirectoryPathString) {
		super(name, initialValue, initialDirectoryPathString);
	}

	@Override
	String browsePath() {

		String pathString = null;

		final String name = getName();
		final String title = "Browse " + name + " folder path:";
		final File initialDirectory = getInitialDirectory();
		final DirectoryChooser directoryChooser =
				BasicControlsFactories.getInstance().createDirectoryChooser(title, initialDirectory);

		final File file = directoryChooser.showDialog(getRoot().getScene().getWindow());
		if (file != null) {
			pathString = file.getAbsolutePath();
		}

		return pathString;
	}

	@Override
	File getInitialDirectory() {

		File initialDirectory = null;
		final String pathString = getPathString();
		if (IoUtils.directoryExists(pathString)) {
			initialDirectory = new File(pathString);
		}
		if (initialDirectory == null) {
			initialDirectory = super.getInitialDirectory();
		}
		return initialDirectory;
	}
}
