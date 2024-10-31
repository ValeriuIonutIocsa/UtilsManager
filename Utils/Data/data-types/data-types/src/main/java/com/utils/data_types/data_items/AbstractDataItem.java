package com.utils.data_types.data_items;

import java.io.PrintStream;

import com.utils.json.JsonUtils;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractDataItem<
		ObjectT> implements DataItem<ObjectT> {

	private short indent;

	@Override
	public String createCsvString() {

		String copyString = createCopyString();
		copyString = copyString.replace("\r", "\\r");
		copyString = copyString.replace("\n", "\\n");
		return copyString;
	}

	@Override
	public String createCopyString() {
		return toString();
	}

	@Override
	public void writeToJson(
			final String columnName,
			final boolean notLastAttribute,
			final int indentCount,
			final PrintStream printStream) {

		final String copyString = createCopyString();
		JsonUtils.writeStringAttribute(columnName, copyString, notLastAttribute, indentCount, printStream);
	}

	@Override
	public void writeToXml(
			final XmlStAXWriter xmlStAXWriter,
			final String columnTitleName) {

		final String csvString = createCsvString();
		xmlStAXWriter.writeAttribute(columnTitleName, csvString);
	}

	@Override
	public ObjectT createXlsxValue() {
		return getValue();
	}

	@Override
	public void setIndent(
			final short indent) {
		this.indent = indent;
	}

	@Override
	public short getIndent() {
		return indent;
	}
}
