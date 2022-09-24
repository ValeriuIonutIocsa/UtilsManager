package com.personal.utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.personal.utils.gradle_roots.FactoryGradleRoot;
import com.personal.utils.gradle_roots.GradleRoot;
import com.utils.io.PathUtils;
import com.utils.io.ReaderUtils;
import com.utils.io.WriterUtils;
import com.utils.io.file_copiers.FactoryFileCopier;
import com.utils.io.folder_copiers.FactoryFolderCopier;
import com.utils.log.Logger;

class WorkerCreate {

	static void work(
			final String pathString) {

		copyRootFiles(pathString);

		final String projectName = PathUtils.computeFileName(pathString);

		final String templateProjectFolderPathString = "C:\\IVI\\Prog\\JavaGradle\\UtilsManager\\" +
				"Projects\\Personal\\UtilsManager\\UtilsManager_EXE\\TemplateProject";

		final String projectRelativePath = "/Projects/Personal/" + projectName + "/" + projectName;
		final String projectFolderPathString = Paths.get(pathString, projectRelativePath).toString();
		FactoryFolderCopier.getInstance().copyFolder(
				templateProjectFolderPathString, projectFolderPathString, true);
		replaceDependencies(projectFolderPathString, new ArrayList<>());

		final String allModulesProjectName = projectName + "AllModules";
		final String allModulesProjectRelativePath = "/Projects/Personal/" +
				allModulesProjectName + "/" + allModulesProjectName;
		final String allModulesProjectFolderPathString =
				Paths.get(pathString, allModulesProjectRelativePath).toString();
		FactoryFolderCopier.getInstance().copyFolder(
				templateProjectFolderPathString, allModulesProjectFolderPathString, true);
		replaceDependencies(allModulesProjectFolderPathString, Collections.singletonList(projectRelativePath));
	}

	private static void copyRootFiles(
			final String pathString) {

		final GradleRoot utilsGradleRoot = FactoryGradleRoot.newInstanceUtils();

		final String commonBuildGradleFilePathString = utilsGradleRoot.getCommonBuildGradleFilePathString();
		copyRootFile(commonBuildGradleFilePathString, pathString);
		final String commonSettingsGradleFilePathString = utilsGradleRoot.getCommonSettingsGradleFilePathString();
		copyRootFile(commonSettingsGradleFilePathString, pathString);
		final String gitAttributesFilePathString = utilsGradleRoot.getGitAttributesFilePathString();
		copyRootFile(gitAttributesFilePathString, pathString);
	}

	private static void copyRootFile(
			final String rootFilePathString,
			final String pathString) {

		final String rootFileName = PathUtils.computeFileName(rootFilePathString);
		final String dstRootFilePathString = Paths.get(pathString, rootFileName).toString();
		FactoryFileCopier.getInstance().copyFile(rootFilePathString, dstRootFilePathString, true, true);
	}

	private static void replaceDependencies(
			final String projectFolderPathString,
			final List<String> subProjectPathList) {

		final String buildGradleFilePathString =
				Paths.get(projectFolderPathString, "build.gradle").toString();
		try {
			final String subProjects;
			if (subProjectPathList.isEmpty()) {
				subProjects = "[]";

			} else {
				final StringBuilder sbSubProjects = new StringBuilder("[");
				for (int i = 0; i < subProjectPathList.size(); i++) {

					final String subProjectPath = subProjectPathList.get(i);
					sbSubProjects.append(System.lineSeparator()).append("            ");
					final String subProjectName = PathUtils.computeFileName(subProjectPath);
					sbSubProjects.append("':").append(subProjectName).append('\'');
					if (i < subProjectPathList.size() - 1) {
						sbSubProjects.append(',');
					}
				}
				sbSubProjects.append(System.lineSeparator()).append("    ]");
				subProjects = sbSubProjects.toString();
			}

			String buildGradleContents =
					ReaderUtils.fileToString(buildGradleFilePathString, StandardCharsets.UTF_8);
			buildGradleContents = StringUtils.replace(buildGradleContents,
					"%%SUB_PROJECTS%%", subProjects);
			WriterUtils.stringToFile(buildGradleContents, StandardCharsets.UTF_8,
					buildGradleFilePathString);

		} catch (final Exception exc) {
			Logger.printError("failed to replace dependencies in file:" +
					System.lineSeparator() + buildGradleFilePathString);
			Logger.printException(exc);
		}

		final String settingsGradleFilePathString =
				Paths.get(projectFolderPathString, "settings.gradle").toString();
		try {
			final String subProjectPaths;
			if (subProjectPathList.isEmpty()) {
				subProjectPaths = "[]";

			} else {
				final StringBuilder sbSubProjectPaths = new StringBuilder("[");
				for (int i = 0; i < subProjectPathList.size(); i++) {

					final String subProjectPath = subProjectPathList.get(i);
					sbSubProjectPaths.append(System.lineSeparator()).append("            ")
							.append('\'').append(subProjectPath).append('\'');
					if (i < subProjectPathList.size() - 1) {
						sbSubProjectPaths.append(',');
					}
				}
				sbSubProjectPaths.append(System.lineSeparator()).append("    ]");
				subProjectPaths = sbSubProjectPaths.toString();
			}

			String settingsGradleContents =
					ReaderUtils.fileToString(settingsGradleFilePathString, StandardCharsets.UTF_8);
			settingsGradleContents = StringUtils.replace(settingsGradleContents,
					"%%SUB_PROJECT_PATHS%%", subProjectPaths);
			WriterUtils.stringToFile(settingsGradleContents, StandardCharsets.UTF_8,
					settingsGradleFilePathString);

		} catch (final Exception exc) {
			Logger.printError("failed to replace dependencies in file:" +
					System.lineSeparator() + settingsGradleFilePathString);
			Logger.printException(exc);
		}
	}
}
