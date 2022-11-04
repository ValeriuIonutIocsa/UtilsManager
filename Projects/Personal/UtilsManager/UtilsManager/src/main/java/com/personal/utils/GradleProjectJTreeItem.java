package com.personal.utils;

import com.utils.io.PathUtils;

public class GradleProjectJTreeItem {

	private final String projectPathString;

	GradleProjectJTreeItem(
			final String projectPathString) {

		this.projectPathString = projectPathString;
	}

	@Override
	public String toString() {
		return PathUtils.computeFileName(projectPathString);
	}

	public String getProjectPathString() {
		return projectPathString;
	}
}
