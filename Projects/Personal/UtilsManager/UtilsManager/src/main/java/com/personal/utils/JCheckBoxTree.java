package com.personal.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class JCheckBoxTree extends JTree {

	private static final long serialVersionUID = -4194122328392241790L;

	JCheckBoxTree selfPointer = this;

	// Defining data structure that will enable to fast check-indicate the state of each node
	// It totally replaces the "selection" mechanism of the JTree
	private static class CheckedNode {

		boolean isSelected;
		boolean hasChildren;
		boolean allChildrenSelected;

		public CheckedNode(
				final boolean isSelected,
				final boolean hasChildren,
				final boolean allChildrenSelected) {

			this.isSelected = isSelected;
			this.hasChildren = hasChildren;
			this.allChildrenSelected = allChildrenSelected;
		}
	}

	Map<TreePath, CheckedNode> nodesCheckingState;
	Set<TreePath> checkedPaths = new HashSet<>();

	// Defining a new event type for the checking mechanism and preparing event-handling mechanism
	protected EventListenerList listenerList = new EventListenerList();

	public static class CheckChangeEvent extends EventObject {

		private static final long serialVersionUID = -8100230309044193368L;

		public CheckChangeEvent(
				final Object source) {
			super(source);
		}
	}

	public interface CheckChangeEventListener extends EventListener {

		void checkStateChanged(
				CheckChangeEvent event);
	}

	public void addCheckChangeEventListener(
			final CheckChangeEventListener listener) {
		listenerList.add(CheckChangeEventListener.class, listener);
	}

	public void removeCheckChangeEventListener(
			final CheckChangeEventListener listener) {
		listenerList.remove(CheckChangeEventListener.class, listener);
	}

	void fireCheckChangeEvent(
			final CheckChangeEvent evt) {
		final Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i++) {
			if (listeners[i] == CheckChangeEventListener.class) {
				((CheckChangeEventListener) listeners[i + 1]).checkStateChanged(evt);
			}
		}
	}

	@Override
	public void setModel(
			final TreeModel newModel) {

		super.setModel(newModel);

		resetCheckingState();
	}

	// New method that returns only the checked paths (totally ignores original "selection" mechanism)
	public TreePath[] getCheckedPaths() {

		return checkedPaths.toArray(new TreePath[0]);
	}

	// Returns true in case that the node is selected, has children but not all of them are selected
	public boolean isSelectedPartially(
			final TreePath path) {

		final CheckedNode cn = nodesCheckingState.get(path);
		return cn.isSelected && cn.hasChildren && !cn.allChildrenSelected;
	}

	private void resetCheckingState() {

		nodesCheckingState = new HashMap<>();
		checkedPaths = new HashSet<>();
		final DefaultMutableTreeNode node = (DefaultMutableTreeNode) getModel().getRoot();
		if (node == null) {
			return;
		}
		addSubtreeToCheckingStateTracking(node);
	}

	// Creating data structure of the current model for the checking mechanism
	private void addSubtreeToCheckingStateTracking(
			final DefaultMutableTreeNode node) {

		final TreeNode[] path = node.getPath();
		final TreePath tp = new TreePath(path);
		final CheckedNode cn = new CheckedNode(false, node.getChildCount() > 0, false);
		nodesCheckingState.put(tp, cn);
		for (int i = 0; i < node.getChildCount(); i++) {
			addSubtreeToCheckingStateTracking((DefaultMutableTreeNode) tp.pathByAddingChild(node.getChildAt(i))
					.getLastPathComponent());
		}
	}

	// Overriding cell renderer by a class that ignores the original "selection" mechanism
	// It decides how to show the nodes due to the checking-mechanism
	private class CheckBoxCellRenderer extends JPanel implements TreeCellRenderer {

		private static final long serialVersionUID = -7341833835878991719L;
		JCheckBox checkBox;

		public CheckBoxCellRenderer() {

			super();

			setLayout(new BorderLayout());
			checkBox = new JCheckBox();
			checkBox.setPreferredSize(new Dimension(600, 20));
			add(checkBox, BorderLayout.CENTER);
			setOpaque(false);
		}

		@Override
		public Component getTreeCellRendererComponent(
				final JTree tree,
				final Object value,
				final boolean selected,
				final boolean expanded,
				final boolean leaf,
				final int row,
				final boolean hasFocus) {

			final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			final Object obj = node.getUserObject();
			final TreePath tp = new TreePath(node.getPath());
			final CheckedNode cn = nodesCheckingState.get(tp);
			if (cn != null) {

				checkBox.setSelected(cn.isSelected);
				checkBox.setText(obj.toString());
				checkBox.setOpaque(cn.isSelected && cn.hasChildren && !cn.allChildrenSelected);
			}
			return this;
		}
	}

	public JCheckBoxTree() {

		super();

		// Disabling toggling by double-click
		setToggleClickCount(0);

		// Overriding cell renderer by new one defined above
		final CheckBoxCellRenderer cellRenderer = new CheckBoxCellRenderer();
		setCellRenderer(cellRenderer);

		// Overriding selection model by an empty one
		final DefaultTreeSelectionModel defaultTreeSelectionModel = new DefaultTreeSelectionModel() {

			private static final long serialVersionUID = -8190634240451667286L;

			// Totally disabling the selection mechanism
			@Override
			public void setSelectionPath(
					final TreePath path) {
			}

			@Override
			public void addSelectionPath(
					final TreePath path) {
			}

			@Override
			public void removeSelectionPath(
					final TreePath path) {
			}

			@Override
			public void setSelectionPaths(
					final TreePath[] pPaths) {
			}
		};

		// Calling checking mechanism on mouse click
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(
					final MouseEvent arg0) {

				final TreePath treePath = selfPointer.getPathForLocation(arg0.getX(), arg0.getY());
				if (treePath == null) {
					return;
				}

				final boolean checkMode = !nodesCheckingState.get(treePath).isSelected;
				checkSubTree(treePath, checkMode);

				// Firing the check change event
				fireCheckChangeEvent(new CheckChangeEvent(new Object()));

				// Repainting tree after the data structures were updated
				selfPointer.repaint();
			}

			@Override
			public void mouseEntered(
					final MouseEvent arg0) {
			}

			@Override
			public void mouseExited(
					final MouseEvent arg0) {
			}

			@Override
			public void mousePressed(
					final MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(
					final MouseEvent arg0) {
			}
		});
		setSelectionModel(defaultTreeSelectionModel);
	}

	// Recursively checks/unchecks a subtree
	protected void checkSubTree(
			final TreePath treePath,
			final boolean check) {

		final CheckedNode checkedNode = nodesCheckingState.get(treePath);
		checkedNode.isSelected = check;
		final MutableTreeNode mutableTreeNode = (MutableTreeNode) treePath.getLastPathComponent();
		for (int i = 0; i < mutableTreeNode.getChildCount(); i++) {

			final TreeNode childTreeNode = mutableTreeNode.getChildAt(i);
			final TreePath childTreePath = treePath.pathByAddingChild(childTreeNode);
			checkSubTree(childTreePath, check);
		}
		checkedNode.allChildrenSelected = check;
		if (check) {
			checkedPaths.add(treePath);
		} else {
			checkedPaths.remove(treePath);
		}
	}
}
