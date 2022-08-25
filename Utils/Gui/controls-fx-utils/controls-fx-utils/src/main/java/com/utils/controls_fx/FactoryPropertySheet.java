package com.utils.controls_fx;

import org.controlsfx.control.PropertySheet;

import com.utils.annotations.ApiMethod;

public final class FactoryPropertySheet {

	private FactoryPropertySheet() {
	}

	@ApiMethod
	public static PropertySheet createPropertySheet() {

		final PropertySheet propertySheet = new PropertySheet();
		propertySheet.setModeSwitcherVisible(false);
		propertySheet.setSearchBoxVisible(false);
		return propertySheet;
	}
}
