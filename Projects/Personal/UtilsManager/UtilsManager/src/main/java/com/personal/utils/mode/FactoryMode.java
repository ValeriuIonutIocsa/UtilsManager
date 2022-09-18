package com.personal.utils.mode;

import org.apache.commons.lang3.StringUtils;

public final class FactoryMode {

	private static final Mode[] VALUES = Mode.values();

	private FactoryMode() {
	}

	public static Mode computeInstance(
            final String name) {

		Mode mode = null;
		for (final Mode aMode : VALUES) {

			final String aModeName = aMode.name();
			if (StringUtils.equalsIgnoreCase(name, aModeName)) {

				mode = aMode;
				break;
			}
		}
		return mode;
	}
}
