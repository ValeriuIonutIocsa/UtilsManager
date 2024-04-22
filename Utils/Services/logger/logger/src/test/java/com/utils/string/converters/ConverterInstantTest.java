package com.utils.string.converters;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.utils.log.Logger;
import com.utils.test.DynamicTestOption;
import com.utils.test.DynamicTestOptions;
import com.utils.test.DynamicTestSuite;

class ConverterInstantTest {

	@TestFactory
	List<DynamicTest> testStringToInstant() {

		final DynamicTestOptions<Instant> instantDynamicTestOptions =
				new DynamicTestOptions<>("instant string", 1);

		instantDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(1, "current instant",
				Instant.now()));

		instantDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(11, "full instant 1",
				ConverterInstant.stringToInstant("2024-Feb-18 18:23:19.674 UTC")));
		instantDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(12, "full instant 2",
				ConverterInstant.stringToInstant("2024-Feb-18 18:23:19.674 EET")));
		instantDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(13, "full instant 3",
				ConverterInstant.stringToInstant("2024-Apr-22 18:32:00.833 Asia/Kolkata")));

		instantDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(21, "simple instant",
				ConverterInstant.stringToInstant("2020-Feb-28 18:07:38")));

		final DynamicTestSuite dynamicTestSuite = new DynamicTestSuite(DynamicTestSuite.Mode.ALL,
				() -> testStringToInstantCommon(instantDynamicTestOptions), instantDynamicTestOptions);

		return dynamicTestSuite.createDynamicTestList();
	}

	private static void testStringToInstantCommon(
			final DynamicTestOptions<Instant> instantDynamicTestOptions) {

		final Instant instant = instantDynamicTestOptions.computeValue();

		final String instantString = ConverterInstant.instantToString(instant);
		Logger.printLine("instantString: " + instantString);

		final Instant secondInstant = ConverterInstant.stringToInstant(instantString);
		final String secondInstantString = ConverterInstant.instantToString(secondInstant);

		Assertions.assertEquals(instantString, secondInstantString);
	}
}
