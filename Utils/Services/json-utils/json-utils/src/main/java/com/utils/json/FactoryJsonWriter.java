package com.utils.json;

import java.io.PrintStream;

public final class FactoryJsonWriter {

	private FactoryJsonWriter() {
	}

	public static JsonWriter newInstance(
			final PrintStream printStream) {

		return new JsonWriter("    ", " ", System.lineSeparator(), printStream);
	}

	public static JsonWriter newInstanceNoIndent(
			final PrintStream printStream) {

		return new JsonWriter("", "", "", printStream);
	}
}
