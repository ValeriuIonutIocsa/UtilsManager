package com.utils.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.utils.annotations.ApiMethod;
import com.utils.log.Logger;

public final class ReaderUtils {

	private ReaderUtils() {
	}

	public static BufferedReader openBufferedReader(
			final String filePathString) throws IOException {

		return openBufferedReader(filePathString, StandardCharsets.UTF_8);
	}

	public static BufferedReader openBufferedReader(
			final String filePathString,
			final Charset charset) throws IOException {

		final Path filePath = Paths.get(filePathString);
		return Files.newBufferedReader(filePath, charset);
	}

	@ApiMethod
	public static byte[] tryFileToByteArray(
			final String filePathString) {

		byte[] byteArray = null;
		try {
			final Path filePath = Paths.get(filePathString);
			byteArray = Files.readAllBytes(filePath);

		} catch (final Throwable throwable) {
			Logger.printError("failed to read byte array from file:" +
					System.lineSeparator() + filePathString);
			Logger.printThrowable(throwable);
		}
		return byteArray;
	}

	@ApiMethod
	public static String tryFileToString(
			final String filePathString) {

		String str = null;
		try {
			str = fileToString(filePathString);

		} catch (final Throwable throwable) {
			Logger.printError("failed to read contents of file:" +
					System.lineSeparator() + filePathString);
			Logger.printThrowable(throwable);
		}
		return str;
	}

	@ApiMethod
	public static String tryFileToString(
			final String filePathString,
			final Charset charset) {

		String str = null;
		try {
			str = fileToString(filePathString, charset);

		} catch (final Throwable throwable) {
			Logger.printError("failed to read contents of file:" +
					System.lineSeparator() + filePathString);
			Logger.printThrowable(throwable);
		}
		return str;
	}

	@ApiMethod
	public static String fileToString(
			final String filePathString) throws Exception {

		return fileToString(filePathString, StandardCharsets.UTF_8);
	}

	@ApiMethod
	public static String fileToString(
			final String filePathString,
			final Charset charset) throws Exception {

		try (InputStream inputStream = StreamUtils.openInputStream(filePathString)) {
			return inputStreamToString(inputStream, charset.name());
		}
	}

	@ApiMethod
	public static String inputStreamToString(
			final InputStream inputStream) throws IOException {

		return inputStreamToString(inputStream, StandardCharsets.UTF_8.name());
	}

	@ApiMethod
	public static String inputStreamToString(
			final InputStream inputStream,
			final String encoding) throws IOException {

		return IOUtils.toString(inputStream, encoding);
	}

	@ApiMethod
	public static List<String> tryFileToLineList(
			final String filePathString,
			final Charset charset) {

		final List<String> lineList = new ArrayList<>();
		try (BufferedReader bufferedReader = ReaderUtils.openBufferedReader(filePathString, charset)) {

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				lineList.add(line);
			}

		} catch (final Throwable throwable) {
			Logger.printError("failed to read contents of file:" +
					System.lineSeparator() + filePathString);
			Logger.printThrowable(throwable);
		}
		return lineList;
	}
}
