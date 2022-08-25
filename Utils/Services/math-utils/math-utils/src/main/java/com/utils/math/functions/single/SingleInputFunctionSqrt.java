package com.utils.math.functions.single;

public class SingleInputFunctionSqrt extends AbstractSingleInputFunction {

	public SingleInputFunctionSqrt() {
		super("sqrt");
	}

	@Override
	public double compute(
			final double input) {
		return Math.sqrt(input);
	}
}
