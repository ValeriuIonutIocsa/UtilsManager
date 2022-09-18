package com.personal.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import com.personal.utils.gradle_roots.FactoryGradleRoot;
import com.personal.utils.gradle_roots.GradleRoot;
import com.personal.utils.mode.FactoryMode;
import com.personal.utils.mode.Mode;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.log.Logger;

final class AppStartUtilsManager {

	private AppStartUtilsManager() {
	}

	public static void main(
			final String[] args) {

		final Instant start = Instant.now();
		Logger.printProgress("starting UtilsManager");

		if (args.length < 2) {

			Logger.printError("insufficient arguments");
			System.exit(-1);
		}

		String modeName = args[0];
		final Mode mode = FactoryMode.computeInstance(modeName);
		if (mode == null) {

			Logger.printError("invalid mode: " + modeName);
			System.exit(-2);
		}

		String pathString = args[1];
		final Path path = PathUtils.tryParsePath("path", pathString).toAbsolutePath();
		if (!IoUtils.directoryExists(path)) {

			Logger.printError("invalid path:" +
					System.lineSeparator() + pathString);
			System.exit(-3);
		}

		modeName = mode.name();
		Logger.printLine("mode: " + modeName);

		Logger.printLine("path:");
		pathString = path.toString();
		Logger.printLine(pathString);

		final String rootFolderPathString = computeRootFolderPathStringRec(pathString);
		Logger.printLine("root folder path:");
		Logger.printLine(rootFolderPathString);

		final GradleRoot gradleRoot = FactoryGradleRoot.newInstance(rootFolderPathString);

		final String utilsRootPathString =
				Paths.get("C:\\IVI\\Prog\\JavaGradle\\UtilsManager").toString();
		final GradleRoot utilsGradleRoot = FactoryGradleRoot.newInstance(utilsRootPathString);

		if (mode == Mode.DOWNLOAD) {
			gradleRoot.synchronizeFrom(utilsGradleRoot);
		} else if (mode == Mode.UPLOAD) {
			utilsGradleRoot.synchronizeFrom(gradleRoot);
		}

		Logger.printFinishMessage(start);
	}

	private static String computeRootFolderPathStringRec(
			final String pathString) {

		final String rootFolderPathString;
		final Path commonBuildGradleFilePath = Paths.get(pathString, "common_build.gradle");
		if (IoUtils.fileExists(commonBuildGradleFilePath)) {
			rootFolderPathString = pathString;

		} else {
			final String folderPathString = PathUtils.computeFolderPathString(pathString);
			if (folderPathString != null) {
				rootFolderPathString = computeRootFolderPathStringRec(folderPathString);
			} else {
				rootFolderPathString = null;
			}
		}
		return rootFolderPathString;
	}
}
