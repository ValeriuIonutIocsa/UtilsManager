package com.utils.io.folder_copiers;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.utils.io.folder_deleters.FactoryFolderDeleter;
import com.utils.log.Logger;

public class FolderCopierImpl implements FolderCopier {

	static final FolderCopierImpl INSTANCE = new FolderCopierImpl();

	private FolderCopierImpl() {
	}

	@Override
	public void copyFolder(
			final String srcFolderPathString,
			final String dstFolderPathString) {

		try {
			Logger.printProgress("copying folder:");
			Logger.printLine(srcFolderPathString);

			final boolean deleteFolderSuccess = FactoryFolderDeleter.getInstance()
					.deleteFolder(dstFolderPathString, true);
			if (deleteFolderSuccess) {

				final File srcFolder = new File(srcFolderPathString);
				final File dstFolder = new File(dstFolderPathString);
				FileUtils.copyDirectory(srcFolder, dstFolder);
			}

		} catch (final Exception exc) {
			Logger.printError("failed to copy folder:" + System.lineSeparator() + dstFolderPathString);
			Logger.printException(exc);
		}
	}
}
