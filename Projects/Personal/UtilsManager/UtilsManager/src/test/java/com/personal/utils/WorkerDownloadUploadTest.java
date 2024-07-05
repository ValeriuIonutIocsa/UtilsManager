package com.personal.utils;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.personal.utils.mode.Mode;
import com.utils.string.StrUtils;

class WorkerDownloadUploadTest {

	@Test
	void testWork() {

		final Mode mode;
		final int inputMode = StrUtils.tryParsePositiveInt("1");
		if (inputMode == 1) {
			mode = Mode.DOWNLOAD;
		} else if (inputMode == 2) {
			mode = Mode.UPLOAD;
		} else {
			throw new RuntimeException();
		}

		final String pathString;
		final int inputPathString = StrUtils.tryParsePositiveInt("1");
		if (inputPathString == 1) {
			pathString = "C:\\IVI\\Prog\\JavaGradle\\Scripts\\General\\GradleCnfMan";

		} else {
			throw new RuntimeException();
		}

		final Instant start = Instant.now();

		WorkerDownloadUpload.work(mode, pathString, start);
	}
}
