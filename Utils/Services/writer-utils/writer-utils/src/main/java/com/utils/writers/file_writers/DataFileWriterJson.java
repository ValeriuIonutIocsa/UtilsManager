package com.utils.writers.file_writers;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.json.JsonUtils;
import com.utils.log.Logger;
import com.utils.writers.file_writers.data.DataTable;
import com.utils.writers.file_writers.data.JsonFirstLevelRoot;

public final class DataFileWriterJson extends AbstractDataFileWriter {

	public static final DataFileWriterJson INSTANCE = new DataFileWriterJson();

	private DataFileWriterJson() {
	}

	@Override
	String writeData(
			final String displayName,
			final String outputPathString,
			final List<DataTable> dataTableList) {

		final boolean folderCreationSuccess = FactoryFolderCreator.getInstance()
				.createParentDirectories(outputPathString, false, true);
		if (folderCreationSuccess) {

			FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(outputPathString, false, true);

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
		return outputPathString;
	}

	public static void write(
			final OutputStream outputStream,
			final List<DataTable> dataTableList) {

		try (PrintStream printStream = new PrintStream(outputStream)) {

			final JsonFirstLevelRoot jsonFirstLevelRoot = new JsonFirstLevelRoot(dataTableList);
			JsonUtils.writeObject(jsonFirstLevelRoot, 0, printStream, JsonFirstLevelRoot::writeToJson);
		}
	}

	@Override
	public String getExtension() {
		return "json";
	}

	@Override
	public int getOrder() {
		return 102;
	}
}
