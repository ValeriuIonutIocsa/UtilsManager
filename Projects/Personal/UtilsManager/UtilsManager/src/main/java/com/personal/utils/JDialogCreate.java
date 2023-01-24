package com.personal.utils;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.lang3.StringUtils;

import com.personal.utils.gradle.sub_prj.FactoryGradleSubProject;
import com.personal.utils.gradle.sub_prj.GradleSubProject;
import com.personal.utils.gradle_roots.FactoryGradleRoot;
import com.utils.io.PathUtils;
import com.utils.log.Logger;

public class JDialogCreate extends JDialog {

	private static final long serialVersionUID = -4309781150215172007L;

	private final JCheckBoxTree jCheckBoxTree;

	JDialogCreate(
			final MutableTreeNode rootMutableTreeNode) {

		super((Window) null);

		setTitle("Choose dependencies to be added:");
		setModal(true);
		setSize(640, 900);
		centerOnScreen(this);

		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 1 };
		gridBagLayout.rowWeights = new double[] { 1 };
		getContentPane().setLayout(gridBagLayout);

		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		final JScrollPane jScrollPane = new JScrollPane();
		jCheckBoxTree = new JCheckBoxTree();
		jScrollPane.setViewportView(jCheckBoxTree);
		getContentPane().add(jScrollPane, gridBagConstraints);

		final TreeModel treeModel = new DefaultTreeModel(rootMutableTreeNode);
		jCheckBoxTree.setModel(treeModel);

		for (int i = 0; i < jCheckBoxTree.getRowCount(); i++) {
			jCheckBoxTree.expandRow(i);
		}

