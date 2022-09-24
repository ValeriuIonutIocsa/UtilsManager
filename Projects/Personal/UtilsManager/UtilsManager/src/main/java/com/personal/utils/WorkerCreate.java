package com.personal.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.utils.io.PathUtils;
import com.utils.io.folder_copiers.FactoryFolderCopier;

class WorkerCreate {

	static void work(
			final String pathString) {

		final String projectName = PathUtils.computeFileName(pathString);

		final String templateProjectFolderPathString = "C:\\IVI\\Prog\\JavaGradle\\UtilsManager\\" +
				"Projects\\Personal\\UtilsManager\\UtilsManager_EXE\\TemplateProject";

		final Path projectFolderPath =
				Paths.get(pathString, "Projects", "Personal", projectName, projectName);

		final String allModulesProjectName = projectName + "AllModules";
		final Path allModulesProjectFolderPath =
				Paths.get(pathString, "Projects", "Personal", allModulesProjectName, allModulesProjectName);
		FactoryFolderCopier.getInstance().copyFolder(
				templateProjectFolderPathString, allModulesProjectFolderPath.toString());
	}
}
