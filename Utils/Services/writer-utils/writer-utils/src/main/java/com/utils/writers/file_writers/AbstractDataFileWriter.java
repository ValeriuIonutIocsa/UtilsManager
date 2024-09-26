package com.utils.writers.file_writers;

import java.util.List;

import com.utils.log.Logger;
import com.utils.writers.file_writers.data.DataTable;

public abstract class AbstractDataFileWriter implements DataFileWriter {

	@Override
	public String write(
			final String displayName,
			final String outputPathString,
			final List<DataTable> dataTableList) throws Exception {

		final String generatedOutputFilePathString;
		try {
			generatedOutputFilePathString =
					writeData(displayName, outputPathString, dataTableList);

		} catch (final Exception exc) {
			Logger.printError("failed to generate the \"" + displayName + "\" data file:" +
					System.lineSeparator() + outputPathString);
			throw exc;
		}
		return generatedOutputFilePathString;
	}

	abstract String writeData(
			String displayName,
			String outputPathString,
			List<DataTable> dataTableList) throws Exception;
}
