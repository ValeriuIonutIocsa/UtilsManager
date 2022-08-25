package com.utils.gui.help.patterns;

import com.utils.gui.help.AbstractVBoxHelpHtml;

public class VBoxHelpHtmlPatternSimple extends AbstractVBoxHelpHtml {

	@Override
	protected String createHtmlResourceFilePathString() {
		return "com/utils/gui/help/patterns/HelpPatternSimple.html";
	}

	@Override
	protected String createTitle() {
		return "Help: Simple Pattern";
	}
}
