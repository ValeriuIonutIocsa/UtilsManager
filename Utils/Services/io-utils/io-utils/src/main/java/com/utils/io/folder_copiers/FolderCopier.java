package com.utils.io.folder_copiers;

public interface FolderCopier {

	void copyFolder(
			final String srcFolderPathString,
			final String dstFolderPathString);
}
