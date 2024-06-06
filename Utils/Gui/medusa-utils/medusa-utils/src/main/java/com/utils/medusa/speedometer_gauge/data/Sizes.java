package com.utils.medusa.speedometer_gauge.data;

import com.utils.string.StrUtils;

public record Sizes(
		double width,
		double height) {

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
