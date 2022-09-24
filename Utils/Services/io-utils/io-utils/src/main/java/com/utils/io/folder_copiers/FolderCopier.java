package com.utils.io.folder_copiers;

import com.utils.annotations.ApiMethod;

public interface FolderCopier {

    @ApiMethod
	void copyFolder(
			final String srcFolderPathString,
			final String dstFolderPathString,
			boolean verbose);
}
