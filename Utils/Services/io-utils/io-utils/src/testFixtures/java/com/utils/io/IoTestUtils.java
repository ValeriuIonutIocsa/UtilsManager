package com.utils.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.utils.log.Logger;

public final class IoTestUtils {

	private IoTestUtils() {
	}

	public static void checkFolderContentsEqual(
			final String folderPathString,
			final String otherFolderPathString,
			final List<FileCompareData> fileCompareDataList) throws Exception {

		Logger.printNewLine();
		Logger.printProgress("comparing folder contents:");
		Logger.printLine(folderPathString);
		Logger.printLine(otherFolderPathString);

		final List<String> filePathStringList;
		final boolean folderExists = IoUtils.directoryExists(folderPathString);
		if (folderExists) {
			filePathStringList = ListFileUtils.listFilesRecursively(folderPathString);
		} else {
			filePathStringList = new ArrayList<>();
		}

		final Set<String> matchedOtherFilePathStringSet = new HashSet<>();
		for (final String filePathString : filePathStringList) {

			if (!IoUtils.directoryExists(filePathString)) {

				final Path folderPath = Paths.get(folderPathString);
				final Path filePath = Paths.get(filePathString);
				final Path relativePath = folderPath.relativize(filePath);
				final String relativePathString = relativePath.toString();
				final Path otherFilePath = Paths.get(otherFolderPathString, relativePathString);
				final String otherFilePathString = otherFilePath.toString();
				matchedOtherFilePathStringSet.add(otherFilePathString);

				final boolean contentEquals =
						FileUtils.contentEquals(filePath.toFile(), otherFilePath.toFile());
				if (!contentEquals) {
					Logger.printWarning("The following files are different:" +
							System.lineSeparator() + filePath +
							System.lineSeparator() + otherFilePath);
				}

				final FileCompareData fileCompareData = new FileCompareData(
						filePathString, otherFilePathString, contentEquals);
				fileCompareDataList.add(fileCompareData);
			}
		}

		final List<String> otherFilePathStringList;
		final boolean otherFolderExists = IoUtils.directoryExists(otherFolderPathString);
		if (otherFolderExists) {
			otherFilePathStringList = ListFileUtils.listFilesRecursively(otherFolderPathString);
		} else {
			otherFilePathStringList = new ArrayList<>();
		}

		for (final String otherFilePathString : otherFilePathStringList) {

			if (!IoUtils.directoryExists(otherFilePathString)) {

				if (!matchedOtherFilePathStringSet.contains(otherFilePathString)) {

					final Path otherFolderPath = Paths.get(otherFolderPathString);
					final Path otherFilePath = Paths.get(otherFilePathString);
					final Path relativePath = otherFolderPath.relativize(otherFilePath);
					final String relativePathString = relativePath.toString();
					final Path filePath = Paths.get(folderPathString, relativePathString);
					final String filePathString = filePath.toString();

					final boolean contentEquals =
							FileUtils.contentEquals(filePath.toFile(), otherFilePath.toFile());
					if (!contentEquals) {
						Logger.printWarning("The following files are different:" +
								System.lineSeparator() + filePath +
								System.lineSeparator() + otherFilePath);
					}

					final FileCompareData fileCompareData = new FileCompareData(
							filePathString, otherFilePathString, contentEquals);
					fileCompareDataList.add(fileCompareData);
				}
			}
		}
	}
}
