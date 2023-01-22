package com.vitesco.pa.writers.file_writers;

import java.io.PrintStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.csv.AbstractCsvWriter;
import com.utils.csv.CsvWriter;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.io.PathUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;
import com.vitesco.pa.writers.file_writers.data.DataTable;

public class DataFileWriterCsv extends AbstractDataFileWriter {

	@Override
	void writeData(
			final String displayName,
			final String outputPathString,
			final List<DataTable> dataTableList) {

		final int dataTableCount = dataTableList.size();
		if (dataTableCount > 1) {

			final String outputPathWoExt = PathUtils.computePathWoExt(outputPathString);
			FactoryFolderCreator.getInstance().createDirectories(outputPathWoExt, true);
		}

		for (final DataTable dataTable : dataTableList) {
			writeToCsv(dataTable, dataTableCount, outputPathString);
		}

		if (dataTableCount > 1) {

			final String outputPathWoExt =
					PathUtils.computePathWoExt(outputPathString);
			Logger.printStatus("Successfully generated the CSV data files inside folder:");
			Logger.printLine(outputPathWoExt);

		} else {
			Logger.printStatus("Successfully generated the \"" + displayName + "\" data file:");
			Logger.printLine(outputPathString);
		}
	}

	private static void writeToCsv(
			final DataTable dataTable,
			final int dataTableCount,
			final String outputPathStringParam) {

		final String displayName = dataTable.getDisplayName();
		try {
			final String outputPathString;
			if (dataTableCount > 1) {

				final String outputPathExtension = PathUtils.computeExtension(outputPathStringParam);
				final String outputPathWoExt = PathUtils.computePathWoExt(outputPathStringParam);
				outputPathString = PathUtils.computePath(outputPathWoExt, displayName + "." + outputPathExtension);

			} else {
				outputPathString = outputPathStringParam;
			}

			final CsvWriter csvWriter = new AbstractCsvWriter(null, outputPathString) {

				@Override
				protected void write(
						final PrintStream printStream) {

					final TableColumnData[] columnsData = dataTable.getColumnsData();
					final String headerLine = StringUtils.join(columnsData, ',');
					printStream.println(headerLine);

					final List<? extends TableRowData> rowDataList = dataTable.getRowDataList();
					for (final TableRowData tableRowData : rowDataList) {
						tableRowData.writeToCsv(printStream);
					}
				}
			};
			final boolean success = csvWriter.writeCsv();
			if (!success) {
				throw new Exception();
			}

		} catch (final Exception exc) {
			Logger.printError("failed to generate the \"" + displayName + "\" data file");
			Logger.printException(exc);
		}
	}
}
