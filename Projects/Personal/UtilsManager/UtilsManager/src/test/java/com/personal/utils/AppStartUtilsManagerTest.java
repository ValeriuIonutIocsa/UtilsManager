package com.personal.utils;

import org.junit.jupiter.api.Test;

import com.utils.string.StrUtils;

class AppStartUtilsManagerTest {

	@Test
	void testMain() {

		final String[] args;
		final int input = StrUtils.tryParsePositiveInt("101");
		if (input == 101) {
			args = new String[] {};
		} else {
			throw new RuntimeException();
		}

		AppStartUtilsManager.main(args);
	}
}
