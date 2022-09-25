package com.personal.utils;

import java.nio.file.Paths;
import java.time.Instant;

import com.personal.utils.gradle_roots.FactoryGradleRoot;
import com.personal.utils.gradle_roots.GradleRoot;
import com.personal.utils.mode.Mode;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.log.Logger;

class WorkerDownloadUpload {

	static void work(
			final Mode mode,
			final String pathString,
			final Instant start) {

		final String rootFolderPathString = computeRootFolderPathStringRec(pathString);
		Logger.printLine("root folder path:");
		Logger.printLine(rootFolderPathString);

		final GradleRoot gradleRoot = FactoryGradleRoot.newInstance(rootFolderPathString);

		final GradleRoot utilsGradleRoot = FactoryGradleRoot.newInstanceUtils();

		if (mode == Mode.DOWNLOAD) {
			gradleRoot.synchronizeFrom(utilsGradleRoot);
		} else if (mode == Mode.UPLOAD) {
			utilsGradleRoot.synchronizeFrom(gradleRoot);
		} else {
			Logger.printError("unsupported mode: " + mode);
		}

		Logger.printFinishMessage(start);
	}

	private static String computeRootFolderPathStringRec(
			final String pathString) {

		final String rootFolderPathString;
		final String commonBuildGradleFilePathString =
				Paths.get(pathString, "common_build.gradle").toString();
		if (IoUtils.fileExists(commonBuildGradleFilePathString)) {
			rootFolderPathString = pathString;

		} else {
			final String folderPathString = PathUtils.computeParentPathString(pathString);
			if (folderPathString != null) {
				rootFolderPathString = computeRootFolderPathStringRec(folderPathString);
			} else {
				rootFolderPathString = null;
			}
		}
		return rootFolderPathString;
	}
}
