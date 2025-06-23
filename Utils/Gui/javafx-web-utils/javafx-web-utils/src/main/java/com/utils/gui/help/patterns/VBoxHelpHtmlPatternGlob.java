package com.utils.gui.help.patterns;

import com.utils.gui.help.AbstractVBoxHelpHtml;

public class VBoxHelpHtmlPatternGlob extends AbstractVBoxHelpHtml {

	public VBoxHelpHtmlPatternGlob(
			final String webViewStyleCss) {

		super(webViewStyleCss);
	}

	@Override
	protected String createHtmlResourceFilePathString() {
		return "com/utils/gui/help/patterns/HelpPatternGlob.html";
	}

	@Override
	protected String createTitle() {
		return "Help: Glob Pattern";
	}
}
