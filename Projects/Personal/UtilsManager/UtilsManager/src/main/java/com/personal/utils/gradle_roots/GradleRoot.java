package com.personal.utils.gradle_roots;

import java.util.Map;

import com.personal.utils.idea.IdeaFilesSynchronizer;
import com.utils.io.PathUtils;
import com.utils.io.file_copiers.FactoryFileCopier;
import com.utils.io.folder_copiers.FactoryFolderCopier;
import com.utils.log.Logger;
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

		synchronizeCommonFiles(srcGradleRoot);

		synchronizeIdeaSettingsFiles(srcGradleRoot);

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

	private void synchronizeCommonFiles(
			final GradleRoot srcGradleRoot) {

		FactoryFileCopier.getInstance().copyFile(
				srcGradleRoot.commonBuildGradleFilePathString, commonBuildGradleFilePathString, true, true);
		FactoryFileCopier.getInstance().copyFile(
				srcGradleRoot.commonSettingsGradleFilePathString, commonSettingsGradleFilePathString, true, true);
		FactoryFileCopier.getInstance().copyFile(
				srcGradleRoot.gitAttributesFilePathString, gitAttributesFilePathString, true, true);
	}

	private void synchronizeIdeaSettingsFiles(
			final GradleRoot srcGradleRoot) {

        final String srcAllModulesFolderPathString = srcGradleRoot.createAllModulesFolderPathString();
        final String srcIdeaFolderPathString =
                PathUtils.computePath(srcAllModulesFolderPathString, ".idea");

        final String dstAllModulesFolderPathString = createAllModulesFolderPathString();
        final String dstIdeaFolderPathString =
                PathUtils.computePath(dstAllModulesFolderPathString, ".idea");

		IdeaFilesSynchronizer.work(srcIdeaFolderPathString, dstIdeaFolderPathString);
	}

	private String createAllModulesFolderPathString() {

		String allModulesPathString = null;
		for (final Map.Entry<String, String> mapEntry : moduleFolderPathsByNameMap.entrySet()) {

			final String moduleName = mapEntry.getKey();
			if (moduleName.endsWith("AllModules")) {

				allModulesPathString = mapEntry.getValue();
				break;
			}
		}

		if (allModulesPathString == null) {

			final String rootModuleName = PathUtils.computeFileName(rootFolderPathString);
			for (final Map.Entry<String, String> mapEntry : moduleFolderPathsByNameMap.entrySet()) {

				final String moduleName = mapEntry.getKey();
				if (rootModuleName.equals(moduleName)) {

					allModulesPathString = mapEntry.getValue();
					break;
				}
			}
		}

		if (allModulesPathString == null) {
			Logger.printError("could not compute " +
					"all modules folder path for project " + rootFolderPathString);
		}

		return allModulesPathString;
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
