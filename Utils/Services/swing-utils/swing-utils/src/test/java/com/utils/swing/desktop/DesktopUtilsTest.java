package com.utils.swing.desktop;

import java.io.File;

import org.junit.jupiter.api.Test;

class DesktopUtilsTest {

	@Test
	void testTryBrowse() throws Exception {

		final String filePathString = "D:\\IVI_MISC\\Tmp\\swing-utils\\test.html";
		final String url = new File(filePathString).toURI().toURL().toExternalForm();

		DesktopUtils.tryBrowse(url);
	}

	@Test
	void testTryOpen() {

		final String filePathString = "D:\\IVI_MISC\\Tmp\\swing-utils\\test.html";

		final File file = new File(filePathString);
		DesktopUtils.tryOpen(file);
	}
}
