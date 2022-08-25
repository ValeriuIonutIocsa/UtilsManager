package com.utils.gui.help.patterns;

import org.junit.jupiter.api.Test;

import com.utils.gui.AbstractCustomApplicationTest;
import com.utils.gui.GuiUtils;

import javafx.scene.layout.StackPane;

class VBoxHelpHtmlPatternUnixRegexTest extends AbstractCustomApplicationTest {

	@Test
	void testLayout() {

		GuiUtils.runAndWait(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			new VBoxHelpHtmlPatternUnixRegex().showWindow(stackPaneContainer.getScene());
		});
	}
}
