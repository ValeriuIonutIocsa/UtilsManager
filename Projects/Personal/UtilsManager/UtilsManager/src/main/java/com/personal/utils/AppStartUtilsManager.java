package com.personal.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

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

		if (args.length < 2) {

			Logger.printError("insufficient arguments");
			System.exit(-1);
		}

		final String modeName = args[0];
		final Mode mode = FactoryMode.computeInstance(modeName);
		if (mode == null) {

			Logger.printError("invalid mode");
			System.exit(-2);
		}

		final String pathString = args[1];
		final String rootFolderPathString = computeRootFolderPathStringRec(pathString);
		final GradleRoot gradleRoot = FactoryGradleRoot.newInstance(rootFolderPathString);

		final String utilsRootPathString = Paths.get("C:\\IVI\\Prog\\JavaGradle\\UtilsManager").toString();
		final GradleRoot utilsGradleRoot = FactoryGradleRoot.newInstance(utilsRootPathString);

		if (mode == Mode.DOWNLOAD) {
			gradleRoot.synchronizeFrom(utilsGradleRoot);
		} else if (mode == Mode.UPLOAD) {
			utilsGradleRoot.synchronizeFrom(gradleRoot);
		}
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
