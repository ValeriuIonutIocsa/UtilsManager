package com.utils.math.functions.single;

public class SingleInputFunctionTan extends AbstractSingleInputFunction {

	public SingleInputFunctionTan() {
		super("tan");
	}

	@Override
	public double compute(
			final double input) {
		return Math.tan(Math.toRadians(input));
	}
}
