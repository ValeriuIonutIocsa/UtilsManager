package com.utils.gui.objects.web_view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.clipboard.ClipboardUtils;
import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.swing.desktop.DesktopUtils;

import javafx.concurrent.Worker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class CustomWebView extends AbstractCustomControl<WebView> {

	public static final int MAX_CSS_LENGTH = 500;

	private static final String HTML_START = "<style>";
	private static final String HTML_MID = "</style>";

	private String styleCss;

	private final StringBuilder stringBuilder;

	public CustomWebView(
			final String styleCss) {

		this.styleCss = styleCss;

		stringBuilder = new StringBuilder();
		clear();
	}

	@Override
	protected WebView createRoot() {

		final WebView webViewRoot = new WebView();
		webViewRoot.setContextMenuEnabled(false);
		configureWebViewCopy(webViewRoot);
		configureUrlHandling(webViewRoot);
		return webViewRoot;
	}

	private static void configureWebViewCopy(
			final WebView webView) {

		webView.addEventFilter(KeyEvent.KEY_PRESSED, (
				final KeyEvent event) -> {

			if (event.isControlDown() && event.getCode() == KeyCode.C) {

				final String selectedText = (String) webView.getEngine()
						.executeScript("window.getSelection().toString()");
				if (StringUtils.isNotBlank(selectedText)) {

					final String trimmedSelectedText = StringUtils.stripEnd(selectedText, "\n\r");
					ClipboardUtils.putStringInClipBoard(trimmedSelectedText);
				}
				event.consume();
			}
		});
	}

	private void configureUrlHandling(
			final WebView webView) {

		webView.getEngine().getLoadWorker().stateProperty().addListener((
				observable,
				oldValue,
				newValue) -> {

			if (newValue == Worker.State.SUCCEEDED) {

				final Document document = webView.getEngine().getDocument();
				if (document != null) {

					final NodeList nodeList = document.getElementsByTagName("a");
					final int nodeListLength = nodeList.getLength();
					for (int i = 0; i < nodeListLength; i++) {

						final Node node = nodeList.item(i);
						final EventTarget eventTarget = (EventTarget) node;
						eventTarget.addEventListener("click", evt -> {

							final EventTarget target = evt.getCurrentTarget();
							final HTMLAnchorElement anchorElement = (HTMLAnchorElement) target;
							final String href = anchorElement.getHref();
							if ("#".equals(href)) {
								webView.getEngine().executeScript("window.scrollTo(0,0)");

							} else {
								handleHref(href);
								evt.preventDefault();
							}
						}, false);
					}
				}
			}
		});
	}

	public void handleHref(
			final String href) {

		DesktopUtils.tryBrowse(href);
	}

	public void search(
			final String text,
			final boolean caseSensitive) {

		final boolean notEmpty = StringUtils.isNotEmpty(text);
		if (notEmpty) {

			try {
				final WebEngine webEngine = getRoot().getEngine();
				final Field pageField = webEngine.getClass().getDeclaredField("page");
				pageField.setAccessible(true);

				final Object webPage = pageField.get(webEngine);
				final Method method = webPage.getClass().getMethod("find",
						String.class, boolean.class, boolean.class, boolean.class);
				method.invoke(webPage, text, true, true, caseSensitive);

			} catch (final Throwable ignored) {
			}
		}
	}

	public void load(
			final String html) {

		final int htmlStart = HTML_START.length() + MAX_CSS_LENGTH + HTML_MID.length();
		stringBuilder.replace(htmlStart, stringBuilder.length(), html);

		final String content = stringBuilder.toString();
		loadContent(content);
	}

	public void refreshStyle(
			final String styleCss) {

		this.styleCss = styleCss;

		final String paddedStyleCss = createPaddedStyleCss();
		stringBuilder.replace(HTML_START.length(), HTML_START.length() + MAX_CSS_LENGTH, paddedStyleCss);

		final String content = stringBuilder.toString();
		loadContent(content);
	}

	public void clear() {

		initStringBuilder();
		load("");
	}

	protected void loadContent(
			final String content) {

		GuiUtils.run(() -> getRoot().getEngine().loadContent(content));
	}

	public void initStringBuilder() {

		stringBuilder.setLength(0);

		stringBuilder.append(HTML_START);
		final String paddedStyleCss = createPaddedStyleCss();
		stringBuilder.append(paddedStyleCss);
		stringBuilder.append(HTML_MID);
	}

	protected String createPaddedStyleCss() {

		final String paddedStyleCss;
		if (styleCss.length() <= MAX_CSS_LENGTH) {
			paddedStyleCss = styleCss;
		} else {
			Logger.printError("WebView CSS exceed maximum length of " + MAX_CSS_LENGTH + " characters");
			paddedStyleCss = "";
		}
		return StrUtils.createRightPaddedString(paddedStyleCss, MAX_CSS_LENGTH);
	}

	protected void setStyleCss(
			final String styleCss) {
		this.styleCss = styleCss;
	}

	protected String getStyleCss() {
		return styleCss;
	}

	protected StringBuilder getStringBuilder() {
		return stringBuilder;
	}
}
