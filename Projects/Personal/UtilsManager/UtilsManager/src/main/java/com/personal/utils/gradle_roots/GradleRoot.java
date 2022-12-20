package com.personal.utils.gradle_roots;

import java.util.Map;

import com.utils.io.file_copiers.FactoryFileCopier;
import com.utils.io.folder_copiers.FactoryFolderCopier;
import com.utils.string.StrUtils;

public class GradleRoot {

	private final String rootFolderPathString;
	private final String commonBuildGradleFilePathString;
	private final String commonSettingsGradleFilePathString;
	private final String gitAttributesFilePathString;
	private final Map<String, String> moduleFolderPathsByNameMap;

	GradleRoot(
			final String rootFolderPathString,
			final String commonBuildGradleFilePathString,
			final String commonSettingsGradleFilePathString,
			final String gitAttributesFilePathString,
			final Map<String, String> moduleFolderPathsByNameMap) {

		this.rootFolderPathString = rootFolderPathString;
		this.commonBuildGradleFilePathString = commonBuildGradleFilePathString;
		this.commonSettingsGradleFilePathString = commonSettingsGradleFilePathString;
		this.gitAttributesFilePathString = gitAttributesFilePathString;
		this.moduleFolderPathsByNameMap = moduleFolderPathsByNameMap;
	}

	public void synchronizeFrom(
			final GradleRoot srcGradleRoot) {

		FactoryFileCopier.getInstance().copyFile(
				srcGradleRoot.commonBuildGradleFilePathString, commonBuildGradleFilePathString, true, true);
		FactoryFileCopier.getInstance().copyFile(
				srcGradleRoot.commonSettingsGradleFilePathString, commonSettingsGradleFilePathString, true, true);
		FactoryFileCopier.getInstance().copyFile(
				srcGradleRoot.gitAttributesFilePathString, gitAttributesFilePathString, true, true);

		for (final Map.Entry<String, String> mapEntry : moduleFolderPathsByNameMap.entrySet()) {

			final String moduleName = mapEntry.getKey();
			final String moduleFolderPathString = mapEntry.getValue();
			final String srcModuleFolderPathString =
					srcGradleRoot.moduleFolderPathsByNameMap.get(moduleName);
			if (srcModuleFolderPathString != null) {

				FactoryFolderCopier.getInstance().copyFolder(
						srcModuleFolderPathString, moduleFolderPathString, true);
			}
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public String getRootFolderPathString() {
		return rootFolderPathString;
	}

	public String getCommonBuildGradleFilePathString() {
		return commonBuildGradleFilePathString;
	}

	public String getCommonSettingsGradleFilePathString() {
		return commonSettingsGradleFilePathString;
	}

	public String getGitAttributesFilePathString() {
		return gitAttributesFilePathString;
	}

	public Map<String, String> getModuleFolderPathsByNameMap() {
		return moduleFolderPathsByNameMap;
	}
}
