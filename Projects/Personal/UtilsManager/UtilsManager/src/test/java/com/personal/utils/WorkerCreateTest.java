package com.personal.utils;

import org.junit.jupiter.api.Test;

import com.utils.string.StrUtils;

class WorkerCreateTest {

	@Test
	void testWork() {

		final String pathString;
		final String packageName;
		final int inputPathString = StrUtils.tryParsePositiveInt("2");
		if (inputPathString == 1) {
			pathString = "C:\\IVI\\Prog\\JavaGradle\\GeneratedTestProject";
			packageName = "com.personal.test_prj";

		} else if (inputPathString == 2) {
			pathString = "C:\\IVI\\Prog\\JavaGradle\\TestHttpsServer";
			packageName = "com.personal.https_test";

		} else {
			throw new RuntimeException();
		}

		WorkerCreate.work(pathString, packageName);
	}
}
