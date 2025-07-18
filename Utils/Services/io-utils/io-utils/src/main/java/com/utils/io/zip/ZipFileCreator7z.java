package com.utils.io.zip;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.processes.InputStreamReaderThread;
import com.utils.io.seven_zip.ReadBytesHandler7z;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicators;
import com.utils.string.StrUtils;

public class ZipFileCreator7z {

	private final String sevenZipExecutablePathString;
	private final String inputFilePathString;
	private final String archiveFilePathString;
	private final boolean deleteExisting;

	private boolean success;

	public ZipFileCreator7z(
			final String sevenZipExecutablePathString,
			final String inputFilePathString,
			final String archiveFilePathString,
			final boolean deleteExisting) {

		this.sevenZipExecutablePathString = sevenZipExecutablePathString;
		this.inputFilePathString = inputFilePathString;
		this.archiveFilePathString = archiveFilePathString;
		this.deleteExisting = deleteExisting;
	}

	public void work() {

		success = false;
		try {
			if (!IoUtils.fileExists(inputFilePathString)) {
				Logger.printError("input file does not exist:" +
						System.lineSeparator() + inputFilePathString);

			} else {
				boolean keepGoing;
				if (deleteExisting && IoUtils.fileExists(archiveFilePathString)) {
					keepGoing = FactoryFileDeleter.getInstance()
							.deleteFile(archiveFilePathString, true, true);
				} else {
					keepGoing = true;
				}

				if (keepGoing) {

					keepGoing = FactoryFolderCreator.getInstance()
							.createParentDirectories(archiveFilePathString, false, true);
					if (keepGoing) {

						ProgressIndicators.getInstance().update(0.0);
						Logger.printProgress("creating ZIP archive:");
						Logger.printLine(archiveFilePathString);

						final List<String> commandPartList = new ArrayList<>();
						commandPartList.add(sevenZipExecutablePathString);
						commandPartList.add("a");
						commandPartList.add("-bsp1");
						commandPartList.add(archiveFilePathString);
						commandPartList.add(inputFilePathString);

						Logger.printProgress("executing command:");
						Logger.printLine(StringUtils.join(commandPartList, ' '));

						final String zipArchiveFolderPathString =
								PathUtils.computeParentPath(archiveFilePathString);
						final File zipArchiveFolder = new File(zipArchiveFolderPathString);

						final Process process = new ProcessBuilder()
								.directory(zipArchiveFolder)
								.command(commandPartList)
								.redirectErrorStream(true)
								.start();

						final InputStreamReaderThread inputStreamReaderThread = new InputStreamReaderThread(
								"create zip archive input stream reader", process.getInputStream(),
								StandardCharsets.UTF_8, new ReadBytesHandler7z());
						inputStreamReaderThread.start();

						final int exitCode = process.waitFor();
						inputStreamReaderThread.join();

						if (exitCode == 0) {
							success = IoUtils.fileExists(archiveFilePathString);
						}
					}
				}
			}

		} catch (final Throwable throwable) {
			Logger.printThrowable(throwable);

		} finally {
			ProgressIndicators.getInstance().update(0.0);
		}

		if (!success) {
			Logger.printError("failed to create ZIP archive:" +
					System.lineSeparator() + archiveFilePathString);
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public boolean isSuccess() {
		return success;
	}
}
