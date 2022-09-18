package com.utils.html.writers;

import java.io.OutputStream;

import com.utils.html.sections.HtmlSectionPlainText;
import com.utils.xml.stax.AbstractXmlStAXWriter;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractXmlStAXWriterHtml extends AbstractXmlStAXWriter {

	protected AbstractXmlStAXWriterHtml(
			final OutputStream outputStream) {
		super(outputStream, "");
	}

	@Override
	protected void write() {

		writePlainText("<!-- saved from url=(0014)about:internet -->");
		final String htmlTagName = "html";
		writeStartElement(htmlTagName);

		final String headTagName = "head";
		writeStartElement(headTagName);
		writeHead(this);
		writeEndElement(headTagName);

		final String bodyTagName = "body";
		writeStartElement(bodyTagName);
		writeBodyAttributes(this);
		writeBody(this);
		writeEndElement(bodyTagName);

		writeEndElement(htmlTagName);
	}

	private void writeHead(
			final XmlStAXWriter xmlStAXWriter) {

		final String metaTagName = "meta";
		xmlStAXWriter.writeStartElement(metaTagName);
		xmlStAXWriter.writeAttribute("http-equiv", "X-UA-Compatible");
		xmlStAXWriter.writeAttribute("content", "IE=10; IE=9; IE=8; IE=7; IE=EDGE");
		xmlStAXWriter.writeEndElement(metaTagName);

		final String styleTagName = "style";
		xmlStAXWriter.writeStartElement(styleTagName);
		xmlStAXWriter.writeAttribute("type", "text/css");
		xmlStAXWriter.writeAttribute("xml:space", "preserve");

		final String cssString = createCssString();
		new HtmlSectionPlainText(cssString).write(xmlStAXWriter);

		xmlStAXWriter.writeEndElement(styleTagName);
	}

	protected abstract String createCssString();

	protected void writeBodyAttributes(
			final XmlStAXWriter xmlStAXWriter) {
	}

	protected abstract void writeBody(
			XmlStAXWriter xmlStAXWriter);
}
