package com.utils.gui.help.patterns;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;
import com.utils.gui.GuiUtils;
import com.utils.gui.objects.web_view.CustomWebViewUtils;

import javafx.scene.layout.StackPane;

class VBoxHelpHtmlPatternGlobTest extends AbstractCustomApplicationTest {

	@Test
	void testLayout() {

		GuiUtils.runAndWait(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			final String webViewStyleCss = CustomWebViewUtils.createWebViewStyleCss();
			new VBoxHelpHtmlPatternGlob(webViewStyleCss).showWindow(stackPaneContainer.getScene());
		});
	}
}
