package com.utils.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class MathUtilsTest {

	@Test
	void testMax() {

		final int max = MathUtils.max(5, 3, 2, 7, 6, 2, 1);
		Assertions.assertEquals(7, max);
	}

	@TestFactory
	List<DynamicTest> testRoundToNextMultiple() {

		final List<DynamicTest> dynamicTestList = new ArrayList<>();
		final List<Integer> testCaseList = Arrays.asList(0, 1, 2, 3, 4);
		if (testCaseList.contains(1)) {
			dynamicTestList.add(DynamicTest.dynamicTest("1",
					() -> testRoundToNextMultipleCommon(13, 4, 16)));
		}
		if (testCaseList.contains(2)) {
			dynamicTestList.add(DynamicTest.dynamicTest("2",
					() -> testRoundToNextMultipleCommon(8, 4, 8)));
		}
		if (testCaseList.contains(3)) {
			dynamicTestList.add(DynamicTest.dynamicTest("3",
					() -> testRoundToNextMultipleCommon(19, 5, 20)));
		}
		return dynamicTestList;
	}

	private static void testRoundToNextMultipleCommon(
			final int n,
			final int d,
			final int expectedResult) {

		final int result = MathUtils.roundToNextMultiple(n, d);
		Assertions.assertEquals(expectedResult, result);
	}

	@TestFactory
	List<DynamicTest> testCheckPowerOfTwo() {

		final List<DynamicTest> dynamicTestList = new ArrayList<>();
		final List<Integer> testCaseList = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
		if (testCaseList.contains(1)) {
			dynamicTestList.add(DynamicTest.dynamicTest("1",
					() -> testCheckPowerOfTwoCommon(0, false)));
		}
		if (testCaseList.contains(2)) {
			dynamicTestList.add(DynamicTest.dynamicTest("2",
					() -> testCheckPowerOfTwoCommon(1, true)));
		}
		if (testCaseList.contains(3)) {
			dynamicTestList.add(DynamicTest.dynamicTest("3",
					() -> testCheckPowerOfTwoCommon(5, false)));
		}
		if (testCaseList.contains(4)) {
			dynamicTestList.add(DynamicTest.dynamicTest("4",
					() -> testCheckPowerOfTwoCommon(512, true)));
		}
		if (testCaseList.contains(5)) {
			dynamicTestList.add(DynamicTest.dynamicTest("5",
					() -> testCheckPowerOfTwoCommon(120, false)));
		}
		if (testCaseList.contains(6)) {
			dynamicTestList.add(DynamicTest.dynamicTest("6",
					() -> testCheckPowerOfTwoCommon(1_073_741_824, true)));
		}
		return dynamicTestList;
	}

	private static void testCheckPowerOfTwoCommon(
			final int n,
			final boolean expectedResult) {

		final boolean result = MathUtils.checkPowerOfTwo(n);
		Assertions.assertEquals(expectedResult, result);
	}

	@TestFactory
	List<DynamicTest> testRoundToNearestPowerOfTwo() {

		final List<DynamicTest> dynamicTestList = new ArrayList<>();
		final List<Integer> testCaseList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
		if (testCaseList.contains(1)) {
			dynamicTestList.add(DynamicTest.dynamicTest("1",
					() -> testRoundToNearestPowerOfTwoCommon(5, 4)));
		}
		if (testCaseList.contains(2)) {
			dynamicTestList.add(DynamicTest.dynamicTest("2",
					() -> testRoundToNearestPowerOfTwoCommon(7, 8)));
		}
		if (testCaseList.contains(3)) {
			dynamicTestList.add(DynamicTest.dynamicTest("3",
					() -> testRoundToNearestPowerOfTwoCommon(16, 16)));
		}
		if (testCaseList.contains(4)) {
			dynamicTestList.add(DynamicTest.dynamicTest("4",
					() -> testRoundToNearestPowerOfTwoCommon(0, 1)));
		}
		if (testCaseList.contains(5)) {
			dynamicTestList.add(DynamicTest.dynamicTest("5",
					() -> testRoundToNearestPowerOfTwoCommon(1, 1)));
		}
		if (testCaseList.contains(6)) {
			dynamicTestList.add(DynamicTest.dynamicTest("6",
					() -> testRoundToNearestPowerOfTwoCommon(1_073_741_820, 1_073_741_824)));
		}
		if (testCaseList.contains(7)) {
			dynamicTestList.add(DynamicTest.dynamicTest("7",
					() -> testRoundToNearestPowerOfTwoCommon(1_073_741_860, 1_073_741_824)));
		}
		return dynamicTestList;
	}

	private static void testRoundToNearestPowerOfTwoCommon(
			final int n,
			final int expectedResult) {

		final int result = MathUtils.roundToNearestPowerOfTwo(n);
		Assertions.assertEquals(expectedResult, result);
	}
}
