package com.personal.utils;

import org.junit.jupiter.api.Test;

class WorkerCreateTest {

	@Test
	void testWork() {

		final String pathString;
		final String packageName;
		final int inputPathString = Integer.parseInt("1");
		if (inputPathString == 1) {
			pathString = "C:\\IVI\\Prog\\JavaGradle\\WeatherAnalyzer";
			packageName = "com.personal.wa";

		} else {
			throw new RuntimeException();
		}

		WorkerCreate.work(pathString, packageName);
	}
}
