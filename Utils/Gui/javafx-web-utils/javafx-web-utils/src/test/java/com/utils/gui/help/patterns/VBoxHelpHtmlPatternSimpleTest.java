package com.utils.gui.help.patterns;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;
import com.utils.gui.GuiUtils;
import com.utils.gui.objects.web_view.CustomWebViewUtils;

import javafx.scene.layout.StackPane;

class VBoxHelpHtmlPatternSimpleTest extends AbstractCustomApplicationTest {

	@Test
	void testLayout() {

		GuiUtils.runAndWait(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			final String webViewStyleCss = CustomWebViewUtils.createWebViewStyleCss();
			new VBoxHelpHtmlPatternSimple(webViewStyleCss).showWindow(stackPaneContainer.getScene());
		});
	}
}
