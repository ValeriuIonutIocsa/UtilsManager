package com.utils.gui.objects.list_select;

import javafx.collections.ListChangeListener;
import javafx.scene.control.TablePosition;

class ListChangeListenerListSelectionView<
		TablePositionT extends TablePosition<?, ?>>
		implements ListChangeListener<TablePositionT> {

	private final CustomListSelectionView<?, ?> customListSelectionView;

	ListChangeListenerListSelectionView(
			final CustomListSelectionView<?, ?> customListSelectionView) {

		this.customListSelectionView = customListSelectionView;
	}

	@Override
	public void onChanged(
			final Change<? extends TablePositionT> change) {
		customListSelectionView.updateButtons();
	}
}
