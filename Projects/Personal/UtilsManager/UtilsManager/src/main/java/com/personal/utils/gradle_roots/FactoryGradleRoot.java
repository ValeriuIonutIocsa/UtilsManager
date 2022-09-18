package com.personal.utils.gradle_roots;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.utils.io.IoUtils;
import com.utils.io.PathUtils;

public final class FactoryGradleRoot {

	private FactoryGradleRoot() {
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
		final Path rootFolderPath = Paths.get(rootFolderPathString);
		final List<Path> buildGradleFilePathList = IoUtils.listFilesRecursively(rootFolderPath, filePath -> {
			final String fileName = PathUtils.computeFileName(filePath);
			return StringUtils.equalsIgnoreCase(fileName, "build.gradle");
		});
		for (final Path buildGradleFilePath : buildGradleFilePathList) {

			final Path moduleFolderPath = buildGradleFilePath.getParent();
			final String moduleName = PathUtils.computeFileName(moduleFolderPath);
			final String moduleFolderPathString = moduleFolderPath.toString();
			moduleFolderPathsByNameMap.put(moduleName, moduleFolderPathString);
		}

		return new GradleRoot(commonBuildGradleFilePathString, commonSettingsGradleFilePathString,
				gitAttributesFilePathString, moduleFolderPathsByNameMap);
	}
}
