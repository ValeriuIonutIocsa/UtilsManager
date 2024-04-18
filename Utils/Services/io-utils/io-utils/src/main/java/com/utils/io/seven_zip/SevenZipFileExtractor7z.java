package com.utils.io.seven_zip;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.io.folder_deleters.FactoryFolderDeleter;
import com.utils.io.processes.InputStreamReaderThread;
import com.utils.log.Logger;
import com.utils.log.progress.ProgressIndicators;
import com.utils.string.StrUtils;

public final class SevenZipFileExtractor7z {

	private final String sevenZipExecutablePathString;
	private final String archiveFilePathString;
	private final String outputParentFolderPathString;
	private final boolean deleteExisting;

	private boolean success;

	public SevenZipFileExtractor7z(
			final String sevenZipExecutablePathString,
			final String archiveFilePathString,
			final String outputParentFolderPathString,
			final boolean deleteExisting) {

		this.sevenZipExecutablePathString = sevenZipExecutablePathString;
		this.archiveFilePathString = archiveFilePathString;
		this.outputParentFolderPathString = outputParentFolderPathString;
		this.deleteExisting = deleteExisting;
	}

	public void work() {

		success = false;
		try {
			if (!IoUtils.fileExists(archiveFilePathString)) {
				Logger.printError("7Z archive does not exist:" +
						System.lineSeparator() + archiveFilePathString);

			} else {
				final boolean keepGoing;
				if (deleteExisting) {

					String zipArchiveNameWoExt = null;
					final String suffix = ".7z";
					final String zipArchiveName = PathUtils.computeFileName(archiveFilePathString);
					if (zipArchiveName.endsWith(suffix)) {
						zipArchiveNameWoExt =
								zipArchiveName.substring(0, zipArchiveName.length() - suffix.length());
					}

					if (StringUtils.isNotBlank(zipArchiveNameWoExt)) {

						final String extractedFilePathString =
								PathUtils.computePath(outputParentFolderPathString, zipArchiveNameWoExt);
						if (IoUtils.directoryExists(extractedFilePathString)) {
							keepGoing = FactoryFolderDeleter.getInstance()
									.cleanFolder(extractedFilePathString, true, true);
						} else if (IoUtils.regularFileExists(extractedFilePathString)) {
							keepGoing = FactoryFileDeleter.getInstance()
									.deleteFile(extractedFilePathString, true, true);
						} else {
							keepGoing = true;
						}

					} else {
						keepGoing = true;
					}

				} else {
					keepGoing = true;
				}

				if (keepGoing) {

					ProgressIndicators.getInstance().update(0.0);
					Logger.printProgress("extracting 7Z archive");

					final List<String> commandPartList = new ArrayList<>();
					commandPartList.add(sevenZipExecutablePathString);
					commandPartList.add("x");
					commandPartList.add("-bsp1");
					commandPartList.add("-y");
					commandPartList.add(archiveFilePathString);
					commandPartList.add("-o" + outputParentFolderPathString);

					Logger.printProgress("executing command:");
					Logger.printLine(StringUtils.join(commandPartList, ' '));

					final String archiveFolderPathString =
							PathUtils.computeParentPath(archiveFilePathString);
					final File archiveFolder = new File(archiveFolderPathString);

					final Process process = new ProcessBuilder()
							.command(commandPartList)
							.directory(archiveFolder)
							.redirectErrorStream(true)
							.start();

					final InputStreamReaderThread inputStreamReaderThread = new InputStreamReaderThread(
							"extract 7z archive input stream reader", process.getInputStream(),
							StandardCharsets.UTF_8, new ReadBytesHandler7z());
					inputStreamReaderThread.start();

					final int exitCode = process.waitFor();
					inputStreamReaderThread.join();

					success = exitCode == 0;
				}
			}

		} catch (final Exception exc) {
			Logger.printException(exc);

		} finally {
			ProgressIndicators.getInstance().update(0.0);
		}

		if (!success) {
			Logger.printError("failed to extract 7Z archive:" +
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
