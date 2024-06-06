package com.utils.medusa.speedometer_gauge.data;

import com.utils.string.StrUtils;

public record Values(
		double minValue,
		double maxValue,
		double value) {

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
