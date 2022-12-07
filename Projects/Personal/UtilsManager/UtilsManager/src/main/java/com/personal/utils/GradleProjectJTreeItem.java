package com.personal.utils;

public class GradleProjectJTreeItem {

	private final String projectName;
	private final String projectPathString;

	GradleProjectJTreeItem(
			final String projectName,
			final String projectPathString) {

		this.projectName = projectName;
		this.projectPathString = projectPathString;
	}

	@Override
	public String toString() {
		return projectName;
	}

	public String getProjectPathString() {
		return projectPathString;
	}
}
