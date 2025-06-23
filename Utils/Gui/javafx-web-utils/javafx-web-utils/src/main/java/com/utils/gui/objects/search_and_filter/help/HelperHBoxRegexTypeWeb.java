package com.utils.gui.objects.search_and_filter.help;

import com.utils.gui.help.patterns.VBoxHelpHtmlPatternGlob;
import com.utils.gui.help.patterns.VBoxHelpHtmlPatternSimple;
import com.utils.gui.help.patterns.VBoxHelpHtmlPatternUnixRegex;
import com.utils.gui.objects.web_view.CustomWebViewUtils;

import javafx.scene.Scene;

public class HelperHBoxRegexTypeWeb implements HelperHBoxRegexType {

	@Override
	public void showHelpSimple(
			final Scene scene) {

		final String webViewStyleCss = CustomWebViewUtils.createWebViewStyleCss();
		new VBoxHelpHtmlPatternSimple(webViewStyleCss).showWindow(scene);
	}

	@Override
	public void showHelpGlob(
			final Scene scene) {

		final String webViewStyleCss = CustomWebViewUtils.createWebViewStyleCss();
		new VBoxHelpHtmlPatternGlob(webViewStyleCss).showWindow(scene);
	}

	@Override
	public void showHelpUnixRegex(
			final Scene scene) {

		final String webViewStyleCss = CustomWebViewUtils.createWebViewStyleCss();
		new VBoxHelpHtmlPatternUnixRegex(webViewStyleCss).showWindow(scene);
	}
}
