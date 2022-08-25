package com.utils.gui.logger;

import org.junit.jupiter.api.Test;

import com.utils.log.Logger;

class CustomWebViewGuiLoggerTest {

	@Test
	void testEscapeHtml() {

		final String text = "abc\nbcd";
		final String escapedText = CustomWebViewGuiLogger.escapeHtml(text);

		Logger.printNewLine();
		Logger.printLine(escapedText);
	}
}
