package com.utils.math.functions.two;

import com.utils.string.StrUtils;

abstract class AbstractTwoInputsFunction implements TwoInputsFunction {

	private final String name;

	AbstractTwoInputsFunction(
			final String name) {

		this.name = name;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	@Override
	public String getName() {
		return name;
	}
}
