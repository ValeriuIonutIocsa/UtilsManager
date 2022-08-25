package com.utils.medusa.speedometer_gauge.data;

public class Values {

	private final double maxValue;
	private final double minValue;
	private final double value;

	public Values(
			final double minValue,
			final double maxValue,
			final double value) {

		this.minValue = minValue;
		this.maxValue = maxValue;
		this.value = value;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getValue() {
		return value;
	}
}
