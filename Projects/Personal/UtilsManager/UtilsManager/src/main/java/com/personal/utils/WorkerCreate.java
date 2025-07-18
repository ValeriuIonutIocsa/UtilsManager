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
import org.apache.commons.lang3.Strings;

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

		final Set<String> selectedModuleNameSet = new HashSet<>();
		final Set<String> selectedModuleNameWoDepsSet = new HashSet<>();
		JDialogCreate.display(selectedModuleNameSet, selectedModuleNameWoDepsSet);

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
			if (selectedModuleNameWoDepsSet.contains(selectedModuleName)) {
				moduleRelativePathStringList.add(moduleRelativePathString);
			}

			final String modulePathString = PathUtils.computePath(pathString, moduleRelativePathString);
			moduleFolderPathsByNameMap.put(selectedModuleName, modulePathString);
		}

		final List<String> subProjectRelativePathStringList = new ArrayList<>();
		for (final String moduleRelativePathString : moduleRelativePathStringList) {

			final String subProjectRelativePathString =
					"/" + Strings.CS.replace(moduleRelativePathString, "\\", "/");
			subProjectRelativePathStringList.add(subProjectRelativePathString);
		}

		final String projectName = PathUtils.computeFileName(pathString);

		final String utilsRootPathString = FactoryGradleRoot.createUtilsRootPathString();
		final String templateProjectFolderPathString = PathUtils.computePath(utilsRootPathString,
				"Projects", "Personal", "UtilsManager", "UtilsManager_EXE", "TemplateProject");

		final String projectRelativePath = "/Projects/Personal/" + projectName + "/" + projectName;
		final String projectFolderPathString = PathUtils.computePath(pathString, projectRelativePath);
		FactoryFolderCopier.getInstance().copyFolder(
				templateProjectFolderPathString, projectFolderPathString, true, true, true);
		final String appInfo = createAppInfo(packageName);
		replaceDependencies(projectFolderPathString, appInfo, subProjectRelativePathStringList);
		createMainClass(projectFolderPathString, projectName, packageName);
		createTestMainClass(projectFolderPathString, projectName, packageName);

		final String allModulesProjectName = projectName + "AllModules";
		final String allModulesProjectRelativePath = "/Projects/Personal/" +
				allModulesProjectName + "/" + allModulesProjectName;
		final String allModulesProjectFolderPathString =
				PathUtils.computePath(pathString, allModulesProjectRelativePath);
		FactoryFolderCopier.getInstance().copyFolder(
				templateProjectFolderPathString, allModulesProjectFolderPathString, true, true, true);
		replaceDependencies(allModulesProjectFolderPathString, "", Collections.singletonList(projectRelativePath));

		final GradleRoot gradleRoot = FactoryGradleRoot.newInstance(pathString,
				allModulesProjectFolderPathString, moduleFolderPathsByNameMap);
		gradleRoot.synchronizeFrom(utilsGradleRoot);
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
			buildGradleContents = Strings.CS.replace(buildGradleContents,
					"%%APP_INFO%%", appInfo);
			buildGradleContents = Strings.CS.replace(buildGradleContents,
					"%%SUB_PROJECTS%%", subProjects);
			WriterUtils.stringToFile(buildGradleContents, StandardCharsets.UTF_8,
					buildGradleFilePathString);

		} catch (final Throwable throwable) {
			Logger.printError("failed to replace dependencies in file:" +
					System.lineSeparator() + buildGradleFilePathString);
			Logger.printThrowable(throwable);
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
			settingsGradleContents = Strings.CS.replace(settingsGradleContents,
					"%%SUB_PROJECT_PATHS%%", subProjectPaths);
			WriterUtils.stringToFile(settingsGradleContents, StandardCharsets.UTF_8,
					settingsGradleFilePathString);

		} catch (final Throwable throwable) {
			Logger.printError("failed to replace dependencies in file:" +
					System.lineSeparator() + settingsGradleFilePathString);
			Logger.printThrowable(throwable);
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

		Logger.printProgress("writing main class file:" +
				System.lineSeparator() + mainClassPathString);

		final boolean success = FactoryFolderCreator.getInstance()
				.createParentDirectories(mainClassPathString, false, true);
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

				printStream.print("        System.out.println(\"--> starting ");
				printStream.print(projectName);
				printStream.print("\");");
				printStream.println();

				printStream.print("\t}");
				printStream.println();

				printStream.print("}");
				printStream.println();

			} catch (final Throwable throwable) {
				Logger.printError("failed to write the main class file");
				Logger.printThrowable(throwable);
			}
		}
	}

	private static void createTestMainClass(
			final String projectFolderPathString,
			final String projectName,
			final String packageName) {

		String testMainClassPathString =
				PathUtils.computePath(projectFolderPathString, "src", "test", "java");
		final String[] packageNameSplitPartArray = StringUtils.split(packageName, '.');
		for (final String packageNameSplitPart : packageNameSplitPartArray) {
			testMainClassPathString = PathUtils.computePath(testMainClassPathString, packageNameSplitPart);
		}
		testMainClassPathString = PathUtils.computePath(testMainClassPathString,
				"AppStart" + projectName + "Test.java");

		Logger.printProgress("writing main test class file:" +
				System.lineSeparator() + testMainClassPathString);

		final boolean success = FactoryFolderCreator.getInstance()
				.createParentDirectories(testMainClassPathString, false, true);
		if (success) {

			try (PrintStream printStream = StreamUtils.openPrintStream(testMainClassPathString)) {

				printStream.print("package ");
				printStream.print(packageName);
				printStream.print(';');
				printStream.println();

				printStream.println();

				printStream.print("import org.junit.jupiter.api.Test;");
				printStream.println();

				printStream.println();

				printStream.print("import com.utils.test.TestInputUtils;");
				printStream.println();

				printStream.println();

				printStream.print("class AppStart");
				printStream.print(projectName);
				printStream.print("Test {");
				printStream.println();

				printStream.println();

				printStream.print("\t@Test");
				printStream.println();

				printStream.print("\tvoid testMain() {");
				printStream.println();

				printStream.println();

				printStream.print("\t\tfinal String[] args;");
				printStream.println();

				printStream.print("\t\tfinal int input = TestInputUtils.parseTestInputNumber(\"1\");");
				printStream.println();

				printStream.print("\t\tif (input == 1) {");
				printStream.println();

				printStream.print("\t\t\targs = new String[] {};");
				printStream.println();

				printStream.print("\t\t} else {");
				printStream.println();

				printStream.print("\t\t\tthrow new RuntimeException();");
				printStream.println();

				printStream.print("\t\t}");
				printStream.println();

				printStream.println();

				printStream.print("\t\tAppStart");
				printStream.print(projectName);
				printStream.print(".main(args);");
				printStream.println();

				printStream.print("\t}");
				printStream.println();

				printStream.print("}");
				printStream.println();

			} catch (final Throwable throwable) {
				Logger.printError("failed to write the test main class file");
				Logger.printThrowable(throwable);
			}
		}
	}
}
