package com.utils.math.functions.single;

abstract class AbstractSingleInputFunction implements SingleInputFunction {

	private final String name;

	AbstractSingleInputFunction(
			final String name) {

		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
