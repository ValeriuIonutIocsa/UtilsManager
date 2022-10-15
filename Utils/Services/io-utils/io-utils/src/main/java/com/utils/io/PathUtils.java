package com.utils.io;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.utils.annotations.ApiMethod;
import com.utils.log.Logger;

public final class PathUtils {

	public static final String ROOT_PATH = createRootPath();

	private static String createRootPath() {

		final String rootPath;
		if (SystemUtils.IS_OS_WINDOWS) {
			rootPath = "D:\\";
		} else {
			rootPath = "/mnt/d";
		}
		return rootPath;
	}

	private PathUtils() {
	}

	@ApiMethod
	public static String computePath(
			final String firstPathString,
			final String... otherPathStringArray) {

		String resultPathString = null;
		try {
			final Path resultPath = Paths.get(firstPathString, otherPathStringArray);
			resultPathString = resultPath.toString();

		} catch (final Exception exc) {
			final StringBuilder sbErrorMessage = new StringBuilder("failed to compute path:");
			sbErrorMessage.append(System.lineSeparator()).append(firstPathString);
			for (final String otherPathString : otherPathStringArray) {
				sbErrorMessage.append(System.lineSeparator()).append(otherPathString);
			}
			Logger.printError(sbErrorMessage);
			Logger.printException(exc);
		}
		return resultPathString;
	}

	@ApiMethod
	public static String computeNamedPath(
			final String pathName,
			final String firstPathString,
			final String... otherPathStringArray) {

		String resultPathString = null;
		try {
			final Path resultPath = Paths.get(firstPathString, otherPathStringArray);
			resultPathString = resultPath.toString();

		} catch (final Exception exc) {
			final StringBuilder sbErrorMessage = new StringBuilder("failed to compute ");
			sbErrorMessage.append(pathName).append(" path:")
					.append(System.lineSeparator()).append(firstPathString);
			for (final String otherPathString : otherPathStringArray) {
				sbErrorMessage.append(System.lineSeparator()).append(otherPathString);
			}
			Logger.printError(sbErrorMessage);
			Logger.printException(exc);
		}
		return resultPathString;
	}

	@ApiMethod
	public static String computeNormalizedPathString(
			final String firstPathString,
			final String... otherPathStringArray) {

		String normalizedPathString = null;
		try {
			final Path path = Paths.get(firstPathString, otherPathStringArray);
			final Path absolutePath = path.toAbsolutePath();
			final String absolutePathString = absolutePath.toString();
			normalizedPathString = FilenameUtils.normalize(absolutePathString);

		} catch (final Exception exc) {
			final StringBuilder sbErrorMessage = new StringBuilder("failed to compute path:");
			sbErrorMessage.append(System.lineSeparator()).append(firstPathString);
			for (final String otherPathString : otherPathStringArray) {
				sbErrorMessage.append(System.lineSeparator()).append(otherPathString);
			}
			Logger.printError(sbErrorMessage);
			Logger.printException(exc);
		}
		return normalizedPathString;
	}

	@ApiMethod
	public static String computeNamedNormalizedPathString(
			final String pathName,
			final String firstPathString,
			final String... otherPathStringArray) {

		String normalizedPathString = null;
		try {
			final Path path = Paths.get(firstPathString, otherPathStringArray);
			final Path absolutePath = path.toAbsolutePath();
			final String absolutePathString = absolutePath.toString();
			normalizedPathString = FilenameUtils.normalize(absolutePathString);

		} catch (final Exception exc) {
			final StringBuilder sbErrorMessage = new StringBuilder("failed to compute ");
			sbErrorMessage.append(pathName).append(" path:")
					.append(System.lineSeparator()).append(firstPathString);
			for (final String otherPathString : otherPathStringArray) {
				sbErrorMessage.append(System.lineSeparator()).append(otherPathString);
			}
			Logger.printError(sbErrorMessage);
			Logger.printException(exc);
		}
		return normalizedPathString;
	}

	@ApiMethod
	public static String computeFileName(
			final Path path) {

		final String pathString = String.valueOf(path);
		return computeFileName(pathString);
	}

	@ApiMethod
	public static String computeFileName(
			final String pathString) {
		return FilenameUtils.getName(pathString);
	}

	@ApiMethod
	public static String computeParentPathString(
			final String pathString) {

		String folderPathString = null;
		try {
			final Path path = Paths.get(pathString);
			final Path parentPath = path.getParent();
			if (parentPath != null) {
				folderPathString = parentPath.toString();
			}

		} catch (final Exception ignored) {
		}
		return folderPathString;
	}

	@ApiMethod
	public static String computeExtension(
			final Path path) {

		final String pathString = path.toString();
		return computeExtension(pathString);
	}

	@ApiMethod
	public static String computeExtension(
			final String pathString) {
		return FilenameUtils.getExtension(pathString);
	}

	@ApiMethod
	public static String computePathWoExt(
			final Path path) {

		String pathWoExt = null;
		if (path != null) {
			final String pathString = path.toString();
			pathWoExt = computePathWoExt(pathString);
		}
		return pathWoExt;
	}

	@ApiMethod
	public static String computePathWoExt(
			final String pathString) {
		return FilenameUtils.removeExtension(pathString);
	}

	@ApiMethod
	public static String computeFileNameWoExt(
			final Path path) {

		String fileNameWoExt = null;
		if (path != null) {

			final String pathString = path.toString();
			fileNameWoExt = computeFileNameWoExt(pathString);
		}
		return fileNameWoExt;
	}

	@ApiMethod
	public static String computeFileNameWoExt(
			final String pathString) {
		return FilenameUtils.getBaseName(pathString);
	}

	@ApiMethod
	public static String appendFileNameSuffix(
			final String pathString,
			final String suffix) {

		final String resultPathString;
		final String pathStringWoExt = PathUtils.computePathWoExt(pathString);
		final String extension = PathUtils.computeExtension(pathString);
		if (StringUtils.isNotEmpty(extension)) {
			resultPathString = pathStringWoExt + suffix + "." + extension;
		} else {
			resultPathString = pathStringWoExt + suffix;
		}
		return resultPathString;
	}

	@ApiMethod
	public static String computeRelativePathString(
			final String fromPathString,
			final String toPathString) {

		String relativePathString = null;
		try {
			final Path fromPath = Paths.get(fromPathString);
			final Path toPath = Paths.get(toPathString);
			final Path relativePath = fromPath.relativize(toPath);
			relativePathString = relativePath.toString();

		} catch (final Exception ignored) {
		}
		return relativePathString;
	}
}
