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

		final ScriptEngine engine = ScriptUtils.createScriptEngine();
		final Object result = engine.eval("2 + 3");
		Logger.printLine(result);
	}

	@Test
	void testFibonacci() {

		final int value;
		final long expectedResult;
		final int input = Integer.parseInt("2");
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
				scriptEngine.eval(reader);

				final Invocable invocable = (Invocable) scriptEngine;
				final Object resultObj = invocable.invokeFunction("fibonacci", value);
				if (resultObj != null) {

					final String resultObjString = resultObj.toString();
					result = StrUtils.tryParsePositiveLong(resultObjString);
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to compute fibonacci number");
			Logger.printException(exc);
		}
		return result;
	}
}
