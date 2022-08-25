package com.utils.gui.objects.search_and_filter.help;

import com.utils.gui.help.patterns.VBoxHelpHtmlPatternGlob;
import com.utils.gui.help.patterns.VBoxHelpHtmlPatternSimple;
import com.utils.gui.help.patterns.VBoxHelpHtmlPatternUnixRegex;

import javafx.scene.Scene;

public class HelperHBoxRegexTypeWeb implements HelperHBoxRegexType {

	@Override
	public void showHelpSimple(
			final Scene scene) {
		new VBoxHelpHtmlPatternSimple().showWindow(scene);
	}

	@Override
	public void showHelpGlob(
			final Scene scene) {
		new VBoxHelpHtmlPatternGlob().showWindow(scene);
	}

	@Override
	public void showHelpUnixRegex(
			final Scene scene) {
		new VBoxHelpHtmlPatternUnixRegex().showWindow(scene);
	}
}
