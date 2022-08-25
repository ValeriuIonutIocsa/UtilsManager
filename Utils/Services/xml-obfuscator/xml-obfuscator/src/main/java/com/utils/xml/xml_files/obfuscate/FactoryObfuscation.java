package com.utils.xml.xml_files.obfuscate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;

import com.utils.log.Logger;
import com.utils.xml.dom.XmlDomUtils;

public final class FactoryObfuscation {

	private FactoryObfuscation() {
	}

	public static Obfuscation parse(
			final Element obfuscationElement) {

		Obfuscation obfuscation = null;
		try {
			final Element xpathElement =
					XmlDomUtils.getFirstElementByTagName(obfuscationElement, "xpath");
			final String xpath = xpathElement.getTextContent();

			final Element textContentElement =
					XmlDomUtils.getFirstElementByTagName(obfuscationElement, "text_content");
			final boolean textContent = textContentElement != null;

			final Set<String> attributeSet = new HashSet<>();
			final List<Element> attributeElements =
					XmlDomUtils.getElementsByTagName(obfuscationElement, "attribute");
			for (final Element attributeElement : attributeElements) {

				final String attribute = attributeElement.getTextContent();
				attributeSet.add(attribute);
			}

			obfuscation = new Obfuscation(xpath, textContent, attributeSet);

		} catch (final Exception exc) {
			Logger.printError("failed to parse an obfuscation element");
			Logger.printException(exc);
		}
		return obfuscation;
	}
}
