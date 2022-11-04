package com.personal.utils;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.personal.utils.gradle.sub_prj.FactoryGradleSubProject;
import com.personal.utils.gradle.sub_prj.GradleSubProject;
import com.utils.io.PathUtils;

public class JDialogCreate extends JDialog {

	private final JCheckBoxTree jCheckBoxTree;

	JDialogCreate(
			final MutableTreeNode rootMutableTreeNode) {

		super((Window) null);

		setTitle("Choose dependencies to be added:");
		setModal(true);
		setSize(640, 750);
		centerOnScreen(this);

		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 1 };
		gridBagLayout.rowWeights = new double[] { 1 };
		getContentPane().setLayout(gridBagLayout);

		jCheckBoxTree = new JCheckBoxTree();
		final GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		final ScrollPane scrollPane = new ScrollPane();
		scrollPane.add(jCheckBoxTree);
		getContentPane().add(scrollPane, gridBagConstraints);

		final TreeModel treeModel = new DefaultTreeModel(rootMutableTreeNode);
		jCheckBoxTree.setModel(treeModel);

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

	static void display() {

		final String projectPathString = "C:\\IVI\\Prog\\JavaGradle\\UtilsManager\\" +
				"Projects\\Personal\\UtilsManagerAllModules\\UtilsManagerAllModules";
		final Map<String, GradleSubProject> gradleSubProjectsByPathMap = new HashMap<>();
		FactoryGradleSubProject.newInstance(projectPathString, gradleSubProjectsByPathMap);

		final MutableTreeNode rootMutableTreeNode = new DefaultMutableTreeNode(
				new GradleProjectJTreeItem(projectPathString));

		final List<String> sortedProjectPathStringList = new ArrayList<>(gradleSubProjectsByPathMap.keySet());
		sortedProjectPathStringList.sort(
				Comparator.comparing(PathUtils::computeFileName, Comparator.reverseOrder()));

		for (final String dependencyProjectPathString : sortedProjectPathStringList) {

			if (dependencyProjectPathString.contains(File.separator + "Utils" + File.separator)) {

				final MutableTreeNode dependencyMutableTreeNode = new DefaultMutableTreeNode(
						new GradleProjectJTreeItem(dependencyProjectPathString));
				rootMutableTreeNode.insert(dependencyMutableTreeNode, 0);

				createTreeNodesRec(dependencyProjectPathString,
						gradleSubProjectsByPathMap, dependencyMutableTreeNode);
			}
		}

		final JDialogCreate jDialogCreate = new JDialogCreate(rootMutableTreeNode);
		jDialogCreate.setVisible(true);

		final TreePath[] checkedPaths = jDialogCreate.jCheckBoxTree.getCheckedPaths();
		for (final TreePath treePath : checkedPaths) {

			for (final Object pathPart : treePath.getPath()) {
				System.out.print(pathPart + ",");
			}
			System.out.println();
		}
	}

	private static void createTreeNodesRec(
			final String projectPathString,
			final Map<String, GradleSubProject> gradleSubProjectsByPathMap,
			final MutableTreeNode parentMutableTreeNode) {

		final GradleSubProject gradleSubProject =
				gradleSubProjectsByPathMap.getOrDefault(projectPathString, null);
		if (gradleSubProject != null) {

			final Set<String> dependencyPathSet = gradleSubProject.getDependencyPathSet();
			for (final String dependencyProjectPathString : dependencyPathSet) {

				final MutableTreeNode mutableTreeNode = new DefaultMutableTreeNode(
						new GradleProjectJTreeItem(dependencyProjectPathString));
				parentMutableTreeNode.insert(mutableTreeNode, 0);
				createTreeNodesRec(dependencyProjectPathString, gradleSubProjectsByPathMap,
						mutableTreeNode);
			}
		}
	}
}
