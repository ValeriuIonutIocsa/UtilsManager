package com.utils.gui.objects.web_view;

import com.utils.io.ResourceFileUtils;

public final class CustomWebViewUtils {

	private CustomWebViewUtils() {
	}

	public static String createWebViewStyleCss() {
		return ResourceFileUtils.resourceFileToString("com/utils/gui/objects/web_view/web_view_style.css");
	}

	public static String createWebViewStyleCssDark() {
		return ResourceFileUtils.resourceFileToString("com/utils/gui/objects/web_view/web_view_style_dark.css");
	}
}
