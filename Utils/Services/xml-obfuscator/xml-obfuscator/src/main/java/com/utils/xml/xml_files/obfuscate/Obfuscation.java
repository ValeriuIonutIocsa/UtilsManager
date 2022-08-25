package com.utils.xml.xml_files.obfuscate;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.utils.string.StrUtils;
import com.utils.xml.stax.XmlStAXWriter;

public class Obfuscation {

	private final String xpath;
	private final boolean textContent;
	private final Set<String> attributes;

	Obfuscation(
			final String xpath,
			final boolean textContent,
			final Set<String> attributes) {

		this.xpath = xpath;
		this.textContent = textContent;
		this.attributes = attributes;
	}

	public void processXmlEvent(
			final XMLEvent xmlEvent,
			final Map<String, String> obfuscatedToOriginalValuesMap,
			final XmlStAXWriter xmlStAXWriter) {

		if (xmlEvent.isCharacters()) {

			if (textContent) {

				final Characters characters = xmlEvent.asCharacters();
				final String textContent = characters.getData();
				final String processedTextContent =
						obfuscateText(textContent, obfuscatedToOriginalValuesMap);
				xmlStAXWriter.writeCharacters(processedTextContent);

			} else {
				xmlStAXWriter.writeXmlEvent(xmlEvent);
			}

		} else if (xmlEvent.isStartElement()) {

			final StartElement startElement = xmlEvent.asStartElement();
			final String tagName = startElement.getName().getLocalPart();
			xmlStAXWriter.writeStartElement(tagName);

			final Iterator<?> iteratorAttributes = startElement.getAttributes();
			while (iteratorAttributes.hasNext()) {

				final Object nextObject = iteratorAttributes.next();
				if (nextObject instanceof Attribute) {

					final Attribute attribute = (Attribute) nextObject;
					processAttribute(attribute, obfuscatedToOriginalValuesMap, xmlStAXWriter);
				}
			}
		}
	}

	private void processAttribute(
			final Attribute attribute,
			final Map<String, String> obfuscatedToOriginalValuesMap,
			final XmlStAXWriter xmlStAXWriter) {

		final QName attributeQName = attribute.getName();
		final String attributeName = attributeQName.getLocalPart();
		if (attributes.contains(attributeName)) {

			final String attributeValue = attribute.getValue();
			final String processedAttributeValue =
					obfuscateText(attributeValue, obfuscatedToOriginalValuesMap);
			xmlStAXWriter.writeAttribute(attributeQName, processedAttributeValue);

		} else {
			xmlStAXWriter.writeXmlEvent(attribute);
		}
	}

	static String obfuscateText(
			final String textContent,
			final Map<String, String> obfuscatedToOriginalValuesMap) {

		String obfuscatedTextContent;
		final int splitIndex = textContent.indexOf('?');
		if (splitIndex >= 0) {

			final String firstPart = textContent.substring(0, splitIndex);
			final String secondPart = textContent.substring(splitIndex);

			obfuscatedTextContent = obfuscatedToOriginalValuesMap.getOrDefault(firstPart, null);
			if (obfuscatedTextContent == null) {
				final int obfuscatedValuesCount = obfuscatedToOriginalValuesMap.size();
				obfuscatedTextContent = "val_" + obfuscatedValuesCount;
				obfuscatedToOriginalValuesMap.put(firstPart, obfuscatedTextContent);
			}
			obfuscatedTextContent += secondPart;

		} else {
			obfuscatedTextContent = obfuscatedToOriginalValuesMap.getOrDefault(textContent, null);
			if (obfuscatedTextContent == null) {
				final int obfuscatedValuesCount = obfuscatedToOriginalValuesMap.size();
				obfuscatedTextContent = "val_" + obfuscatedValuesCount;
				obfuscatedToOriginalValuesMap.put(textContent, obfuscatedTextContent);
			}
		}

		return obfuscatedTextContent;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public String getXpath() {
		return xpath;
	}
}
