package com.utils.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import com.utils.test.DynamicTestOption;
import com.utils.test.DynamicTestOptions;
import com.utils.test.DynamicTestSuite;

class MathUtilsTest {

	@Test
	void testMax() {

		final int max = MathUtils.max(5, 3, 2, 7, 6, 2, 1);
		Assertions.assertEquals(7, max);
	}

	@TestFactory
	List<DynamicTest> testRoundToNextMultiple() {

		final DynamicTestOptions<TestRoundToNextMultipleData> inputDynamicTestOptions =
				new DynamicTestOptions<>("input", 1);

		inputDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(1, null,
				new TestRoundToNextMultipleData(13, 4, 16)));
		inputDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(2, null,
				new TestRoundToNextMultipleData(8, 4, 8)));
		inputDynamicTestOptions.getDynamicTestOptionList().add(new DynamicTestOption<>(3, null,
				new TestRoundToNextMultipleData(19, 5, 20)));

		final DynamicTestSuite dynamicTestSuite = new DynamicTestSuite(DynamicTestSuite.Mode.ALL,
				() -> testRoundToNextMultipleCommon(inputDynamicTestOptions),
				inputDynamicTestOptions);

		return dynamicTestSuite.createDynamicTestList();
	}

	private static void testRoundToNextMultipleCommon(
			final DynamicTestOptions<TestRoundToNextMultipleData> inputDynamicTestOptions) {

		final TestRoundToNextMultipleData testRoundToNextMultipleData =
				inputDynamicTestOptions.computeValue();
		final int result = MathUtils.roundToNextMultiple(
				testRoundToNextMultipleData.n, testRoundToNextMultipleData.d);
		Assertions.assertEquals(testRoundToNextMultipleData.expectedResult, result);
	}

	private static class TestRoundToNextMultipleData {

		private final int n;
		private final int d;
		private final int expectedResult;

		public TestRoundToNextMultipleData(
				final int n,
				final int d,
				final int expectedResult) {

			this.n = n;
			this.d = d;
			this.expectedResult = expectedResult;
		}
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
