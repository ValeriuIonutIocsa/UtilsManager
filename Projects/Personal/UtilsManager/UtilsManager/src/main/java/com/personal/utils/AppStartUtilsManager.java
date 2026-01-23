package com.personal.utils;

import java.time.Instant;

import org.apache.commons.lang3.StringUtils;

import com.personal.utils.app_info.FactoryAppInfoUtilsManager;
import com.personal.utils.mode.FactoryMode;
import com.personal.utils.mode.Mode;
import com.utils.app_info.AppInfo;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.log.Logger;

final class AppStartUtilsManager {

	private AppStartUtilsManager() {
	}

	public static void main(
			final String[] args) {

		Logger.setDebugMode(true);

		final Instant start = Instant.now();

		final AppInfo appInfo = FactoryAppInfoUtilsManager.computeInstance();
		final String appStartMessage = appInfo.createAppStartMessage();
		Logger.printProgress(appStartMessage);

		if (args.length < 2) {

			Logger.printError("insufficient arguments" + System.lineSeparator() + System.lineSeparator() +
					"supported modes examples:" + System.lineSeparator() +
					"utils_manager download ." + System.lineSeparator() +
					"utils_manager upload ." + System.lineSeparator() +
					"utils_manager create . Personal com.personal.package_name");
			System.exit(-1);
		}

		String modeName = args[0];
		final Mode mode = FactoryMode.computeInstance(modeName);
		if (mode == null) {

			Logger.printError("invalid mode: " + modeName);
			System.exit(-2);
		}

		String pathString = args[1];
		pathString = PathUtils.computeNormalizedPath(null, pathString);
		if (!IoUtils.directoryExists(pathString)) {

			Logger.printError("invalid path:" +
					System.lineSeparator() + pathString);
			System.exit(-3);
		}

		modeName = mode.name();
		Logger.printLine("mode: " + modeName);

		Logger.printLine("path:");
		Logger.printLine(pathString);

		if (mode == Mode.CREATE) {

			String projectType = null;
			String packageName = null;
			if (args.length >= 3) {

				projectType = args[2];
				packageName = args[3];
			}
			if (StringUtils.isBlank(projectType)) {

				Logger.printError("invalid project type: " + projectType);
				System.exit(-2);
			}
			if (StringUtils.isBlank(packageName)) {

				Logger.printError("invalid package name: " + packageName);
				System.exit(-3);
			}

			Logger.printLine("project type: " + projectType);
			Logger.printLine("package name: " + packageName);

			WorkerCreate.work(pathString, projectType, packageName);

		} else {
			WorkerDownloadUpload.work(mode, pathString, start);
		}
	}
}
