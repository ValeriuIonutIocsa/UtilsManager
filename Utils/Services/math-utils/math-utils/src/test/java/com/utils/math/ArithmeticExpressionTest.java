package com.utils.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class ArithmeticExpressionTest {

	@TestFactory
	List<DynamicTest> testParse() {

		final List<DynamicTest> dynamicTestList = new ArrayList<>();
		final List<Integer> testCaseList = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
		if (testCaseList.contains(1)) {
			dynamicTestList.add(DynamicTest.dynamicTest("1 (simple addition)",
					() -> testParseCommon("2 + 3", 5)));
		}
		if (testCaseList.contains(2)) {
			dynamicTestList.add(DynamicTest.dynamicTest("2 (simple subtraction)",
					() -> testParseCommon("8 - 4", 4)));
		}
		if (testCaseList.contains(3)) {
			dynamicTestList.add(DynamicTest.dynamicTest("3 (hex string)",
					() -> testParseCommon("0x7001c000", 0x7001c000)));
		}
		if (testCaseList.contains(4)) {
			dynamicTestList.add(DynamicTest.dynamicTest("4 (hex subtraction)",
					() -> testParseCommon("0x801 - 0x12", 0x7ef)));
		}
		if (testCaseList.contains(5)) {
			dynamicTestList.add(DynamicTest.dynamicTest("5 (max)",
					() -> testParseCommon("2 + max(8, 14) + 4", 20)));
		}
		if (testCaseList.contains(6)) {
			dynamicTestList.add(DynamicTest.dynamicTest("6 (double division)",
					() -> testParseCommon("1 / 0.56", 1.785_710)));
		}
		return dynamicTestList;
	}

	private static void testParseCommon(
			final String expressionString,
			final double expectedResult) throws Exception {

		final double result = new ArithmeticExpression(expressionString).parse();
		Assertions.assertEquals(expectedResult, result, 0.001);
	}
}
