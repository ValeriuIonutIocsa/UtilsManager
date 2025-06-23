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

	private static final String HTML_START = "<html><head><style>";
	private static final String HTML_MID =
			"</style><script language=\"javascript\" type=\"text/javascript\">" +
					"function toBottom(){window.scrollTo(0,document.body.scrollHeight);}" +
					"</script>" +
					"</head>" +
					"<body onload='toBottom()'>";
	private static final String HTML_END = "<br></body></html>";

	private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\\R");

	public CustomWebViewGuiLogger(
			final String styleCss) {

		super(styleCss);
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

	@Override
	public void load(
			final String html) {

		final StringBuilder stringBuilder = getStringBuilder();

		final String escapedText = escapeHtml(html);
		stringBuilder.append(escapedText);

		if (stringBuilder.length() > 100_000) {

			final int initialPartLength = HTML_START.length() + MAX_CSS_LENGTH + HTML_MID.length();
			stringBuilder.replace(initialPartLength, 36_000, "");
		}

		final String content = stringBuilder + HTML_END;
		loadContent(content);
	}

	static String escapeHtml(
			final String text) {

		return NEW_LINE_PATTERN.matcher(text).replaceAll("<br>");
	}

	@Override
	public void refreshStyle(
			final String styleCss) {

		setStyleCss(styleCss);

		final StringBuilder stringBuilder = getStringBuilder();

		final String paddedStyleCss = createPaddedStyleCss();
		stringBuilder.replace(HTML_START.length(), HTML_START.length() + MAX_CSS_LENGTH, paddedStyleCss);

		final String content = stringBuilder.toString();
		loadContent(content);
	}

	@Override
	public void initStringBuilder() {

		final StringBuilder stringBuilder = getStringBuilder();
		stringBuilder.setLength(0);

		stringBuilder.append(HTML_START);
		final String paddedStyleCss = createPaddedStyleCss();
		stringBuilder.append(paddedStyleCss);
		stringBuilder.append(HTML_MID);
	}
}
