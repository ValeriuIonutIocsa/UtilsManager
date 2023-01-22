package com.vitesco.pa.writers.file_writers;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.vitesco.pa.writers.file_writers.data.DataTable;

public class DataFileWriterJson extends AbstractDataFileWriter {

	@Override
	void writeData(
			final String displayName,
			final String outputPathString,
			final List<DataTable> dataTableList) {

		final boolean folderCreationSuccess = FactoryFolderCreator.getInstance()
				.createParentDirectories(outputPathString, true);
		if (folderCreationSuccess) {

			FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(outputPathString, true);

			try (OutputStream outputStream = StreamUtils.openBufferedOutputStream(outputPathString)) {
				write(outputStream, dataTableList);

			} catch (final Exception exc) {
				Logger.printError("failed to generate JSON file");
				Logger.printException(exc);
			}

			if (StringUtils.isNotBlank(displayName)) {

				Logger.printStatus("Successfully generated the \"" + displayName + "\" data file:");
				Logger.printLine(outputPathString);
			}
		}
	}

	public static void write(
			final OutputStream outputStream,
			final List<DataTable> dataTableList) {

		try (PrintStream printStream = new PrintStream(outputStream)) {

			printStream.print('{');
			printStream.println();

			printStream.print("    ");
			printStream.print("\"ExportedData\": {");
			printStream.println();

			for (int i = 0; i < dataTableList.size(); i++) {

				final DataTable dataTable = dataTableList.get(i);
				final boolean hasMoreEntries = i < dataTableList.size() - 1;
				writeToJson(dataTable, hasMoreEntries, printStream);
			}

			printStream.print("    ");
			printStream.print('}');
			printStream.println();

			printStream.print('}');
			printStream.println();
		}
	}

	private static void writeToJson(
			final DataTable dataTable,
			final boolean hasMoreEntries,
			final PrintStream printStream) {

		final String xmlRootElementTagName = dataTable.getXmlRootElementTagName();
		printStream.print("    ".repeat(2));
		printStream.print('"');
		printStream.print(xmlRootElementTagName);
		printStream.print("\": {");
		printStream.println();

		final String displayName = dataTable.getDisplayName();
		printStream.print("    ".repeat(3));
		printStream.print("\"Name\": \"");
		printStream.print(displayName);
		printStream.print("\",");
		printStream.println();

		final String xmlDataElementTagName = dataTable.getXmlDataElementTagName();
		printStream.print("    ".repeat(3));
		printStream.print('"');
		printStream.print(xmlDataElementTagName);
		printStream.print("\": [");
		printStream.println();

		final List<? extends TableRowData> rowDataList = dataTable.getRowDataList();
		for (int i = 0; i < rowDataList.size(); i++) {

			final TableRowData tableRowData = rowDataList.get(i);
			final TableColumnData[] columnsData = dataTable.getColumnsData();
			final boolean childHasMoreEntries = i < rowDataList.size() - 1;
			tableRowData.writeToJson(columnsData, childHasMoreEntries, printStream);
		}

		printStream.print("    ".repeat(3));
		printStream.print("]");
		printStream.println();

		printStream.print("    ".repeat(2));
		printStream.print('}');
		if (hasMoreEntries) {
			printStream.print(',');
		}
		printStream.println();
	}
}
