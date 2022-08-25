package com.utils.math.functions.single;

public class SingleInputFunctionSin extends AbstractSingleInputFunction {

	public SingleInputFunctionSin() {
		super("sin");
	}

	@Override
	public double compute(
			final double input) {
		return Math.sin(Math.toRadians(input));
	}
}
