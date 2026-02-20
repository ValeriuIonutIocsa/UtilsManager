package com.personal.utils.gradle_roots.patchers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.personal.utils.gradle_roots.GradleRoot;
import com.utils.io.PathUtils;
import com.utils.io.ReaderUtils;
import com.utils.io.WriterUtils;

public final class DoNotUseIntranetPatcher {

	private DoNotUseIntranetPatcher() {
	}

	public static void work(
			final GradleRoot gradleRoot) {

		final String commonBuildGradleFilePathString = gradleRoot.getCommonBuildGradleFilePathString();
		patchCommonBuildGradleFile(commonBuildGradleFilePathString);

		final Map<String, String> moduleFolderPathsByNameMap = gradleRoot.getModuleFolderPathsByNameMap();
		for (final String moduleFolderPathString : moduleFolderPathsByNameMap.values()) {

			final String buildGradleFilePathString =
					PathUtils.computePath(moduleFolderPathString, "build.gradle");
			patchBuildGradleFile(buildGradleFilePathString);

			final String gradleWrapperPropertiesFilePathString =
					PathUtils.computePath(moduleFolderPathString, "gradle", "wrapper", "gradle-wrapper.properties");
			patchGradleWrapperPropertiesFile(gradleWrapperPropertiesFilePathString);
		}
	}

	private static void patchCommonBuildGradleFile(
			final String commonBuildGradleFilePathString) {

		final String repositories = "repositories {" + System.lineSeparator() +
				"    mavenCentral()" + System.lineSeparator() +
				"}";

		final List<String> lineList =
				ReaderUtils.tryFileToLineList(commonBuildGradleFilePathString, StandardCharsets.UTF_8);
		final List<String> outputLineList = new ArrayList<>();
		boolean insideRepositoriesBlock = false;
		for (final String line : lineList) {

			if (!insideRepositoriesBlock) {

				if ("if (project.hasProperty('useIntranet') && useIntranet) {".equals(line)) {
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
			final String buildGradleFilePathString) {

		final List<String> lineList =
				ReaderUtils.tryFileToLineList(buildGradleFilePathString, StandardCharsets.UTF_8);
		final List<String> outputLineList = new ArrayList<>();
		for (final String line : lineList) {

			if (!line.contains("useIntranet = ")) {
				outputLineList.add(line);
			}
		}
		WriterUtils.tryLineListToFile(outputLineList, StandardCharsets.UTF_8, buildGradleFilePathString);
	}

	private static void patchGradleWrapperPropertiesFile(
			final String gradleWrapperPropertiesFilePathString) {

		final List<String> lineList =
				ReaderUtils.tryFileToLineList(gradleWrapperPropertiesFilePathString, StandardCharsets.UTF_8);
		final List<String> outputLineList = new ArrayList<>();
		for (final String line : lineList) {

			if (line.startsWith("distributionUrl=")) {

				final int lastIndexOf = line.lastIndexOf("/");
				final String suffix = line.substring(lastIndexOf + 1);
				final String distributionUrl = "https\\://services.gradle.org/distributions/";
				final String outputLine = "distributionUrl=" + distributionUrl + suffix;
				outputLineList.add(outputLine);

			} else {
				outputLineList.add(line);
			}
		}
		WriterUtils.tryLineListToFile(outputLineList, StandardCharsets.UTF_8, gradleWrapperPropertiesFilePathString);
	}
}
