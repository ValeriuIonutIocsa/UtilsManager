package com.personal.utils;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.personal.utils.gradle_roots.FactoryGradleRoot;
import com.personal.utils.gradle_roots.GradleRoot;
import com.utils.io.PathUtils;
import com.utils.io.ReaderUtils;
import com.utils.io.StreamUtils;
import com.utils.io.WriterUtils;
import com.utils.io.folder_copiers.FactoryFolderCopier;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;

final class WorkerCreate {

	private WorkerCreate() {
	}

	static void work(
			final String pathString,
			final String packageName) {

		final List<String> selectedTreePathList = JDialogCreate.display();

		final Set<String> selectedModuleNameSet = new HashSet<>();
		fillSelectedModuleNameSet(selectedTreePathList, selectedModuleNameSet);

		final GradleRoot utilsGradleRoot = FactoryGradleRoot.newInstanceUtils();

		final Map<String, String> moduleFolderPathsByNameMap = new HashMap<>();
		final List<String> moduleRelativePathStringList = new ArrayList<>();
		final String utilsRootFolderPathString = utilsGradleRoot.getRootFolderPathString();
		final Map<String, String> utilsModuleFolderPathsByNameMap =
				utilsGradleRoot.getModuleFolderPathsByNameMap();
		for (final String selectedModuleName : selectedModuleNameSet) {

			final String utilsModulePathString = utilsModuleFolderPathsByNameMap.get(selectedModuleName);
			final String moduleRelativePathString =
					PathUtils.computeRelativePath(utilsRootFolderPathString, utilsModulePathString);
			moduleRelativePathStringList.add(moduleRelativePathString);
			final String modulePathString = PathUtils.computePath(pathString, moduleRelativePathString);
			moduleFolderPathsByNameMap.put(selectedModuleName, modulePathString);
		}

		final GradleRoot gradleRoot = FactoryGradleRoot.newInstance(pathString, moduleFolderPathsByNameMap);
		gradleRoot.synchronizeFrom(utilsGradleRoot);

		final String projectName = PathUtils.computeFileName(pathString);

		final String templateProjectFolderPathString = "C:\\IVI\\Prog\\JavaGradle\\UtilsManager\\" +
				"Projects\\Personal\\UtilsManager\\UtilsManager_EXE\\TemplateProject";

		final String projectRelativePath = "/Projects/Personal/" + projectName + "/" + projectName;
		final String projectFolderPathString = PathUtils.computePath(pathString, projectRelativePath);
		FactoryFolderCopier.getInstance().copyFolder(
				templateProjectFolderPathString, projectFolderPathString, true);
		final String appInfo = createAppInfo(packageName);
		replaceDependencies(projectFolderPathString, appInfo, moduleRelativePathStringList);
		createMainClass(projectFolderPathString, projectName, packageName);

		final String allModulesProjectName = projectName + "AllModules";
		final String allModulesProjectRelativePath = "/Projects/Personal/" +
				allModulesProjectName + "/" + allModulesProjectName;
		final String allModulesProjectFolderPathString =
				PathUtils.computePath(pathString, allModulesProjectRelativePath);
		FactoryFolderCopier.getInstance().copyFolder(
				templateProjectFolderPathString, allModulesProjectFolderPathString, true);
		replaceDependencies(allModulesProjectFolderPathString, "", Collections.singletonList(projectRelativePath));
	}

	private static void fillSelectedModuleNameSet(
			final List<String> selectedTreePathList,
			final Set<String> selectedProjectNameSet) {

		for (final String selectedTreePath : selectedTreePathList) {

			String selectedProjectName;
			final int lastIndexOf = selectedTreePath.lastIndexOf('>');
			if (lastIndexOf >= 0) {
				selectedProjectName = selectedTreePath.substring(lastIndexOf + 1);
			} else {
				selectedProjectName = selectedTreePath;
			}
			selectedProjectNameSet.add(selectedProjectName);
		}

		Logger.printLine("selected project names: " + selectedProjectNameSet);
	}

