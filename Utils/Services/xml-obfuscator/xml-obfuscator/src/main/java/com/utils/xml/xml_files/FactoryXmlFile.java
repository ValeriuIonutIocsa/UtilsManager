package com.utils.xml.xml_files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.utils.log.Logger;
import com.utils.xml.dom.XmlDomUtils;
import com.utils.xml.xml_files.obfuscate.FactoryObfuscation;
import com.utils.xml.xml_files.obfuscate.Obfuscation;

public final class FactoryXmlFile {

	private FactoryXmlFile() {
	}

	public static XmlFile parseXmlFile(
			final Element xmlFileElement) {

		XmlFile xmlFile = null;
		try {
			final Path inputPath = parsePath(xmlFileElement, "input_path");
			final Path outputPath = parsePath(xmlFileElement, "output_path");
			final Path mappingsPath = parsePath(xmlFileElement, "mappings_path");

			final Map<String, Obfuscation> obfuscationsByXpathMap = new HashMap<>();
			final List<Element> obfuscationElementList =
					XmlDomUtils.getElementsByTagName(xmlFileElement, "obfuscate");
			for (final Element obfuscationElement : obfuscationElementList) {

				final Obfuscation obfuscation = FactoryObfuscation.parse(obfuscationElement);
				if (obfuscation != null) {

					final String xpath = obfuscation.getXpath();
					obfuscationsByXpathMap.put(xpath, obfuscation);
				}
			}

			xmlFile = new XmlFile(inputPath, outputPath, mappingsPath, obfuscationsByXpathMap);

		} catch (final Exception exc) {
			Logger.printError("failed to parse a xml file element");
			Logger.printException(exc);
		}
		return xmlFile;
	}

	private static Path parsePath(
			final Element xmlFileElement,
			final String pathElementTagName) {

		final Element inputPathElement =
				XmlDomUtils.getFirstElementByTagName(xmlFileElement, pathElementTagName);
		final String inputPathString = inputPathElement.getTextContent().trim();
		return Paths.get(inputPathString).toAbsolutePath();
	}
}
