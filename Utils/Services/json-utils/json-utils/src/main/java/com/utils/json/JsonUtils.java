package com.utils.json;

import java.io.PrintStream;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.TriConsumer;

import com.utils.string.StrUtils;

public final class JsonUtils {

	private JsonUtils() {
	}

	public static <
			ObjectT> void writeObject(
					final ObjectT object,
					final int indentCount,
					final PrintStream printStream,
					final TriConsumer<ObjectT, Integer, PrintStream> writeObjectTriConsumer) {

		StrUtils.printRepeatedString("    ", indentCount, printStream);
		printStream.print('{');
		printStream.println();

		if (object != null) {
			writeObjectTriConsumer.accept(object, indentCount, printStream);
		}

		StrUtils.printRepeatedString("    ", indentCount, printStream);
		printStream.print('}');
	}

	public static <
			ObjectT> void writeListAttribute(
					final String name,
					final Collection<ObjectT> objectCollection,
					final boolean notLastAttribute,
					final int indentCount,
					final PrintStream printStream,
					final TriConsumer<ObjectT, Integer, PrintStream> writeObjectTriConsumer) {

		StrUtils.printRepeatedString("    ", indentCount + 1, printStream);

		if (StringUtils.isNotBlank(name)) {

			printStream.print('"');
			printStream.print(name);
			printStream.print("\": ");
		}

		if (objectCollection != null && !objectCollection.isEmpty()) {

			printStream.print('[');
			printStream.println();

			int index = 0;
			final int size = objectCollection.size();
			for (final ObjectT object : objectCollection) {

				writeObject(object, indentCount + 2, printStream, writeObjectTriConsumer);

				if (index < size - 1) {
					printStream.print(',');
				}
				printStream.println();
				index++;
			}

			StrUtils.printRepeatedString("    ", indentCount + 1, printStream);
			printStream.print(']');

		} else {
			printStream.print("\"\"");
		}

		if (notLastAttribute) {
			printStream.print(',');
		}
		printStream.println();
	}

	public static <
			ObjectT> void writeObjectAttribute(
					final String name,
					final ObjectT object,
					final boolean notLastAttribute,
					final int indentCount,
					final PrintStream printStream,
					final TriConsumer<ObjectT, Integer, PrintStream> writeObjectTriConsumer) {

		StrUtils.printRepeatedString("    ", indentCount + 1, printStream);

		if (StringUtils.isNotBlank(name)) {

			printStream.print('"');
			printStream.print(name);
			printStream.print("\": ");
		}

		if (object != null) {

			printStream.print('{');
			printStream.println();

			writeObjectTriConsumer.accept(object, indentCount + 1, printStream);

			StrUtils.printRepeatedString("    ", indentCount + 1, printStream);
			printStream.print('}');

		} else {
			printStream.print("\"\"");
		}

		if (notLastAttribute) {
			printStream.print(',');
		}
		printStream.println();
	}

	public static void writeStringAttribute(
			final String name,
			final String value,
			final boolean notLastAttribute,
			final int indentCount,
			final PrintStream printStream) {

		StrUtils.printRepeatedString("    ", indentCount + 1, printStream);

		if (StringUtils.isNotBlank(name)) {

			printStream.print('"');
			printStream.print(name);
			printStream.print("\": ");
		}

		printStream.print('"');
		if (value != null) {
			printStream.print(value);
		}
		printStream.print('"');

		if (notLastAttribute) {
			printStream.print(',');
		}
		printStream.println();
	}
}
