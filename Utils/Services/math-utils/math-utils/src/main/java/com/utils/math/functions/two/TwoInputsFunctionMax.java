package com.utils.math.functions.two;

public class TwoInputsFunctionMax extends AbstractTwoInputsFunction {

	public TwoInputsFunctionMax() {
		super("max");
	}

	@Override
	public double compute(
			final double input1,
			final double input2) {
		return Math.max(input1, input2);
	}
}
