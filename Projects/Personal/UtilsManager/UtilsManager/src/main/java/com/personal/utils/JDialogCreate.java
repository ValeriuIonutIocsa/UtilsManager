package com.personal.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.tree.TreePath;

public class JDialogCreate extends JDialog {

	private final JCheckBoxTree jCheckBoxTree;

	JDialogCreate() {

		super((Window) null);

		setModal(true);
		setSize(500, 500);
		centerOnScreen(this);

		getContentPane().setLayout(new BorderLayout());

		jCheckBoxTree = new JCheckBoxTree();
		getContentPane().add(jCheckBoxTree);

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

		final JDialogCreate jDialogCreate = new JDialogCreate();
		jDialogCreate.setVisible(true);

		final TreePath[] checkedPaths = jDialogCreate.jCheckBoxTree.getCheckedPaths();
		for (final TreePath treePath : checkedPaths) {

			for (final Object pathPart : treePath.getPath()) {
				System.out.print(pathPart + ",");
			}
			System.out.println();
		}
	}
}
