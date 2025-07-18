package com.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.time.Instant;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.io.processes.InputStreamReaderThread;
import com.utils.io.processes.ReadBytesHandlerLinesPrint;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class IoUtils {

	private IoUtils() {
	}

	/**
	 * @param pathString
	 *            the input path
	 * @return false if the path is null, blank or if the file does not exist, true otherwise
	 */
	@ApiMethod
	public static boolean fileExists(
			final String pathString) {

		final boolean fileExists = false;
		try {
			return StringUtils.isNotBlank(pathString) && Files.exists(Paths.get(pathString));

		} catch (final Throwable ignored) {
		}
		return fileExists;
	}

	/**
	 * @param pathString
	 *            the input path
	 * @return false if the path is null, blank or if the file is a not an existing regular file, true otherwise
	 */
	@ApiMethod
	public static boolean regularFileExists(
			final String pathString) {

		boolean regularFileExists = false;
		try {
			regularFileExists = StringUtils.isNotBlank(pathString) && Files.isRegularFile(Paths.get(pathString));

		} catch (final Throwable ignored) {
		}
		return regularFileExists;
	}

	/**
	 * @param pathString
	 *            the input path
	 * @return false if the path is null, blank or if the file is not a directory, true otherwise
	 */
	@ApiMethod
	public static boolean directoryExists(
			final String pathString) {

		boolean directoryExists = false;
		try {
			directoryExists = StringUtils.isNotBlank(pathString) && Files.isDirectory(Paths.get(pathString));

		} catch (final Throwable ignored) {
		}
		return directoryExists;
	}

	@ApiMethod
	public static boolean checkFileHidden(
			final String pathString) {

		boolean fileHidden = false;
		try {
			if (StringUtils.isNotBlank(pathString)) {

				final Path path = Paths.get(pathString);
				fileHidden = checkFileHidden(path);
			}

		} catch (final Throwable ignored) {
		}
		return fileHidden;
	}

	@ApiMethod
	public static boolean checkFileHidden(
			final Path path) {

		boolean fileHidden = false;
		if (Files.exists(path)) {
			fileHidden = checkFileHiddenNoChecks(path);
		}
		return fileHidden;
	}

	@ApiMethod
	public static boolean checkFileHiddenNoChecks(
			final Path path) {

		boolean fileHidden = false;
		try {
			if (Files.isHidden(path)) {
				fileHidden = true;
			}

		} catch (final Throwable ignored) {
		}
		return fileHidden;
	}

	@ApiMethod
	public static long computeFileLastModifiedTime(
			final String filePathString) {

		long lastModifiedTime = -1;
		try {
			final Path filePath = Paths.get(filePathString);
			lastModifiedTime = Files.getLastModifiedTime(filePath).toMillis();

		} catch (final Throwable throwable) {
			Logger.printError("failed to compute last modified time of file:" +
					System.lineSeparator() + filePathString);
			Logger.printThrowable(throwable);
		}
		return lastModifiedTime;
	}

	@ApiMethod
	public static void configureFileLastModifiedTime(
			final String filePathString,
			final Instant lastModifiedTimeInstant) {

		try {
			final Path filePath = Paths.get(filePathString);
			Files.setLastModifiedTime(filePath, FileTime.from(lastModifiedTimeInstant));

		} catch (final Throwable throwable) {
			Logger.printError("failed to configure last modified time of file:" +
					System.lineSeparator() + filePathString);
			Logger.printThrowable(throwable);
		}
	}

	@ApiMethod
	public static byte[] fileMd5HashCode(
			final String filePathString) {

		byte[] md5HashCode = null;
		try {
			final byte[] fileBytes = ReaderUtils.tryFileToByteArray(filePathString);
			final MessageDigest messageDigestMd5 = MessageDigest.getInstance("MD5");
			md5HashCode = messageDigestMd5.digest(fileBytes);

		} catch (final Throwable throwable) {
			Logger.printError("failed to compute MD5 hash code of file:" +
					System.lineSeparator() + filePathString);
			Logger.printThrowable(throwable);
		}
		return md5HashCode;
	}

	@ApiMethod
	public static int computeLineCount(
			final String filePathString) {

		int lineCount = 0;
		try (BufferedReader bufferedReader = ReaderUtils.openBufferedReader(filePathString)) {

			while (bufferedReader.readLine() != null) {
				lineCount++;
			}

		} catch (final Throwable throwable) {
			Logger.printError("failed to compute line count of file:" +
					System.lineSeparator() + filePathString);
			Logger.printThrowable(throwable);
		}
		return lineCount;
	}

	@ApiMethod
	public static String createTmpFile(
			final String filePathString,
			final String tmpFilePathString) {

		String resultTmpFilePathString = null;
		try {
			final File tmpFile;
			if (tmpFilePathString != null) {
				tmpFile = new File(tmpFilePathString);
			} else {
				tmpFile = File.createTempFile(StrUtils.createDateTimeString(), ".tmp");
			}

			tmpFile.deleteOnExit();

			try (InputStream inputStream = StreamUtils.openInputStream(filePathString);
					OutputStream outputStream = Files.newOutputStream(tmpFile.toPath())) {
				IOUtils.copy(inputStream, outputStream);
			}
			resultTmpFilePathString = tmpFile.getPath();

		} catch (final Throwable throwable) {
			Logger.printError("failed to create temporary file");
			Logger.printThrowable(throwable);
		}
		return resultTmpFilePathString;
	}

	@ApiMethod
	public static String createTmpFile(
			final InputStream inputStream,
			final String tmpFilePathString) {

		String resultTmpFilePathString = null;
		try {
			final File tmpFile;
			if (tmpFilePathString != null) {
				tmpFile = new File(tmpFilePathString);
			} else {
				tmpFile = File.createTempFile(StrUtils.createDateTimeString(), ".tmp");
			}

			tmpFile.deleteOnExit();

			try (OutputStream outputStream = Files.newOutputStream(tmpFile.toPath())) {
				IOUtils.copy(inputStream, outputStream);
			}
			resultTmpFilePathString = tmpFile.getPath();

		} catch (final Throwable throwable) {
			Logger.printError("failed to create temporary file");
			Logger.printThrowable(throwable);
		}
		return resultTmpFilePathString;
	}

	@ApiMethod
	public static boolean openFileWithDefaultApp(
			final String filePathString) {

		boolean success = false;
		try {
			final Process process = new ProcessBuilder()
					.command("cmd", "/c", "start", "open file with default app", filePathString)
					.redirectErrorStream(true)
					.start();

			final InputStreamReaderThread inputStreamReaderThread = new InputStreamReaderThread(
					"open file with default app input stream reader", process.getInputStream(),
					StandardCharsets.UTF_8, new ReadBytesHandlerLinesPrint());
			inputStreamReaderThread.start();

			final int exitCode = process.waitFor();
			inputStreamReaderThread.join();

			success = exitCode == 0;

		} catch (final Throwable throwable) {
			Logger.printError("failed to open file with default app:" +
					System.lineSeparator() + filePathString);
			Logger.printThrowable(throwable);
		}
		return success;
	}

	@ApiMethod
	public static void selectFileInExplorer(
			final String filePathString,
			final String appFolderPathString) {

		String tmpBatFilePathString = null;
		try {
			final String pathDateTimeString = StrUtils.createPathDateTimeString();
			tmpBatFilePathString =
					PathUtils.computePath(appFolderPathString, pathDateTimeString + ".bat");

			WriterUtils.tryStringToFile("start explorer /select,\"" + filePathString + "\"",
					StandardCharsets.UTF_8, tmpBatFilePathString);

			final Process process = new ProcessBuilder()
					.command("cmd", "/c", tmpBatFilePathString)
					.inheritIO()
					.start();
			process.waitFor();

		} catch (final Throwable throwable) {
			Logger.printError("failed to select file in explorer");
			Logger.printThrowable(throwable);

		} finally {
			if (IoUtils.fileExists(tmpBatFilePathString)) {
				FactoryFileDeleter.getInstance().deleteFile(tmpBatFilePathString, false, true);
			}
		}
	}
}
