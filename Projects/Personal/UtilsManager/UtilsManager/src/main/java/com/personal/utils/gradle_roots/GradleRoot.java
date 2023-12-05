package com.personal.utils.gradle_roots;

import java.util.Map;

import com.personal.utils.idea.IdeaFilesSynchronizer;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.file_copiers.FactoryFileCopier;
import com.utils.io.folder_copiers.FactoryFolderCopier;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.string.StrUtils;

public class GradleRoot {

	private final String rootFolderPathString;
	private final String commonBuildGradleFilePathString;
	private final String commonSettingsGradleFilePathString;
	private final String gitAttributesFilePathString;
	private final String allModulesFolderPathString;
	private final Map<String, String> moduleFolderPathsByNameMap;

	GradleRoot(
			final String rootFolderPathString,
			final String commonBuildGradleFilePathString,
			final String commonSettingsGradleFilePathString,
			final String gitAttributesFilePathString,
			final String allModulesFolderPathString,
			final Map<String, String> moduleFolderPathsByNameMap) {

		this.rootFolderPathString = rootFolderPathString;
		this.commonBuildGradleFilePathString = commonBuildGradleFilePathString;
		this.commonSettingsGradleFilePathString = commonSettingsGradleFilePathString;
		this.gitAttributesFilePathString = gitAttributesFilePathString;
		this.allModulesFolderPathString = allModulesFolderPathString;
		this.moduleFolderPathsByNameMap = moduleFolderPathsByNameMap;
	}

	public void synchronizeFrom(
			final GradleRoot srcGradleRoot) {

		synchronizeCommonFiles(srcGradleRoot);

		synchronizeBatchFiles(srcGradleRoot);
		synchronizeIdeaSettingsFiles(srcGradleRoot);

		for (final Map.Entry<String, String> mapEntry : moduleFolderPathsByNameMap.entrySet()) {

			final String moduleName = mapEntry.getKey();
			final String moduleFolderPathString = mapEntry.getValue();
			final String srcModuleFolderPathString =
					srcGradleRoot.moduleFolderPathsByNameMap.get(moduleName);
			if (srcModuleFolderPathString != null) {

				FactoryFolderCopier.getInstance().copyFolder(
						srcModuleFolderPathString, moduleFolderPathString, true, true, true);
			}
		}
	}

	private void synchronizeCommonFiles(
			final GradleRoot srcGradleRoot) {

		FactoryFileCopier.getInstance().copyFile(
				srcGradleRoot.commonBuildGradleFilePathString, commonBuildGradleFilePathString, true, true, true);
		FactoryFileCopier.getInstance().copyFile(
				srcGradleRoot.commonSettingsGradleFilePathString, commonSettingsGradleFilePathString, true, true, true);
		FactoryFileCopier.getInstance().copyFile(
				srcGradleRoot.gitAttributesFilePathString, gitAttributesFilePathString, true, true, true);
	}

	private void synchronizeBatchFiles(
			final GradleRoot srcGradleRoot) {

		final String srcAllModulesFolderPathString = srcGradleRoot.allModulesFolderPathString;
		if (srcAllModulesFolderPathString != null) {

			final String srcGitPushPathString =
					PathUtils.computePath(srcAllModulesFolderPathString, "git_push.bat");
			if (IoUtils.fileExists(srcGitPushPathString)) {

				final String dstAllModulesFolderPathString = allModulesFolderPathString;
				if (dstAllModulesFolderPathString != null) {

					final String dstGitPushPathString =
							PathUtils.computePath(dstAllModulesFolderPathString, "git_push.bat");

					if (!IoUtils.fileExists(dstGitPushPathString)) {

						final boolean createParentDirectoriesSuccess = FactoryFolderCreator.getInstance()
								.createParentDirectories(dstGitPushPathString, false, true);
						if (createParentDirectoriesSuccess) {

							FactoryFileCopier.getInstance().copyFile(
									srcGitPushPathString, dstGitPushPathString, false, true, true);
						}
					}
				}
			}
		}
	}

	private void synchronizeIdeaSettingsFiles(
			final GradleRoot srcGradleRoot) {

		final String srcAllModulesFolderPathString = srcGradleRoot.allModulesFolderPathString;
		if (srcAllModulesFolderPathString != null) {

			final String srcIdeaFolderPathString =
					PathUtils.computePath(srcAllModulesFolderPathString, ".idea");

			final String dstAllModulesFolderPathString = allModulesFolderPathString;
			if (dstAllModulesFolderPathString != null) {

				final String dstIdeaFolderPathString =
						PathUtils.computePath(dstAllModulesFolderPathString, ".idea");

				IdeaFilesSynchronizer.work(srcIdeaFolderPathString, dstIdeaFolderPathString);
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

	public Map<String, String> getModuleFolderPathsByNameMap() {
		return moduleFolderPathsByNameMap;
	}
}
