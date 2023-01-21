package com.utils.swing.desktop;

import java.io.File;

import org.junit.jupiter.api.Test;

class DesktopUtilsTest {

	@Test
	void testTryBrowse() throws Exception {

		final String filePathString = "C:\\IVI\\Vitesco\\Main\\Projects\\CRO\\ProjectAnalyzer\\ProjectAnalyzer" +
				"\\src\\main\\resources\\com\\vitesco\\pa\\gui\\views\\general\\help\\" +
				"CallTreeAnalysis\\2.CallTreeMemories\\CallTreeMemories.html";
		final String url = new File(filePathString).toURI().toURL().toExternalForm();

		DesktopUtils.tryBrowse(url);
	}

	@Test
	void testTryOpen() {

		final String filePathString = "C:\\IVI\\Vitesco\\Main\\Projects\\CRO\\ProjectAnalyzer\\ProjectAnalyzer" +
				"\\src\\main\\resources\\com\\vitesco\\pa\\gui\\views\\general\\help\\" +
				"CallTreeAnalysis\\2.CallTreeMemories\\CallTreeMemories.html";

		final File file = new File(filePathString);
		DesktopUtils.tryOpen(file);
	}
}
