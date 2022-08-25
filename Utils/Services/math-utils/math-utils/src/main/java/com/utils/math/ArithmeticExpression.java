package com.utils.math;

import java.util.HashSet;
import java.util.Set;

import com.utils.math.functions.single.SingleInputFunction;
import com.utils.math.functions.single.SingleInputFunctionCos;
import com.utils.math.functions.single.SingleInputFunctionSin;
import com.utils.math.functions.single.SingleInputFunctionSqrt;
import com.utils.math.functions.single.SingleInputFunctionTan;
import com.utils.math.functions.two.TwoInputsFunction;
import com.utils.math.functions.two.TwoInputsFunctionMax;
import com.utils.math.functions.two.TwoInputsFunctionMin;

public class ArithmeticExpression {

	private static final SingleInputFunction[] SINGLE_INPUT_FUNCTIONS = {
			new SingleInputFunctionSqrt(),
			new SingleInputFunctionSin(),
			new SingleInputFunctionCos(),
			new SingleInputFunctionTan()
	};
	private static final TwoInputsFunction[] TWO_INPUTS_FUNCTIONS = {
			new TwoInputsFunctionMin(),
			new TwoInputsFunctionMax()
	};
	private static final Set<String> FUNCTION_NAMES = new HashSet<>();
	static {
		for (final SingleInputFunction singleInputFunction : SINGLE_INPUT_FUNCTIONS) {

			final String functionName = singleInputFunction.getName();
			FUNCTION_NAMES.add(functionName);
		}
		for (final TwoInputsFunction twoInputsFunction : TWO_INPUTS_FUNCTIONS) {

			final String functionName = twoInputsFunction.getName();
			FUNCTION_NAMES.add(functionName);
		}
	}

	public static boolean isFunctionName(
			final String str) {
		return FUNCTION_NAMES.contains(str);
	}

	private final String str;
	private int pos = -1;
	private char ch;

	public ArithmeticExpression(
			final String str) {

		this.str = str;
	}

	public double tryParse(
			final double defaultValue) {

		double result = defaultValue;
		try {
			result = parse();
		} catch (final Exception ignored) {
		}
		return result;
	}

	public double parse() throws Exception {

		nextChar();

		final double x = parseExpression();
		if (pos < str.length()) {
			throw new Exception("Unexpected: " + ch);
		}

		return x;
	}

	private double parseExpression() throws Exception {

		double x = parseTerm();

		for (;;) {

			if (eat('+')) {
				x += parseTerm();

			} else if (eat('-')) {
				x -= parseTerm();

			} else {
				return x;
			}
		}
	}

	private double parseTerm() throws Exception {

		double x = parseFactor();

		for (;;) {

			if (eat('*')) {
				x *= parseFactor();

			} else if (eat('/')) {
				x /= parseFactor();

			} else {
				return x;
			}
		}
	}

	private double parseFactor() throws Exception {

		final double result;
		if (eat('+')) {
			result = parseFactor();

		} else {
			if (eat('-')) {
				result = -parseFactor();

			} else {
				Double x = null;
				final int startPos = pos;
				if (eat('(')) {
					x = parseExpression();
					eat(')');

				} else if (Character.isDigit(ch) || ch == '.') {
					final boolean startsWithZero = ch == '0';
					char lch = ch;
					while (Character.isDigit(lch) || lch == '.' ||
							startsWithZero && ('a' <= lch && lch <= 'f' || lch == 'x')) {

						nextChar();
						lch = Character.toLowerCase(ch);
					}

					final String numberString = str.substring(startPos, pos);
					if (numberString.startsWith("0x")) {
						final String hexString = numberString.substring(2);
						x = (double) Long.parseLong(hexString, 16);
					} else {
						x = Double.parseDouble(numberString);
					}

				} else if ('a' <= ch && ch <= 'z') {

					while ('a' <= ch && ch <= 'z') {
						nextChar();
					}

					final String func = str.substring(startPos, pos);
					boolean foundFunction = false;

					for (final SingleInputFunction singleInputFunction : SINGLE_INPUT_FUNCTIONS) {

						if (singleInputFunction.getName().equals(func)) {
							final double input = parseFactor();
							x = singleInputFunction.compute(input);
							foundFunction = true;
						}
					}

					for (final TwoInputsFunction twoInputsFunction : TWO_INPUTS_FUNCTIONS) {

						if (twoInputsFunction.getName().equals(func)) {
							eat('(');
							final double input1 = parseFactor();
							eat(',');
							final double input2 = parseFactor();
							eat(')');
							x = twoInputsFunction.compute(input1, input2);
							foundFunction = true;
						}
					}

					if (!foundFunction) {
						throw new Exception("Unknown function: " + func);
					}

				} else {
					throw new Exception("Unexpected: " + ch);
				}

				if (eat('^')) {
					final double exponent = parseFactor();
					x = Math.pow(x, exponent);
				}

				result = x;
			}
		}
		return result;
	}

	private boolean eat(
			final int charToEat) {

		while (ch == ' ') {
			nextChar();
		}

		boolean eaten = false;
		if (ch == charToEat) {

			nextChar();
			eaten = true;
		}
		return eaten;
	}

	private void nextChar() {

		if (++pos < str.length()) {
			ch = str.charAt(pos);
		} else {
			ch = '\0';
		}
	}
}
