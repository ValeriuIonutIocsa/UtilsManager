package com.personal.utils.gradle_roots;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.utils.io.folder_deleters.FactoryFolderDeleter;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class GradleRoot {

	private final String commonBuildGradleFilePathString;
	private final String commonSettingsGradleFilePathString;
	private final String gitAttributesFilePathString;
	private final Map<String, String> moduleFolderPathsByNameMap;

	GradleRoot(
			final String commonBuildGradleFilePathString,
			final String commonSettingsGradleFilePathString,
			final String gitAttributesFilePathString,
			final Map<String, String> moduleFolderPathsByNameMap) {

		this.commonBuildGradleFilePathString = commonBuildGradleFilePathString;
		this.commonSettingsGradleFilePathString = commonSettingsGradleFilePathString;
		this.gitAttributesFilePathString = gitAttributesFilePathString;
		this.moduleFolderPathsByNameMap = moduleFolderPathsByNameMap;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public void synchronizeFrom(
			final GradleRoot srcGradleRoot) {

		copyFile(srcGradleRoot.commonBuildGradleFilePathString, commonBuildGradleFilePathString);
		copyFile(srcGradleRoot.commonSettingsGradleFilePathString, commonSettingsGradleFilePathString);
		copyFile(srcGradleRoot.gitAttributesFilePathString, gitAttributesFilePathString);

		for (final Map.Entry<String, String> mapEntry : moduleFolderPathsByNameMap.entrySet()) {

			final String moduleName = mapEntry.getKey();
			final String moduleFolderPathString = mapEntry.getValue();
			final String srcModuleFolderPathString =
					srcGradleRoot.moduleFolderPathsByNameMap.getOrDefault(moduleName, null);
			if (srcModuleFolderPathString != null) {

				copyFolder(srcModuleFolderPathString, moduleFolderPathString);
			}
		}
	}

	private static void copyFile(
			final String srcFilePathString,
			final String dstFilePathString) {

		try {
			Logger.printProgress("copying file:");
			Logger.printLine(dstFilePathString);

			final Path srcFilePath = Paths.get(srcFilePathString);
			final Path dstFilePath = Paths.get(dstFilePathString);
			Files.copy(srcFilePath, dstFilePath, StandardCopyOption.REPLACE_EXISTING);

		} catch (final Exception exc) {
			Logger.printError("failed to copy file:" + System.lineSeparator() + dstFilePathString);
			Logger.printException(exc);
		}
	}

	private void copyFolder(
			final String srcModuleFolderPathString,
			final String dstModuleFolderPathString) {

		try {
			Logger.printProgress("copying folder:");
			Logger.printLine(dstModuleFolderPathString);

			final Path dstModuleFolderPath = Paths.get(dstModuleFolderPathString);
			final boolean deleteFolderSuccess = FactoryFolderDeleter.getInstance()
					.deleteFolder(dstModuleFolderPath, true);
			if (deleteFolderSuccess) {

				final File srcModuleFolder = new File(srcModuleFolderPathString);
				final File dstModuleFolder = dstModuleFolderPath.toFile();
				FileUtils.copyDirectory(srcModuleFolder, dstModuleFolder);
			}

		} catch (final Exception exc) {
			Logger.printError("failed to copy folder:" + System.lineSeparator() + dstModuleFolderPathString);
			Logger.printException(exc);
		}
	}
}
