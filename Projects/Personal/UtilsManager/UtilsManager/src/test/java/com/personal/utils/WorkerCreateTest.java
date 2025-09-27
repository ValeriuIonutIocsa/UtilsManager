package com.personal.utils;

import org.junit.jupiter.api.Test;

import com.utils.string.StrUtils;

class WorkerCreateTest {

	@Test
	void testWork() {

		final String pathString;
		final String projectType;
		final String packageName;
		final int inputPathString = StrUtils.tryParsePositiveInt("1");
		if (inputPathString == 1) {
			pathString = "C:\\IVI\\Prog\\JavaGradle\\GeneratedTestProject";
			projectType = "TestPrj";
			packageName = "com.personal.test_prj";

		} else if (inputPathString == 2) {
			pathString = "C:\\IVI\\Prog\\JavaGradle\\TestHttpsServer";
			projectType = "Personal";
			packageName = "com.personal.https_test";

		} else {
			throw new RuntimeException();
		}

		WorkerCreate.work(pathString, projectType, packageName);
	}
}
