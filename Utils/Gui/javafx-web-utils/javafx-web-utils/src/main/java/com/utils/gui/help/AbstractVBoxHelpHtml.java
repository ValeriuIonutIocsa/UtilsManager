package com.utils.gui.help;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.data.Dimensions;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.HBoxWindowButtons;
import com.utils.gui.objects.PopupWindow;
import com.utils.gui.objects.web_view.CustomWebView;
import com.utils.io.ResourceFileUtils;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

public abstract class AbstractVBoxHelpHtml
		extends AbstractCustomControl<VBox> implements VBoxHelpHtml {

	@Override
	protected VBox createRoot() {

		final VBox vBoxRoot = LayoutControlsFactories.getInstance().createVBox();

		final CustomWebView customWebView = createCustomWebView();
		GuiUtils.addToVBox(vBoxRoot, customWebView.getRoot(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final HBoxWindowButtons hBoxWindowButtons = new HBoxWindowButtons("OK", null);
		GuiUtils.addToVBox(vBoxRoot, hBoxWindowButtons.getRoot(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 7, 0, 7, 0);

		return vBoxRoot;
	}

	private CustomWebView createCustomWebView() {

		final CustomWebView customWebView = new CustomWebView();
		final String htmlContent = createHtmlContent();
		customWebView.getRoot().getEngine().loadContent(htmlContent);
		return customWebView;
	}

	protected String createHtmlContent() {

		final String htmlResourceFilePathString = createHtmlResourceFilePathString();
		return ResourceFileUtils.resourceFileToString(htmlResourceFilePathString);
	}

	protected abstract String createHtmlResourceFilePathString();

	@Override
	public void showWindow(
			final Scene parentScene) {

		final String title = createTitle();
		new PopupWindow(parentScene, Modality.APPLICATION_MODAL, title,
				new Dimensions(450, 450, -1, -1, 960, 540), getRoot()).showAndWait();
	}

	protected abstract String createTitle();
}
