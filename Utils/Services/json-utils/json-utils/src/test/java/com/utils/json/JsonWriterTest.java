package com.utils.json;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.log.Logger;
import com.utils.test.TestInputUtils;

class JsonWriterTest {

	@Test
	void testWrite() {

		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try (PrintStream printStream = new PrintStream(byteArrayOutputStream)) {

			final JsonWriter jsonWriter = FactoryJsonWriter.newInstance(printStream);

			final boolean testBoolean = TestInputUtils.parseTestInputBoolean("true");
			final TestJsonObject testJsonObject = new TestJsonObject(testBoolean);
			jsonWriter.writeObject(testJsonObject, 0, TestJsonObject::writeToJson);
		}
		final String jsonString = byteArrayOutputStream.toString();
		Assertions.assertFalse(StringUtils.isBlank(jsonString));

		Logger.printNewLine();
		Logger.printLine(jsonString);
	}
}
