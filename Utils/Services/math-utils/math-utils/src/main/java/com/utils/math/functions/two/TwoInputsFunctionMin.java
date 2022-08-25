package com.utils.math.functions.two;

public class TwoInputsFunctionMin extends AbstractTwoInputsFunction {

	public TwoInputsFunctionMin() {
		super("min");
	}

	@Override
	public double compute(
			final double input1,
			final double input2) {
		return Math.min(input1, input2);
	}
}
