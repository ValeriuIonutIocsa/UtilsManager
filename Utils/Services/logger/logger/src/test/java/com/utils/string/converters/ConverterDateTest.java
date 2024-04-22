package com.utils.string.converters;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.utils.log.Logger;
import com.utils.test.DynamicTestOption;
import com.utils.test.DynamicTestOptions;
import com.utils.test.DynamicTestSuite;
import com.utils.test.TestInputUtils;

class ConverterDateTest {

	private static String timeZoneId;

	@BeforeAll
	static void beforeAll() {

		timeZoneId = configureTimeZoneId();
	}

	private static String configureTimeZoneId() {

		final String timeZoneId;
		final int input = TestInputUtils.parseTestInputNumber("0");
		if (input == 1) {
			timeZoneId = "UTC";
		} else if (input == 2) {
			timeZoneId = "IST";
		} else {
			timeZoneId = null;
		}
		return timeZoneId;
	}

	@TestFactory
	List<DynamicTest> testStringToDate() {

		if (timeZoneId != null) {
			TimeZone.setDefault(TimeZone.getTimeZone(timeZoneId));
		}

		final DynamicTestOptions<Date> dateDynamicTestOptions =
				new DynamicTestOptions<>("date string", 1);

		dateDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(1, "current date",
				new Date()));

		dateDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(11, "full date 1",
				ConverterDate.stringToDate("2024-Feb-18 18:23:19.674 UTC")));
		dateDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(12, "full date 2",
				ConverterDate.stringToDate("2024-Feb-18 18:23:19.674 EET")));
		dateDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(13, "full date 3",
				ConverterDate.stringToDate("2024-Apr-22 18:32:00.833 IST")));

		dateDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(21, "simple date",
				ConverterDate.stringToDate("2020-Feb-28 18:07:38")));

		final DynamicTestSuite dynamicTestSuite = new DynamicTestSuite(DynamicTestSuite.Mode.ALL,
				() -> testStringToDateCommon(dateDynamicTestOptions), dateDynamicTestOptions);

		return dynamicTestSuite.createDynamicTestList();
	}

	private static void testStringToDateCommon(
			final DynamicTestOptions<Date> dateDynamicTestOptions) {

		final Date date = dateDynamicTestOptions.computeValue();

		final String dateString = ConverterDate.dateToString(date);
		Logger.printLine("dateString: " + dateString);

		final Date secondDate = ConverterDate.stringToDate(dateString);
		final String secondDateString = ConverterDate.dateToString(secondDate);

		Assertions.assertEquals(dateString, secondDateString);
	}
}
