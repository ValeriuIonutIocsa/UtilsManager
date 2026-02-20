package com.personal.utils.gradle_roots.patchers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.personal.utils.gradle_roots.GradleRoot;
import com.utils.io.PathUtils;
import com.utils.io.ReaderUtils;
import com.utils.io.WriterUtils;

public final class UseIntranetPatcher {

	private UseIntranetPatcher() {
	}

	public static void work(
			final GradleRoot gradleRoot,
			final boolean useIntranet) {

		String repositories = ReaderUtils.tryFileToString(
				"C:\\IVI_WORK\\Apps\\Artifactory\\repositories.txt", StandardCharsets.UTF_8);
		repositories = repositories.trim();

		String distributionUrl = ReaderUtils.tryFileToString(
				"C:\\IVI_WORK\\Apps\\Artifactory\\distribution_url.txt", StandardCharsets.UTF_8);
		distributionUrl = distributionUrl.trim();

		final String commonBuildGradleFilePathString = gradleRoot.getCommonBuildGradleFilePathString();
		patchCommonBuildGradleFile(commonBuildGradleFilePathString, repositories);

		final Map<String, String> moduleFolderPathsByNameMap = gradleRoot.getModuleFolderPathsByNameMap();
		for (final String moduleFolderPathString : moduleFolderPathsByNameMap.values()) {

			final String buildGradleFilePathString =
					PathUtils.computePath(moduleFolderPathString, "build.gradle");
			patchBuildGradleFile(buildGradleFilePathString, useIntranet);

			final String gradleWrapperPropertiesFilePathString =
					PathUtils.computePath(moduleFolderPathString, "gradle", "wrapper", "gradle-wrapper.properties");
			patchGradleWrapperPropertiesFile(gradleWrapperPropertiesFilePathString, distributionUrl);
		}
	}

	private static void patchCommonBuildGradleFile(
			final String commonBuildGradleFilePathString,
			final String repositories) {

		final List<String> lineList =
				ReaderUtils.tryFileToLineList(commonBuildGradleFilePathString, StandardCharsets.UTF_8);
		final List<String> outputLineList = new ArrayList<>();
		boolean insideRepositoriesBlock = false;
		for (final String line : lineList) {

			if (!insideRepositoriesBlock) {

				if ("repositories {".equals(line)) {
					insideRepositoriesBlock = true;
					outputLineList.add(repositories);

				} else {
					outputLineList.add(line);
				}

			} else {
				if ("}".equals(line)) {
					insideRepositoriesBlock = false;
				}
			}
		}
		WriterUtils.tryLineListToFile(outputLineList, StandardCharsets.UTF_8, commonBuildGradleFilePathString);
	}

	private static void patchBuildGradleFile(
			final String buildGradleFilePathString,
			final boolean useIntranet) {

		final List<String> lineList =
				ReaderUtils.tryFileToLineList(buildGradleFilePathString, StandardCharsets.UTF_8);
		final List<String> outputLineList = new ArrayList<>();
		for (final String line : lineList) {

			if (!line.contains("useIntranet = ")) {

				outputLineList.add(line);
				if (line.contains("ext {")) {
					outputLineList.add("    useIntranet = " + useIntranet);
				}
			}
		}
		WriterUtils.tryLineListToFile(outputLineList, StandardCharsets.UTF_8, buildGradleFilePathString);
	}

	private static void patchGradleWrapperPropertiesFile(
			final String gradleWrapperPropertiesFilePathString,
			final String distributionUrl) {

		final List<String> lineList =
				ReaderUtils.tryFileToLineList(gradleWrapperPropertiesFilePathString, StandardCharsets.UTF_8);
		final List<String> outputLineList = new ArrayList<>();
		for (final String line : lineList) {

			if (line.startsWith("distributionUrl=")) {

				final int lastIndexOf = line.lastIndexOf("/");
				final String suffix = line.substring(lastIndexOf + 1);
				final String outputLine = "distributionUrl=" + distributionUrl + suffix;
				outputLineList.add(outputLine);

			} else {
				outputLineList.add(line);
			}
		}
		WriterUtils.tryLineListToFile(outputLineList, StandardCharsets.UTF_8, gradleWrapperPropertiesFilePathString);
	}
}
