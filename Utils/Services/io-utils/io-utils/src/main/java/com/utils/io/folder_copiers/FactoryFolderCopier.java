package com.utils.io.folder_copiers;

import com.utils.annotations.ApiMethod;

public class FactoryFolderCopier {

	private static FolderCopier instance = FolderCopierImpl.INSTANCE;

	private FactoryFolderCopier() {
	}

	@ApiMethod
	public static FolderCopier getInstance() {
		return instance;
	}

	@ApiMethod
	public static void setInstance(
			final FolderCopier instance) {
		FactoryFolderCopier.instance = instance;
	}
}
