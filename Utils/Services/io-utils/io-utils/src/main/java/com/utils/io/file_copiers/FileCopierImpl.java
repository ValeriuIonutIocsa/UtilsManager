package com.utils.io.file_copiers;

import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.utils.annotations.ApiMethod;
import com.utils.io.IoUtils;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public final class FileCopierImpl implements FileCopier {

	static final FileCopierImpl INSTANCE = new FileCopierImpl();

	private FileCopierImpl() {
	}

	@ApiMethod
	@Override
	public boolean copyFile(
			final String srcFilePathString,
			final String dstFilePathString,
			final boolean copyAttributes,
			final boolean verbose) {

		final boolean dstFileExists = IoUtils.fileExists(dstFilePathString);
		return copyFileNoChecks(srcFilePathString, dstFilePathString,
				dstFileExists, copyAttributes, verbose);
	}

	@ApiMethod
	@Override
	public boolean copyFileNoChecks(
			final String srcFilePathString,
			final String dstFilePathString,
			final boolean dstFileExists,
			final boolean copyAttributes,
			final boolean verbose) {

		boolean success = false;
		try {
			if (dstFileExists) {
				FactoryReadOnlyFlagClearer.getInstance()
						.clearReadOnlyFlagFileNoChecks(dstFilePathString, true);
			}

			final List<CopyOption> copyOptionList = new ArrayList<>();
			copyOptionList.add(StandardCopyOption.REPLACE_EXISTING);
			if (copyAttributes) {
				copyOptionList.add(StandardCopyOption.COPY_ATTRIBUTES);
			}
			final CopyOption[] copyOptionArray = copyOptionList.toArray(new CopyOption[] {});

			final Path srcFilePath = Paths.get(srcFilePathString);
			final Path dstFilePath = Paths.get(dstFilePathString);
			Files.copy(srcFilePath, dstFilePath, copyOptionArray);
			success = true;

		} catch (final Exception exc) {
			if (verbose) {
				Logger.printError("failed to copy file " +
						System.lineSeparator() + srcFilePathString +
						System.lineSeparator() + "to:" +
						System.lineSeparator() + dstFilePathString);
			}
			Logger.printException(exc);
		}
		return success;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
