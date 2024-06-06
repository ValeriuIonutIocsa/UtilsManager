package com.utils.js;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.script.Invocable;
import javax.script.ScriptEngine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.utils.log.Logger;
import com.utils.string.StrUtils;

class ScriptUtilsTest {

	@Test
	void testCreateScriptEngine() throws Exception {

		int result = -1;
		final ScriptEngine scriptEngine = ScriptUtils.createScriptEngine();
		if (scriptEngine != null) {

			final Object resultObject = scriptEngine.eval("2 + 3");
			final double resultDouble =
					StrUtils.tryParseDouble(resultObject.toString(), Double.NaN);
			if (!Double.isNaN(resultDouble)) {
				result = (int) resultDouble;
			}
		}
		Assertions.assertEquals(5, result);
	}

	@Test
	void testFibonacci() {

		final int value;
		final long expectedResult;
		final int input = StrUtils.tryParsePositiveInt("1");
		if (input == 1) {

			value = 10;
			expectedResult = 89;

		} else if (input == 2) {

			value = 5;
			expectedResult = 8;

		} else {
			throw new RuntimeException();
		}

		final long result = computeFibonacciJs(value);
		Assertions.assertEquals(expectedResult, result);
	}

	private static long computeFibonacciJs(
			final int value) {

		long result = -1;
		try {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(
					Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("com/utils/js/fibonacci.js"))))) {

				final ScriptEngine scriptEngine = ScriptUtils.createScriptEngine();
				if (scriptEngine != null) {

					scriptEngine.eval(reader);

					final Invocable invocable = (Invocable) scriptEngine;
					final Object resultObj = invocable.invokeFunction("fibonacci", value);
					if (resultObj != null) {

						final String resultObjString = resultObj.toString();
						final double resultDouble = StrUtils.tryParseDouble(resultObjString, Double.NaN);
						if (!Double.isNaN(resultDouble)) {

							result = (int) resultDouble;
						}
					}
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to compute fibonacci number");
			Logger.printException(exc);
		}
		return result;
	}
}
