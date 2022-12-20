package com.personal.utils;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.utils.log.Logger;

class JDialogCreateTest {

	@Test
	void testDisplay() {

		final List<String> selectedTreePathList = JDialogCreate.display();

		Logger.printNewLine();
		Logger.printLine("selected tree paths:");
		for (final String selectedTreePath : selectedTreePathList) {
			Logger.printLine(selectedTreePath);
		}
	}
}
