package com.utils.math.functions.single;

public class SingleInputFunctionCos extends AbstractSingleInputFunction {

	public SingleInputFunctionCos() {
		super("cos");
	}

	@Override
	public double compute(
			final double input) {
		return Math.cos(Math.toRadians(input));
	}
}
