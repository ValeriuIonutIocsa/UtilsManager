package com.utils.math;

import java.util.List;

import com.utils.annotations.ApiMethod;

public final class MathUtils {

	private MathUtils() {
	}

	@ApiMethod
	public static int sum(
			final int... values) {

		int sum = 0;
		for (final int value : values) {

			sum += value;
		}
		return sum;
	}

	@ApiMethod
	public static long sum(
			final long... values) {

		long sum = 0;
		for (final long value : values) {

			sum += value;
		}
		return sum;
	}

	@ApiMethod
	public static int min(
			final int... values) {

		int min = Integer.MAX_VALUE;
		for (final int value : values) {

			if (value < min) {
				min = value;
			}
		}
		return min;
	}

	@ApiMethod
	public static long min(
			final long... values) {

		long min = Long.MAX_VALUE;
		for (final long value : values) {

			if (value < min) {
				min = value;
			}
		}
		return min;
	}

	@ApiMethod
	public static int max(
			final int... values) {

		int max = Integer.MIN_VALUE;
		for (final int value : values) {

			if (value > max) {
				max = value;
			}
		}
		return max;
	}

	@ApiMethod
	public static long max(
			final long... values) {

		long max = Long.MIN_VALUE;
		for (final long value : values) {

			if (value > max) {
				max = value;
			}
		}
		return max;
	}

	@ApiMethod
	public static double average(
			final List<Double> cpuLoadValues) {

		double average = Double.NaN;
		if (!cpuLoadValues.isEmpty()) {

			double sum = 0;
			for (final double cpuLoadValue : cpuLoadValues) {
				sum += cpuLoadValue;
			}
			average = sum / cpuLoadValues.size();
		}
		return average;
	}

	/**
	 * @param n
	 *            non-negative integer to be rounded to the next multiple of d
	 * @param d
	 *            integer greater than 0
	 * @return the smallest multiple of d greater than n
	 */
	@ApiMethod
	public static int roundToNextMultiple(
			final int n,
			final int d) {
		return (n + d - 1) / d * d;
	}

	@ApiMethod
	public static boolean checkInRange(
			final int value,
			final int start,
			final int end) {
		return start <= value && value <= end;
	}

	@ApiMethod
	public static boolean checkInRange(
			final long value,
			final long start,
			final long end) {
		return start <= value && value <= end;
	}

	@ApiMethod
	public static boolean checkInRange(
			final double value,
			final double start,
			final double end) {
		return start - 0.001 <= value && value <= end + 0.001;
	}

	@ApiMethod
	public static boolean checkIntervalNotInsideAnother(
			final int start1,
			final int end1,
			final int start2,
			final int end2) {
		return start2 > start1 || end1 > end2;
	}

	@ApiMethod
	public static boolean checkIntervalNotInsideAnother(
			final long start1,
			final long end1,
			final long start2,
			final long end2) {
		return start2 > start1 || end1 > end2;
	}

	@ApiMethod
	public static boolean checkIntervalsIntersect(
			final int start1,
			final int end1,
			final int start2,
			final int end2) {
		return (end1 - start1) + (end2 - start2) > Math.max(end1, end2) - Math.min(start1, start2);
	}

	@ApiMethod
	public static boolean checkIntervalsIntersect(
			final long start1,
			final long end1,
			final long start2,
			final long end2) {
		return (end1 - start1) + (end2 - start2) > Math.max(end1, end2) - Math.min(start1, start2);
	}

	@ApiMethod
	public static boolean checkAllPositive(
			final int... numberArray) {

		boolean allPositive = true;
		for (final int number : numberArray) {

			if (number < 0) {

				allPositive = false;
				break;
			}
		}
		return allPositive;
	}

	@ApiMethod
	public static boolean checkAllPositive(
			final long... numberArray) {

		boolean allPositive = true;
		for (final long number : numberArray) {

			if (number < 0) {

				allPositive = false;
				break;
			}
		}
		return allPositive;
	}

	@ApiMethod
	public static boolean checkPowerOfTwo(
			final int n) {
		return n != 0 && (n & (n - 1)) == 0;
	}

	@ApiMethod
	public static boolean checkPowerOfTwo(
			final long n) {
		return n != 0 && (n & (n - 1)) == 0;
	}

	@ApiMethod
	public static int roundToNearestPowerOfTwo(
			final int n) {

		final int result;
		if (n < 1) {
			result = 1;

		} else {
			int exp = 0;
			int tmp = n;
			while (tmp != 1) {

				tmp /= 2;
				exp++;
			}
			final int lowerPower = pow(2, exp);
			final int higherPower = lowerPower * 2;
			if (n - lowerPower < higherPower - n) {
				result = lowerPower;
			} else {
				result = higherPower;
			}
		}
		return result;
	}

	@ApiMethod
	public static long roundToNearestPowerOfTwo(
			final long n) {

		final long result;
		if (n < 1) {
			result = 1;

		} else {
			int exp = 0;
			long tmp = n;
			while (tmp != 1) {

				tmp /= 2;
				exp++;
			}
			final int lowerPower = pow(2, exp);
			final int higherPower = lowerPower * 2;
			if (n - lowerPower < higherPower - n) {
				result = lowerPower;
			} else {
				result = higherPower;
			}
		}
		return result;
	}

	@ApiMethod
	public static int pow(
			final int n,
			final int exp) {

		int result = 1;
		for (int i = 0; i < exp; i++) {
			result *= n;
		}
		return result;
	}

	@ApiMethod
	public static long pow(
			final long n,
			final long exp) {

		long result = 1;
		for (int i = 0; i < exp; i++) {
			result *= n;
		}
		return result;
	}
}
