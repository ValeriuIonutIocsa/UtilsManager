package com.utils.io.folder_deleters;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.lang3.StringUtils;

import com.utils.io.file_deleters.FileDeleterWin;
import com.utils.log.Logger;
import com.utils.string.exc.SilentException;

public class FolderDeleterWin extends FolderDeleterImpl {

	@Override
	public boolean deleteFolderNoChecks(
			final String folderPathString,
			final boolean verboseProgress,
			final boolean verboseError) {

		boolean success = false;
		try {
			if (verboseProgress) {

				Logger.printProgress("deleting folder:");
				Logger.printLine(folderPathString);
			}

			success = deleteFolderWin(folderPathString);

		} catch (final Exception exc) {
			if (verboseError) {
				Logger.printError("failed to delete folder:" +
						System.lineSeparator() + folderPathString);
			}
			Logger.printException(exc);
		}
		return success;
	}

	@Override
	public boolean cleanFolderNoChecks(
			final String folderPathString,
			final boolean verboseProgress,
			final boolean verboseError) {

		boolean success = false;
		try {
			if (verboseProgress) {

				Logger.printProgress("cleaning folder:");
				Logger.printLine(folderPathString);
			}

			final Path folderPath = Paths.get(folderPathString);
			Files.walkFileTree(folderPath, new SimpleFileVisitor<>() {

				@Override
				public FileVisitResult preVisitDirectory(
						final Path dir,
						final BasicFileAttributes attrs) {

					boolean success = false;
					try {
						final String subFolderPathString = dir.toString();
						success = deleteFolderWin(subFolderPathString);

					} catch (final Exception ignored) {
					}
					if (!success) {
						throw new SilentException();
					}
					return FileVisitResult.SKIP_SUBTREE;
				}

				@Override
				public FileVisitResult visitFile(
						final Path filePath,
						final BasicFileAttributes attrs) {

					boolean success = false;
					try {
						final String filePathString = filePath.toString();
						success = FileDeleterWin.deleteFileWin(filePathString);

					} catch (final Exception ignored) {
					}
					if (!success) {
						throw new SilentException();
					}
					return FileVisitResult.CONTINUE;
				}
			});
			success = true;

		} catch (final SilentException ignored) {
		} catch (final Exception exc) {
			Logger.printException(exc);
		}

		if (!success) {
			if (verboseError) {
				Logger.printError("failed to clean folder:" +
						System.lineSeparator() + folderPathString);
			}
		}

		return success;
	}

	private static boolean deleteFolderWin(
			final String folderPathString) throws Exception {

		boolean success = false;
		final String[] delCommandPartArray =
				{ "cmd", "/c", "del", "/f", "/s", "/q", folderPathString };

		Logger.printProgress("executing command:");
		Logger.printLine(StringUtils.join(delCommandPartArray, ' '));

		final Process delProcess = new ProcessBuilder()
				.command(delCommandPartArray)
				.redirectOutput(ProcessBuilder.Redirect.DISCARD)
				.redirectError(ProcessBuilder.Redirect.DISCARD)
				.start();

		final int delExitCode = delProcess.waitFor();
		if (delExitCode != 0) {
			Logger.printError("failed to run del command");

		} else {
			final String[] rmDirCommandPartArray =
					{ "cmd", "/c", "rmdir", "/s", "/q", folderPathString };
			Logger.printProgress("executing command:");
			Logger.printLine(StringUtils.join(rmDirCommandPartArray, ' '));

			final Process rmDirProcess = new ProcessBuilder()
					.command(rmDirCommandPartArray)
					.redirectOutput(ProcessBuilder.Redirect.DISCARD)
					.redirectError(ProcessBuilder.Redirect.DISCARD)
					.start();

			final int rmDirExitCode = rmDirProcess.waitFor();
			if (rmDirExitCode != 0) {
				Logger.printError("failed to run rmdir command");
			} else {
				success = true;
			}
		}
		return success;
	}
}
