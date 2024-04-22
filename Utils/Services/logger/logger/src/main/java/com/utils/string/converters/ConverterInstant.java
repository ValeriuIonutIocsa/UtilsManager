package com.utils.string.converters;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class ConverterInstant {

	private final static String SIMPLE_DATE_FORMAT = "yyyy-MMM-dd HH:mm:ss";
	private final static String FULL_DATE_FORMAT = "yyyy-MMM-dd HH:mm:ss.SSS zzz";

	private ConverterInstant() {
	}

	public static String instantToString(
			final Instant instant) {

		final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FULL_DATE_FORMAT)
				.withLocale(Locale.US).withZone(ZoneId.systemDefault());
		return dateTimeFormatter.format(instant);
	}

	public static Instant stringToInstant(
			final String instantString) {

		Instant instant = tryParseInstant(instantString, FULL_DATE_FORMAT);
		if (instant == null) {
			instant = tryParseInstant(instantString, SIMPLE_DATE_FORMAT);
		}
		return instant;
	}

	private static Instant tryParseInstant(
			final String instantString,
			final String dateFormat) {

		Instant instant = null;
		try {
			final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat)
					.withLocale(Locale.US).withZone(ZoneId.systemDefault());
			instant = Instant.from(dateTimeFormatter.parse(instantString));

		} catch (final Exception ignored) {
		}
		return instant;
	}
}
