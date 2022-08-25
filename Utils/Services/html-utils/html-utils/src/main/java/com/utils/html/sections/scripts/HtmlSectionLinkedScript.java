package com.utils.html.sections.scripts;

import com.utils.html.sections.AbstractHtmlSection;
import com.utils.xml.stax.XmlStAXWriter;

public class HtmlSectionLinkedScript extends AbstractHtmlSection {

	private final String jsScriptLink;

	public HtmlSectionLinkedScript(
			final String jsScriptLink) {

		this.jsScriptLink = jsScriptLink;
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeStartElement("script");
		xmlStAXWriter.writeAttribute("type", "text/javascript");
		xmlStAXWriter.writeAttribute("src", jsScriptLink);

		xmlStAXWriter.writeEndElement("script");
	}
}
