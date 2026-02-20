package com.personal.utils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import com.personal.utils.gradle_roots.FactoryGradleRoot;
import com.personal.utils.gradle_roots.GradleRoot;
import com.personal.utils.mode.Mode;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.ReaderUtils;
import com.utils.log.Logger;

final class WorkerDownloadUpload {

	private WorkerDownloadUpload() {
	}

	static void work(
			final Mode mode,
			final String pathString,
			final Instant start) {

		final String rootFolderPathString = computeRootFolderPathStringRec(pathString);
		if (rootFolderPathString != null) {

			Logger.printLine("root folder path:");
			Logger.printLine(rootFolderPathString);

			final Boolean useIntranet = checkUseIntranet(pathString);

			final GradleRoot gradleRoot = FactoryGradleRoot.newInstance(rootFolderPathString, useIntranet);

			final GradleRoot utilsGradleRoot = FactoryGradleRoot.newInstanceUtils();

			if (mode == Mode.DOWNLOAD) {
				gradleRoot.synchronizeFrom(utilsGradleRoot);
			} else if (mode == Mode.UPLOAD) {
				utilsGradleRoot.synchronizeFrom(gradleRoot);
			} else {
				Logger.printError("unsupported mode: " + mode);
			}

		} else {
			Logger.printError("cannot find root folder for path:" +
					System.lineSeparator() + pathString);
		}

		Logger.printFinishMessage(start);
	}

	private static String computeRootFolderPathStringRec(
			final String pathString) {

		final String rootFolderPathString;
		final String commonBuildGradleFilePathString =
				PathUtils.computePath(pathString, "common_build.gradle");
		if (IoUtils.fileExists(commonBuildGradleFilePathString)) {
			rootFolderPathString = pathString;

		} else {
			final String folderPathString = PathUtils.computeParentPath(pathString);
			if (folderPathString != null) {
				rootFolderPathString = computeRootFolderPathStringRec(folderPathString);
			} else {
				rootFolderPathString = null;
			}
		}
		return rootFolderPathString;
	}

	private static Boolean checkUseIntranet(
			final String pathString) {

		Boolean useIntranet = null;
		final String buildGradleFilePathString = PathUtils.computePath(pathString, "build.gradle");
		if (IoUtils.fileExists(buildGradleFilePathString)) {

			final List<String> lineList =
					ReaderUtils.tryFileToLineList(buildGradleFilePathString, StandardCharsets.UTF_8);
			for (final String line : lineList) {

				if (line.contains("useIntranet = true")) {

					useIntranet = true;
					break;

				} else if (line.contains("useIntranet = false")) {

					useIntranet = false;
					break;
				}
			}
		}
		return useIntranet;
	}
}
