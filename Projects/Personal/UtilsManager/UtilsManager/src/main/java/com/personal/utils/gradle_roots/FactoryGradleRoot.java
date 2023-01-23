package com.personal.utils.gradle_roots;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.utils.io.ListFileUtils;
import com.utils.io.PathUtils;

public final class FactoryGradleRoot {

	private FactoryGradleRoot() {
	}

	public static GradleRoot newInstanceUtils() {

		final String utilsRootPathString = createUtilsRootPathString();
		return newInstance(utilsRootPathString);
	}

	public static String createUtilsRootPathString() {
		return "C:\\IVI\\Prog\\JavaGradle\\UtilsManager";
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
		final List<String> buildGradleFilePathStringList =
				ListFileUtils.listFilesRecursively(rootFolderPathString, filePath -> {

					final String fileName = PathUtils.computeFileName(filePath);
					return StringUtils.equalsIgnoreCase(fileName, "build.gradle");
				});
		for (final String buildGradleFilePathString : buildGradleFilePathStringList) {

			final String moduleFolderPathString =
					PathUtils.computeParentPath(buildGradleFilePathString);
			final String moduleName = PathUtils.computeFileName(moduleFolderPathString);
			moduleFolderPathsByNameMap.put(moduleName, moduleFolderPathString);
		}

		return new GradleRoot(rootFolderPathString,
				commonBuildGradleFilePathString, commonSettingsGradleFilePathString,
				gitAttributesFilePathString, moduleFolderPathsByNameMap);
	}

	public static GradleRoot newInstance(
			final String rootFolderPathString,
			final Map<String, String> moduleFolderPathsByNameMap) {

		final String commonBuildGradleFilePathString =
				PathUtils.computePath(rootFolderPathString, "common_build.gradle");
		final String commonSettingsGradleFilePathString =
				PathUtils.computePath(rootFolderPathString, "common_settings.gradle");
		final String gitAttributesFilePathString =
				PathUtils.computePath(rootFolderPathString, ".gitattributes");

		return new GradleRoot(rootFolderPathString,
				commonBuildGradleFilePathString, commonSettingsGradleFilePathString,
				gitAttributesFilePathString, moduleFolderPathsByNameMap);
	}
}
