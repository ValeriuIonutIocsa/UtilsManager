package com.utils.gui.logger;

import java.util.regex.Pattern;

import com.utils.gui.GuiUtils;
import com.utils.gui.objects.web_view.CustomWebView;
import com.utils.log.Logger;
import com.utils.log.MessageConsumer;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.web.WebView;

public class CustomWebViewGuiLogger extends CustomWebView {

	public static final String HTML_FONT =
			"<style> * { font-family: \"Helvetica\"; font-size: 12px; } </style>";
	private static final String HTML_HEADER = "<html>" +
			"<head>" +
			HTML_FONT +
			"<script language=\"javascript\" type=\"text/javascript\">" +
			"function toBottom(){window.scrollTo(0,document.body.scrollHeight);}" +
			"</script>" +
			"</head>" +
			"<body onload='toBottom()'>";
	private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\\R");

	private final StringBuilder stringBuilder;

	public CustomWebViewGuiLogger() {

		stringBuilder = new StringBuilder();
		initStringBuilder();
	}

	@Override
	protected WebView createRoot() {

		final WebView webViewRoot = super.createRoot();

		final ContextMenu contextMenu = new ContextMenu();

		final MenuItem menuItemClear = new MenuItem("clear");
		menuItemClear.setOnAction(event -> clear());
		contextMenu.getItems().add(menuItemClear);

		webViewRoot.setOnMouseClicked(mouseEvent -> {
			if (GuiUtils.isRightClick(mouseEvent)) {
				contextMenu.show(webViewRoot, mouseEvent.getScreenX(), mouseEvent.getScreenY());
			} else {
				contextMenu.hide();
			}
		});

		return webViewRoot;
	}

	public void useAsLogger() {

		final MessageConsumer oldMessageConsumer = Logger.getMessageConsumer();
		Logger.setMessageConsumer(new MessageConsumerGuiLogger(oldMessageConsumer, this));
	}

	public void log(
			final String text) {

		final String escapedText = escapeHtml(text);
		stringBuilder.append(escapedText);
		if (stringBuilder.length() > 100_000) {
			stringBuilder.replace(HTML_HEADER.length(), 36_000, "");
		}
		final String content = stringBuilder + "<br></body></html>";
		loadContent(content);
	}

	static String escapeHtml(
			final String text) {
		return NEW_LINE_PATTERN.matcher(text).replaceAll("<br>");
	}

	@Override
	public void clear() {

		loadContent("");
		initStringBuilder();
	}

	private void initStringBuilder() {

		stringBuilder.setLength(0);
		stringBuilder.append(HTML_HEADER);
	}

	private void loadContent(
			final String content) {
		GuiUtils.run(() -> getRoot().getEngine().loadContent(content));
	}
}
