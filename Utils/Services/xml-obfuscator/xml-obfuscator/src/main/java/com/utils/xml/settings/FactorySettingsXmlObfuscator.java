package com.utils.xml.settings;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.utils.log.Logger;
import com.utils.xml.dom.XmlDomUtils;
import com.utils.xml.xml_files.FactoryXmlFile;
import com.utils.xml.xml_files.XmlFile;

public final class FactorySettingsXmlObfuscator {

	private FactorySettingsXmlObfuscator() {
	}

	public static SettingsXmlObfuscator newInstance(
			final String configurationFilePathString) {

		SettingsXmlObfuscator settingsXmlObfuscator = null;
		try {
			Logger.printProgress("parsing XML obfuscator configuration file:");
			Logger.printLine(configurationFilePathString);

			final Document document = XmlDomUtils.openDocument(configurationFilePathString);
			final Element documentElement = document.getDocumentElement();

			final List<XmlFile> xmlFileList = new ArrayList<>();
			final List<Element> xmlFileElementList =
					XmlDomUtils.getElementsByTagName(documentElement, "xml_file");
			for (final Element xmlFileElement : xmlFileElementList) {

				final XmlFile xmlFile = FactoryXmlFile.parseXmlFile(xmlFileElement);
				if (xmlFile != null) {
					xmlFileList.add(xmlFile);
				}
			}

			settingsXmlObfuscator = new SettingsXmlObfuscator(xmlFileList);

		} catch (final Exception exc) {
			Logger.printError("failed to parse configuration file");
			Logger.printException(exc);
		}
		return settingsXmlObfuscator;
	}
}
