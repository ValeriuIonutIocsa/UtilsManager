package com.personal.utils;

import java.time.Instant;

import org.apache.commons.lang3.StringUtils;

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

		Logger.setDebugMode(true);

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
		pathString = PathUtils.computeNormalizedPathString(pathString);
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

			final String packageName;
			if (args.length >= 3) {
				packageName = args[2];
			} else {
				packageName = null;
			}

			if (StringUtils.isBlank(packageName)) {

				Logger.printError("invalid package name: " + packageName);
				System.exit(-2);
			}

			WorkerCreate.work(pathString, packageName);

		} else {
			WorkerDownloadUpload.work(mode, pathString, start);
		}
	}
}
