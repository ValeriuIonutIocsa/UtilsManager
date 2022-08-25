package com.utils.gui.styles.vitesco;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.utils.gui.GuiUtils;
import com.utils.gui.styles.vitesco.themes.FactoryGuiTheme;
import com.utils.gui.styles.vitesco.themes.GuiTheme;
import com.utils.io.IoUtils;
import com.utils.log.Logger;
import com.utils.xml.dom.XmlDomUtils;

import javafx.scene.Scene;

public final class VitescoStyleUtils {

	public static final String COMMON_STYLE_SHEET_RESOURCE_PATH_STRING =
			"com/utils/gui/styles/vitesco/style.css";

	private VitescoStyleUtils() {
	}

	public static void configureVitescoStyle(
			final Scene scene,
			final String specificStyleSheetResourceFilePath) {

		final String guiThemeName = parseGuiTheme();
		configureVitescoStyle(scene, specificStyleSheetResourceFilePath, guiThemeName);
	}

	public static void configureVitescoStyle(
			final Scene scene,
			final String specificStyleSheetResourceFilePath,
			final String guiThemeName) {

		final GuiTheme guiTheme = FactoryGuiTheme.computeInstance(guiThemeName);
		final String themeStyleSheetResourcePathString = guiTheme.getStyleSheetResourcePathString();
		GuiUtils.setStyleCss(scene, COMMON_STYLE_SHEET_RESOURCE_PATH_STRING,
				themeStyleSheetResourcePathString, specificStyleSheetResourceFilePath);
	}

	private static String parseGuiTheme() {

		String guiThemeName = null;
		try {
			final Path generalSettingsFilePath = Paths.get(SystemUtils.USER_HOME,
					"Documents", "ProjectAnalyzer", "GeneralSettings.xml");
			if (IoUtils.fileExists(generalSettingsFilePath)) {

				final Document document = XmlDomUtils.openDocument(generalSettingsFilePath);
				final Element documentElement = document.getDocumentElement();

				final Element guiThemeElement =
						XmlDomUtils.getFirstElementByTagName(documentElement, "gui_theme");
				if (guiThemeElement != null) {
					guiThemeName = guiThemeElement.getTextContent();
				}
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
		}
		return guiThemeName;
	}
}