	private static String createAppInfo(
			final String packageName) {

		return "    mainClass = '" + packageName + ".AppStart' + project.name" + System.lineSeparator() +
				"    projectVersion = '0.0.1'" + System.lineSeparator() + System.lineSeparator();
	}

	private static void replaceDependencies(
			final String projectFolderPathString,
			final String appInfo,
			final List<String> subProjectRelativePathStringList) {

		final String buildGradleFilePathString =
				PathUtils.computePath(projectFolderPathString, "build.gradle");
		try {
			final String subProjects;
			if (subProjectRelativePathStringList.isEmpty()) {
				subProjects = "[]";

			} else {
				final StringBuilder sbSubProjects = new StringBuilder("[");
				for (int i = 0; i < subProjectRelativePathStringList.size(); i++) {

					final String subProjectRelativePathString = subProjectRelativePathStringList.get(i);
					sbSubProjects.append(System.lineSeparator()).append("            ");
					final String subProjectName = PathUtils.computeFileName(subProjectRelativePathString);
					sbSubProjects.append("':").append(subProjectName).append('\'');
					if (i < subProjectRelativePathStringList.size() - 1) {
						sbSubProjects.append(',');
					}
				}
				sbSubProjects.append(System.lineSeparator()).append("    ]");
				subProjects = sbSubProjects.toString();
			}

			String buildGradleContents =
					ReaderUtils.fileToString(buildGradleFilePathString, StandardCharsets.UTF_8);
			buildGradleContents = StringUtils.replace(buildGradleContents,
					"%%APP_INFO%%", appInfo);
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
				PathUtils.computePath(projectFolderPathString, "settings.gradle");
		try {
			final String subProjectPaths;
			if (subProjectRelativePathStringList.isEmpty()) {
				subProjectPaths = "[]";

			} else {
				final StringBuilder sbSubProjectPaths = new StringBuilder("[");
				for (int i = 0; i < subProjectRelativePathStringList.size(); i++) {

					final String subProjectRelativePathString = subProjectRelativePathStringList.get(i);
					sbSubProjectPaths.append(System.lineSeparator()).append("            ")
							.append('\'').append(subProjectRelativePathString).append('\'');
					if (i < subProjectRelativePathStringList.size() - 1) {
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

	private static void createMainClass(
			final String projectFolderPathString,
			final String projectName,
			final String packageName) {

		String mainClassPathString =
				PathUtils.computePath(projectFolderPathString, "src", "main", "java");
		final String[] packageNameSplitPartArray = StringUtils.split(packageName, '.');
		for (final String packageNameSplitPart : packageNameSplitPartArray) {
			mainClassPathString = PathUtils.computePath(mainClassPathString, packageNameSplitPart);
		}
		mainClassPathString = PathUtils.computePath(mainClassPathString, "AppStart" + projectName + ".java");

		final boolean success = FactoryFolderCreator.getInstance()
				.createParentDirectories(mainClassPathString, true);
		if (success) {

			try (PrintStream printStream = StreamUtils.openPrintStream(mainClassPathString)) {

				printStream.print("package ");
				printStream.print(packageName);
				printStream.print(';');
				printStream.println();

				printStream.println();

				printStream.print("final class AppStart");
				printStream.print(projectName);
				printStream.print(" {");
				printStream.println();

				printStream.println();

				printStream.print("\tprivate AppStart");
				printStream.print(projectName);
				printStream.print("() {");
				printStream.println();

				printStream.print("\t}");
				printStream.println();

				printStream.println();

				printStream.print("\tpublic static void main(");
				printStream.println();

				printStream.print("\t\t\tfinal String[] args) {");
				printStream.println();

				printStream.println();

				printStream.print("\t}");
				printStream.println();

				printStream.print("}");
				printStream.println();

			} catch (final Exception exc) {
				Logger.printError("failed to write the main class file");
				Logger.printException(exc);
			}
		}
	}
}
