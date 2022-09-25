package com.personal.utils.gradle_roots;

import java.nio.file.Paths;
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

		final String utilsRootPathString =
				Paths.get("C:\\IVI\\Prog\\JavaGradle\\UtilsManager").toString();
		return newInstance(utilsRootPathString);
	}

	public static GradleRoot newInstance(
			final String rootFolderPathString) {

		final String commonBuildGradleFilePathString =
				Paths.get(rootFolderPathString, "common_build.gradle").toString();
		final String commonSettingsGradleFilePathString =
				Paths.get(rootFolderPathString, "common_settings.gradle").toString();
		final String gitAttributesFilePathString =
				Paths.get(rootFolderPathString, ".gitattributes").toString();

		final Map<String, String> moduleFolderPathsByNameMap = new HashMap<>();
		final List<String> buildGradleFilePathStringList =
				ListFileUtils.listFilesRecursively(rootFolderPathString, filePath -> {

					final String fileName = PathUtils.computeFileName(filePath);
					return StringUtils.equalsIgnoreCase(fileName, "build.gradle");
				});
		for (final String buildGradleFilePathString : buildGradleFilePathStringList) {

			final String moduleFolderPathString =
					PathUtils.computeParentPathString(buildGradleFilePathString);
			final String moduleName = PathUtils.computeFileName(moduleFolderPathString);
			moduleFolderPathsByNameMap.put(moduleName, moduleFolderPathString);
		}

		return new GradleRoot(commonBuildGradleFilePathString, commonSettingsGradleFilePathString,
				gitAttributesFilePathString, moduleFolderPathsByNameMap);
	}
}
