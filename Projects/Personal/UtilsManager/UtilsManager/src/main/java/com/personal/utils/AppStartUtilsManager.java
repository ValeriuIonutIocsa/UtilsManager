package com.personal.utils;

import java.time.Instant;

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
		pathString = PathUtils.tryParsePath("path", pathString).toAbsolutePath().normalize().toString();
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
			WorkerCreate.work(pathString);
		} else {
			WorkerDownloadUpload.work(mode, pathString, start);
		}
	}
}
