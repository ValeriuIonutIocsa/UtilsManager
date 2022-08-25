package com.utils.gui.help.patterns;

import com.utils.gui.help.AbstractVBoxHelpHtml;

public class VBoxHelpHtmlPatternUnixRegex extends AbstractVBoxHelpHtml {

	@Override
	protected String createHtmlResourceFilePathString() {
		return "com/utils/gui/help/patterns/HelpPatternUnixRegex.html";
	}

	@Override
	protected String createTitle() {
		return "Help: Unix Regex Pattern";
	}
}
