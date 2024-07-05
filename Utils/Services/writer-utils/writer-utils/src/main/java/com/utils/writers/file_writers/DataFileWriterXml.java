package com.utils.writers.file_writers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.log.Logger;
import com.utils.writers.file_writers.data.DataTable;
import com.utils.xml.stax.AbstractXmlStAXWriter;
import com.utils.xml.stax.XmlStAXWriter;

public final class DataFileWriterXml extends AbstractDataFileWriter {

	public static final DataFileWriterXml INSTANCE = new DataFileWriterXml();

	private DataFileWriterXml() {
	}

	@Override
	void writeData(
			final String displayName,
			final String outputPathString,
			final List<DataTable> dataTableList) {

		new AbstractXmlStAXWriter(outputPathString, "    ") {

			@Override
			protected void write() {
				writeToXml(this, dataTableList);
			}
		}.writeXml();

		if (StringUtils.isNotBlank(displayName)) {

			Logger.printStatus("Successfully generated the \"" + displayName + "\" data file:");
			Logger.printLine(outputPathString);
		}
	}

	private static void writeToXml(
			final XmlStAXWriter xmlStAXWriter,
			final List<DataTable> dataTableList) {

		xmlStAXWriter.writeStartDocument();
		final String rootTagName = "ExportedData";
		xmlStAXWriter.writeStartElement(rootTagName);

		for (final DataTable dataTable : dataTableList) {
			writeToXml(xmlStAXWriter, dataTable);
		}

		xmlStAXWriter.writeEndElement(rootTagName);
		xmlStAXWriter.writeEndDocument();
	}

	private static void writeToXml(
			final XmlStAXWriter xmlStAXWriter,
			final DataTable dataTable) {

		final String xmlRootElementTagName = dataTable.getXmlRootElementTagName();
		xmlStAXWriter.writeStartElement(xmlRootElementTagName);
		final String displayName = dataTable.getDisplayName();
		xmlStAXWriter.writeAttribute("Name", displayName);

		final String xmlDataElementTagName = dataTable.getXmlDataElementTagName();
		final TableColumnData[] columnsData = dataTable.getColumnsData();
		final List<? extends TableRowData> rowDataList = dataTable.getRowDataList();
		for (final TableRowData tableRowData : rowDataList) {
			tableRowData.writeToXml(xmlDataElementTagName, columnsData, xmlStAXWriter);
		}

		xmlStAXWriter.writeEndElement(xmlRootElementTagName);
	}

	@Override
	public String getExtension() {
		return "xml";
	}

	@Override
	public int getOrder() {
		return 101;
	}
}
