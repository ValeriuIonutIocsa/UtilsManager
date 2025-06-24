package com.personal.utils.idea;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.utils.io.PathUtils;
import com.utils.io.file_copiers.FactoryFileCopier;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;
import com.utils.xml.dom.XmlDomUtils;

public final class IdeaFilesSynchronizer {

	private IdeaFilesSynchronizer() {
	}

	public static void work(
			final String srcIdeaFolderPathString,
			final String dstIdeaFolderPathString) {

		final boolean createDirectoriesSuccess =
				FactoryFolderCreator.getInstance().createDirectories(dstIdeaFolderPathString, false, true);
		if (createDirectoriesSuccess) {

			final String[] settingsFileNameArray = {
					"checkstyle-idea.xml",
					"eclipseCodeFormatter.xml",
					"gradle.xml",
					"misc.xml",
					"saveactions_settings.xml"
			};
			for (final String settingsFileName : settingsFileNameArray) {

				final String srcFilePathString =
						PathUtils.computePath(srcIdeaFolderPathString, settingsFileName);
				final String dstFilePathString =
						PathUtils.computePath(dstIdeaFolderPathString, settingsFileName);
				final boolean copyFileSuccess = FactoryFileCopier.getInstance()
						.copyFile(srcFilePathString, dstFilePathString, false, true, true);
				if (copyFileSuccess && "gradle.xml".equals(settingsFileName)) {

					editGradleXmlFile(dstFilePathString);
				}
			}
		}
	}

	private static void editGradleXmlFile(
			final String gradleSettingsFilePathString) {

		try {
			Logger.printProgress("editing Gradle idea settings XML file:");
			Logger.printLine(gradleSettingsFilePathString);

			final Document document = XmlDomUtils.openDocument(gradleSettingsFilePathString);
			final Element documentElement = document.getDocumentElement();

			final List<Element> optionElementList =
					XmlDomUtils.getElementsByTagName(documentElement, "option");
			for (final Element optionElement : optionElementList) {

				final String name = optionElement.getAttribute("name");
				if ("modules".equals(name)) {

					final Node parentNode = optionElement.getParentNode();
					parentNode.removeChild(optionElement);
				}
			}
			XmlDomUtils.saveXmlFile(document, false, 4, gradleSettingsFilePathString);

		} catch (final Throwable throwable) {
			Logger.printError("failed to edit Gradle idea settings XML file:" +
					System.lineSeparator() + gradleSettingsFilePathString);
			Logger.printThrowable(throwable);
		}
	}
}
