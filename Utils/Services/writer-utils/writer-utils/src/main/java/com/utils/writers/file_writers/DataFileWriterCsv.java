package com.utils.writers.file_writers;

import java.io.PrintStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.csv.AbstractCsvWriter;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.io.PathUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;
import com.utils.writers.file_writers.data.DataTable;

public final class DataFileWriterCsv extends AbstractDataFileWriter {

	public static final DataFileWriterCsv INSTANCE = new DataFileWriterCsv();

	private DataFileWriterCsv() {
	}

	@Override
	String writeData(
			final String displayName,
			final String outputPathString,
			final List<DataTable> dataTableList) {

		final String generatedOutputPathString;
		final String outputPathWoExt = PathUtils.computePathWoExt(outputPathString);
		final int dataTableCount = dataTableList.size();
		if (dataTableCount > 1) {
			FactoryFolderCreator.getInstance().createDirectories(outputPathWoExt, false, true);
		}

		for (final DataTable dataTable : dataTableList) {
			writeToCsv(dataTable, dataTableCount, outputPathString);
		}

		if (dataTableCount > 1) {

			Logger.printStatus("Successfully generated the CSV data files inside folder:");
			Logger.printLine(outputPathWoExt);
			generatedOutputPathString = outputPathWoExt;

		} else {
			Logger.printStatus("Successfully generated the \"" + displayName + "\" data file:");
			Logger.printLine(outputPathString);
			generatedOutputPathString = outputPathString;
		}
		return generatedOutputPathString;
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
			final boolean success = new CsvWriterImpl(null, outputPathString, dataTable).writeCsv();
			if (!success) {
				throw new Exception();
			}

		} catch (final Exception exc) {
			Logger.printError("failed to generate the \"" + displayName + "\" data file");
			Logger.printException(exc);
		}
	}

	private static class CsvWriterImpl extends AbstractCsvWriter {

		private final DataTable dataTable;

		protected CsvWriterImpl(
				final String name,
				final String outputPathString,
				final DataTable dataTable) {

			super(name, outputPathString);

			this.dataTable = dataTable;
		}

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
	}

	@Override
	public String getExtension() {
		return "csv";
	}

	@Override
	public int getOrder() {
		return 103;
	}
}
