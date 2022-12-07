package com.utils.xml.xml_files;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;

import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.xml.stax.AbstractXmlStAXReader;
import com.utils.xml.stax.AbstractXmlStAXWriter;
import com.utils.xml.xml_files.obfuscate.Obfuscation;

public class XmlFile {

	private final String inputPathString;
	private final String outputPathString;
	private final String mappingsPathString;
	private final Map<String, Obfuscation> obfuscationsByXpathMap;

	XmlFile(
			final String inputPathString,
			final String outputPathString,
			final String mappingsPathString,
			final Map<String, Obfuscation> obfuscationsByXpathMap) {

		this.inputPathString = inputPathString;
		this.outputPathString = outputPathString;
		this.mappingsPathString = mappingsPathString;
		this.obfuscationsByXpathMap = obfuscationsByXpathMap;
	}

	public void obfuscate() {

		Logger.printProgress("obfuscating XML file:");
		Logger.printLine(inputPathString);

		final Map<String, String> obfuscatedToOriginalValuesMap = new LinkedHashMap<>();

		new AbstractXmlStAXWriter(outputPathString, "    ") {

			@Override
			protected void write() {

				new AbstractXmlStAXReader(inputPathString) {

					@Override
					protected void parseXmlEvent(
							final Stack<String> pathInXml,
							final XMLEvent xmlEvent) {

						if (xmlEvent.isStartElement() || xmlEvent.isCharacters()) {

							final String pathInXmlString = StringUtils.join(pathInXml, '/');
							final Obfuscation obfuscation = obfuscationsByXpathMap.get(pathInXmlString);
							if (obfuscation != null) {
								processXmlEvent(xmlEvent, obfuscation, obfuscatedToOriginalValuesMap);
							} else {
								writeXmlEvent(xmlEvent);
							}

						} else {
							writeXmlEvent(xmlEvent);
						}
					}

				}.readXml();
			}

			private void processXmlEvent(
					final XMLEvent xmlEvent,
					final Obfuscation obfuscation,
					final Map<String, String> obfuscatedToOriginalValuesMap) {
				obfuscation.processXmlEvent(xmlEvent, obfuscatedToOriginalValuesMap, this);
			}

		}.writeXml();

		writeMapping(obfuscatedToOriginalValuesMap);

		Logger.printStatus("Obfuscated XML file generated:");
		Logger.printLine(outputPathString);
	}

	private void writeMapping(
			final Map<String, String> obfuscatedToOriginalValuesMap) {

		Logger.printProgress("generating the obfuscation mapping file:");
		Logger.printLine(mappingsPathString);

		new AbstractXmlStAXWriter(mappingsPathString, "    ") {

			@Override
			protected void write() {

				writeStartDocument();
				final String obfuscationMappingsTagName = "ObfuscationMappings";
				writeStartElement(obfuscationMappingsTagName);

				for (final Map.Entry<String, String> mappingEntry : obfuscatedToOriginalValuesMap.entrySet()) {

					final String original = mappingEntry.getKey();
					final String obfuscated = mappingEntry.getValue();

					final String obfuscationTagName = "Obfuscation";
					writeStartElement(obfuscationTagName);
					writeAttribute("Original", original);
					writeAttribute("Obfuscated", obfuscated);
					writeEndElement(obfuscationTagName);
				}

				writeEndElement(obfuscationMappingsTagName);
				writeEndDocument();
			}
		}.writeXml();
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
