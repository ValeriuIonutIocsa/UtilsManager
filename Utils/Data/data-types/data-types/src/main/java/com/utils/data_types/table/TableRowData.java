package com.utils.data_types.table;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.utils.data_types.data_items.DataItem;
import com.utils.xml.stax.XmlStAXWriter;

public interface TableRowData extends Serializable {

	default DataItem<?>[] getDataItemArray() {
		return getTableViewDataItemArray();
	}

	DataItem<?>[] getTableViewDataItemArray();

	default void writeToXml(
			final String tagName,
			final TableColumnData[] columnsData,
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeStartElement(tagName);

		final DataItem<?>[] dataItemArray = getDataItemArray();

		for (int i = 0; i < columnsData.length; i++) {

			final TableColumnData tableColumnData = columnsData[i];
			final String columnName = tableColumnData.getSerializeName();
			if (StringUtils.isNotBlank(columnName)) {

				final DataItem<?> dataItem = dataItemArray[i];
				if (dataItem != null) {
					dataItem.writeToXml(xmlStAXWriter, columnName);
				} else {
					xmlStAXWriter.writeAttribute(columnName, "");
				}
			}
		}
		xmlStAXWriter.writeEndElement(tagName);
	}

	default void writeToCsv(
			final PrintStream printStream) {

		final DataItem<?>[] dataItemArray = getDataItemArray();
		for (int i = 0; i < dataItemArray.length; i++) {

			final DataItem<?> dataItem = dataItemArray[i];

			if (dataItem != null) {
				final String csvString = dataItem.createCsvString();
				printStream.print(csvString);
			}
			if (i < dataItemArray.length - 1) {
				printStream.print(',');
			}
		}
		printStream.println();
	}

	default void writeToJson(
			final TableColumnData[] columnsData,
			final boolean childHasMoreEntries,
			final PrintStream printStream) {

		printStream.print("    ".repeat(4));
		printStream.print('{');
		printStream.println();

		final Map<TableColumnData, DataItem<?>> notBlankColumnDataMap = new LinkedHashMap<>();
		final DataItem<?>[] dataItemArray = getDataItemArray();
		for (int i = 0; i < columnsData.length; i++) {

			final TableColumnData tableColumnData = columnsData[i];
			final DataItem<?> dataItem = dataItemArray[i];

			final String columnName = tableColumnData.getSerializeName();
			if (StringUtils.isNotBlank(columnName)) {
				notBlankColumnDataMap.put(tableColumnData, dataItem);
			}
		}

		int i = 0;
		for (final Map.Entry<TableColumnData, DataItem<?>> mapEntry : notBlankColumnDataMap.entrySet()) {

			final TableColumnData tableColumnData = mapEntry.getKey();
			final DataItem<?> dataItem = mapEntry.getValue();

			printStream.print("    ".repeat(5));
			printStream.print('"');
			final String columnName = tableColumnData.getSerializeName();
			printStream.print(columnName);
			printStream.print("\": \"");

			if (dataItem != null) {

				final String csvString = dataItem.createCsvString();
				printStream.print(csvString);
			}
			printStream.print('"');
			if (i < notBlankColumnDataMap.size() - 1) {
				printStream.print(',');
			}
			printStream.println();
			i++;
		}

		printStream.print("    ".repeat(4));
		printStream.print('}');
		if (childHasMoreEntries) {
			printStream.print(',');
		}
		printStream.println();
	}
}