		jCheckBoxTree.setRootVisible(false);
		jCheckBoxTree.setShowsRootHandles(true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private static void centerOnScreen(
			final Window frame) {

		final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		final int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		final int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	/**
	 * displays the dialog returns the selected paths in the JTree
	 */
	static void display(
			final Set<String> selectedModuleNameSet,
			final Set<String> selectedModuleNameWoDepsSet) {

		final String utilsRootPathString = FactoryGradleRoot.createUtilsRootPathString();
		final String projectPathString = PathUtils.computePath(utilsRootPathString,
				"Projects", "Personal", "UtilsManagerAllModules", "UtilsManagerAllModules");
		final Map<String, GradleSubProject> gradleSubProjectsByPathMap = new HashMap<>();
		FactoryGradleSubProject.newInstance(projectPathString, gradleSubProjectsByPathMap);

		final String projectName = PathUtils.computeFileName(projectPathString);
		final MutableTreeNode rootMutableTreeNode = new DefaultMutableTreeNode(
				new GradleProjectJTreeItem(projectName, projectPathString));

		final List<String> sortedProjectPathStringList = new ArrayList<>(gradleSubProjectsByPathMap.keySet());
		sortedProjectPathStringList.sort(
				Comparator.comparing(PathUtils::computeFileName, Comparator.reverseOrder()));

		for (final String dependencyProjectPathString : sortedProjectPathStringList) {

			if (dependencyProjectPathString.contains(File.separator + "Utils" + File.separator)) {

				final String dependencyProjectName = PathUtils.computeFileName(dependencyProjectPathString);

				final MutableTreeNode dependencyMutableTreeNode = new DefaultMutableTreeNode(
						new GradleProjectJTreeItem(dependencyProjectName, dependencyProjectPathString));
				rootMutableTreeNode.insert(dependencyMutableTreeNode, 0);

				createTreeNodesRec(dependencyProjectPathString, gradleSubProjectsByPathMap,
						dependencyMutableTreeNode);
			}
		}

		final JDialogCreate jDialogCreate = new JDialogCreate(rootMutableTreeNode);
		jDialogCreate.setVisible(true);

		final List<String> selectedTreePathList = new ArrayList<>();
		final TreePath[] checkedPaths = jDialogCreate.jCheckBoxTree.getCheckedPaths();
		for (final TreePath checkedPath : checkedPaths) {

			final List<String> treePathPartList = new ArrayList<>();
			for (final Object treePathPart : checkedPath.getPath()) {
				treePathPartList.add(treePathPart.toString());
			}
			final String treePath = StringUtils.join(treePathPartList, '>');
			selectedTreePathList.add(treePath);
		}

		fillSelectedModuleNameSet(selectedTreePathList, selectedModuleNameSet);

		Logger.printNewLine();
		Logger.printLine("selected module names:");
		for (final String selectedModuleName : selectedModuleNameSet) {
			Logger.printLine(selectedModuleName);
		}

		final Map<String, GradleSubProject> gradleSubProjectsByNameMap = new HashMap<>();
		fillGradleSubProjectsByNameMap(gradleSubProjectsByPathMap, gradleSubProjectsByNameMap);

		final Set<String> dependencyModuleNameSet = new HashSet<>();
		fillDependencyModuleNameSet(selectedModuleNameSet, gradleSubProjectsByNameMap, dependencyModuleNameSet);

		selectedModuleNameWoDepsSet.addAll(selectedModuleNameSet);
		selectedModuleNameWoDepsSet.removeAll(dependencyModuleNameSet);

		Logger.printNewLine();
		Logger.printLine("selected module names without dependencies:");
		for (final String selectedModuleName : selectedModuleNameWoDepsSet) {
			Logger.printLine(selectedModuleName);
		}
	}

	private static void createTreeNodesRec(
			final String projectPathString,
			final Map<String, GradleSubProject> gradleSubProjectsByPathMap,
			final MutableTreeNode parentMutableTreeNode) {

		final GradleSubProject gradleSubProject = gradleSubProjectsByPathMap.get(projectPathString);
		if (gradleSubProject != null) {

			final Set<String> dependencyPathSet = gradleSubProject.getDependencyPathSet();
			for (final String dependencyProjectPathString : dependencyPathSet) {

				final String dependencyProjectName = PathUtils.computeFileName(dependencyProjectPathString);

				final MutableTreeNode mutableTreeNode = new DefaultMutableTreeNode(
						new GradleProjectJTreeItem(dependencyProjectName, dependencyProjectPathString));
				parentMutableTreeNode.insert(mutableTreeNode, 0);
				createTreeNodesRec(dependencyProjectPathString,
						gradleSubProjectsByPathMap, mutableTreeNode);
			}
		}
	}

	private static void fillSelectedModuleNameSet(
			final List<String> selectedTreePathList,
			final Set<String> selectedProjectNameSet) {

		for (final String selectedTreePath : selectedTreePathList) {

			final String selectedProjectName;
			final int lastIndexOf = selectedTreePath.lastIndexOf('>');
			if (lastIndexOf >= 0) {
				selectedProjectName = selectedTreePath.substring(lastIndexOf + 1);
			} else {
				selectedProjectName = selectedTreePath;
			}
			selectedProjectNameSet.add(selectedProjectName);
		}
	}

	private static void fillGradleSubProjectsByNameMap(
			final Map<String, GradleSubProject> gradleSubProjectsByPathMap,
			final Map<String, GradleSubProject> gradleSubProjectsByNameMap) {

		for (final Map.Entry<String, GradleSubProject> mapEntry : gradleSubProjectsByPathMap.entrySet()) {

			final String subProjectPath = mapEntry.getKey();
			final GradleSubProject gradleSubProject = mapEntry.getValue();
			final String subProjectName = PathUtils.computeFileName(subProjectPath);
			gradleSubProjectsByNameMap.put(subProjectName, gradleSubProject);
		}
	}

	private static void fillDependencyModuleNameSet(
			final Set<String> selectedModuleNameSet,
			final Map<String, GradleSubProject> gradleSubProjectsByNameMap,
			final Set<String> dependencyModuleNameSet) {

		for (final String selectedModuleName : selectedModuleNameSet) {

			final GradleSubProject gradleSubProject = gradleSubProjectsByNameMap.get(selectedModuleName);
			if (gradleSubProject != null) {

				final Set<String> dependencyPathSet = gradleSubProject.getDependencyPathSet();
				for (final String dependencyPath : dependencyPathSet) {

					final String dependencyName = PathUtils.computeFileName(dependencyPath);
					dependencyModuleNameSet.add(dependencyName);
				}
			}
		}
	}
}
