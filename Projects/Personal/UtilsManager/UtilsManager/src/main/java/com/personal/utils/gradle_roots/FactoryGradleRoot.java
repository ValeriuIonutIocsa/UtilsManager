package com.personal.utils.gradle_roots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.utils.io.ListFileUtils;
import com.utils.io.PathUtils;
import com.utils.log.Logger;

public final class FactoryGradleRoot {

	private FactoryGradleRoot() {
	}

	public static GradleRoot newInstanceUtils() {

		final String utilsRootPathString = createUtilsRootPathString();
		return newInstance(utilsRootPathString);
	}

	public static String createUtilsRootPathString() {
		return "C:\\IVI\\Prog\\JavaGradle\\Scripts\\General\\UtilsManager";
	}

	public static GradleRoot newInstance(
			final String rootFolderPathString) {

		final String commonBuildGradleFilePathString =
				PathUtils.computePath(rootFolderPathString, "common_build.gradle");
		final String commonSettingsGradleFilePathString =
				PathUtils.computePath(rootFolderPathString, "common_settings.gradle");
		final String gitAttributesFilePathString =
				PathUtils.computePath(rootFolderPathString, ".gitattributes");

		final Map<String, String> moduleFolderPathsByNameMap = new HashMap<>();
		final List<String> buildGradleFilePathStringList = new ArrayList<>();
		ListFileUtils.visitFilesRecursively(rootFolderPathString,
				dirPath -> {
				}, filePath -> {

					final String fileName = PathUtils.computeFileName(filePath);
					if (StringUtils.equalsIgnoreCase(fileName, "build.gradle")) {

						final String filePathString = filePath.toString();
						buildGradleFilePathStringList.add(filePathString);
					}
				});
		for (final String buildGradleFilePathString : buildGradleFilePathStringList) {

			final String moduleFolderPathString =
					PathUtils.computeParentPath(buildGradleFilePathString);
			final String moduleName = PathUtils.computeFileName(moduleFolderPathString);
			moduleFolderPathsByNameMap.put(moduleName, moduleFolderPathString);
		}

		final String allModulesFolderPathString = createAllModulesFolderPathString(
				rootFolderPathString, moduleFolderPathsByNameMap);

		return new GradleRoot(rootFolderPathString,
				commonBuildGradleFilePathString, commonSettingsGradleFilePathString,
				gitAttributesFilePathString, allModulesFolderPathString, moduleFolderPathsByNameMap);
	}

	private static String createAllModulesFolderPathString(
			final String rootFolderPathString,
			final Map<String, String> moduleFolderPathsByNameMap) {

		String allModulesPathString = null;
		for (final Map.Entry<String, String> mapEntry : moduleFolderPathsByNameMap.entrySet()) {

			final String moduleName = mapEntry.getKey();
			if (moduleName.endsWith("AllModules")) {

				allModulesPathString = mapEntry.getValue();
				break;
			}
		}

		if (allModulesPathString == null) {

			final String rootModuleName = PathUtils.computeFileName(rootFolderPathString);
			for (final Map.Entry<String, String> mapEntry : moduleFolderPathsByNameMap.entrySet()) {

				final String moduleName = mapEntry.getKey();
				if (rootModuleName.equals(moduleName)) {

					allModulesPathString = mapEntry.getValue();
					break;
				}
			}
		}

		if (allModulesPathString == null) {
			Logger.printError("could not compute " +
					"all modules folder path for project " + rootFolderPathString);
		}

		return allModulesPathString;
	}

	public static GradleRoot newInstance(
			final String rootFolderPathString,
			final String allModulesProjectFolderPathString,
			final Map<String, String> moduleFolderPathsByNameMap) {

		final String commonBuildGradleFilePathString =
				PathUtils.computePath(rootFolderPathString, "common_build.gradle");
		final String commonSettingsGradleFilePathString =
				PathUtils.computePath(rootFolderPathString, "common_settings.gradle");
		final String gitAttributesFilePathString =
				PathUtils.computePath(rootFolderPathString, ".gitattributes");

		return new GradleRoot(rootFolderPathString,
				commonBuildGradleFilePathString, commonSettingsGradleFilePathString,
				gitAttributesFilePathString, allModulesProjectFolderPathString, moduleFolderPathsByNameMap);
	}
}
